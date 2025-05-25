package socialmedia.server.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import socialmedia.server.services.JwtService;

@RestController
@RequestMapping("/users")
@CrossOrigin
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired private JwtService jwtService;

    // Retrieve all users
    @GetMapping
    public List<User> getAllUsers() {
        return userService.findAllUsers();
    }

    // Retrieve a user by ID
    @GetMapping("/{id}")
    public User getUserById(@PathVariable int id) {
        return userService.findById(id);
    }

    // Delete a user by ID
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable int id) {
        userService.deleteById(id);
    }

    @PostMapping("/signup")
    public ResponseEntity<Void> signup(@RequestBody SignupRequest req) {
        // Build a new User entity from the incoming DTO
        User u = new User();
        u.setEmail(req.getEmail());
        u.setName(req.getName());
        // Pass both the User *and* the raw password into your service
        userService.createUser(u, req.getPassword());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req) {
        // 1) lookup
        Optional<User> opt = Optional.ofNullable(
                userService.authenticateByEmail(req.getEmail())
        );
        if (opt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        User u = opt.get();

        // 2) verify password
        if (!userService.matchesPassword(req.getPassword(), u.getPasswordHash())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // 3) branch on 2FA setting
        if (u.isUsing2FA()) {
            // user has 2FA turned on → issue pre-2FA ticket
            String pre2fa = jwtService.createPre2faToken(u);
            return ResponseEntity
                    .status(HttpStatus.ACCEPTED)
                    .body(Map.of("pre2faToken", pre2fa));
        }

        // 4) 2FA is *not* enabled → issue final JWT + user info
        String jwt = jwtService.createToken(u);

        // Build a minimal DTO so we don't leak sensitive fields
        Map<String,Object> userDto = Map.of(
                "id",    u.getId(),
                "email", u.getEmail(),
                "name",  u.getName()
        );

        return ResponseEntity.ok(
                Map.of(
                        "token", jwt,
                        "user",  userDto
                )
        );
    }

}
