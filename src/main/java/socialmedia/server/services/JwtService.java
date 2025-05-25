package socialmedia.server.services;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import socialmedia.server.user.User;



import java.security.Key;
import java.util.Date;

@Service
public class JwtService {
    // Replace with something from your application.properties or env var
    private static final String SECRET = "replace_this_with_a_very_long_random_secret";
    private static final long JWT_TTL_MS    = 1000L * 60 * 60 * 24; // 24h
    private static final long PRE2FA_TTL_MS = 1000L * 60 * 5;       // 5m

    private Key key;

    @PostConstruct
    public void init() {
        // Use an HMAC-SHA key
        this.key = Keys.hmacShaKeyFor(SECRET.getBytes());
    }

    /** Create the normal JWT after full login (or pre2fa verification). */
    public String createToken(User u) {
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .setSubject(u.getEmail())
                .claim("userId", u.getId())
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + JWT_TTL_MS))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /** Create a “pre2fa” token when the user has valid credentials but needs to do 2FA. */
    public String createPre2faToken(User u) {
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .setSubject(u.getEmail())
                .claim("userId", u.getId())
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + PRE2FA_TTL_MS))
                .claim("pre2fa", true)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /** Parse & validate a “pre2fa” token—will throw if invalid/expired. */
    public Claims verifyPre2faToken(String token) {
        JwtParser parser = Jwts.parserBuilder()
                .require("pre2fa", true)
                .setSigningKey(key)
                .build();
        return parser.parseClaimsJws(token).getBody();
    }

    public Claims verifyToken(String token) {
        JwtParser parser = Jwts.parserBuilder()
                .setSigningKey(key)
                .build();
        return parser.parseClaimsJws(token).getBody();
    }
}