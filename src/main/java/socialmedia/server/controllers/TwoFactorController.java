package socialmedia.server.controllers;

import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import io.jsonwebtoken.JwtException;
import socialmedia.server.services.JwtService;
import socialmedia.server.user.User;
import socialmedia.server.user.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;

import com.warrenstrange.googleauth.GoogleAuthenticatorQRGenerator;

import io.jsonwebtoken.Claims;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Map;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "http://localhost:8080")
public class TwoFactorController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    // 1) Generate (or re-generate) the TOTP secret + QR
    @PostMapping("/{id}/2fa/setup")
    public ResponseEntity<Map<String,String>> setup2FA(
            @PathVariable int id) throws WriterException, IOException {

        User u = userService.findById(id);
        GoogleAuthenticatorKey key = userService.setup2FA(u);

        // Build URI
        String otpAuth = GoogleAuthenticatorQRGenerator
                .getOtpAuthURL("SocialMediaApp", u.getEmail(), key);

        // Render QR
        BitMatrix matrix = new QRCodeWriter()
                .encode(otpAuth, BarcodeFormat.QR_CODE, 200, 200);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(matrix, "PNG", out);
        String b64 = Base64.getEncoder().encodeToString(out.toByteArray());

        // Return both
        return ResponseEntity.ok(Map.of(
                "qr",     b64,
                "secret", key.getKey()
        ));
    }


    // 2) After scanning, confirm the first code and flip the “using2FA” flag
    @PostMapping("/2fa/confirm")
    public ResponseEntity<?> confirm2FA(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody Map<String,String> body
    ) {
        // 1) Check we got “Bearer <token>”
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Missing or invalid Authorization header");
        }
        String token = authHeader.substring(7);

        // 2) Validate & parse your full-login JWT
        Claims claims;
        try {
            claims = jwtService.verifyToken(token);
        } catch (JwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid or expired token");
        }

        // 3) Load the user
        Integer userId = claims.get("userId", Integer.class);
        User u = userService.findById(userId);
        if (u == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Unknown user");
        }

        // 4) Verify the TOTP code
        int code;
        try {
            code = Integer.parseInt(body.get("code"));
        } catch (NumberFormatException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Code must be a number");
        }
        if (!userService.verify2FACode(u, code)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Invalid 2FA code");
        }

        // 5) Turn on 2FA and persist
        u.setUsing2FA(true);
        userService.save(u);

        return ResponseEntity.ok().build();
    }


    // 3) Verify a pre-2fa token + OTP at login time
    @PostMapping("/2fa/verify")
    public ResponseEntity<Map<String,Object>> verify2FA(@RequestBody Map<String,String> body) {
        String pre2fa = body.get("pre2faToken");
        int code = Integer.parseInt(body.get("code"));

        // Validate & parse your “pre2fa” JWT to extract userId
        Claims claims = jwtService.verifyPre2faToken(pre2fa);
        int userId = claims.get("userId", Integer.class);
        User u = userService.findById(userId);

        if (!userService.verify2FACode(u, code)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // Issue real JWT
        String jwt = jwtService.createToken(u);

        // Build a safe user DTO
        Map<String,Object> userDto = Map.of(
                "id",    u.getId(),
                "email", u.getEmail(),
                "name",  u.getName()
        );

        // Return both token and user
        return ResponseEntity.ok(Map.of(
                "token", jwt,
                "user",  userDto
        ));
    }
}