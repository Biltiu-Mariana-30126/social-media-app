package socialmedia.server.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User save(User user) {
        return userRepository.save(user);
    }

    public User createUser(User user, String rawPassword) {
        String hash = passwordEncoder.encode(rawPassword);
        user.setPasswordHash(hash);
        return userRepository.save(user);
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public User findById(int id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    public User authenticateByEmail(String email) {
        User user = userRepository.findByEmail(email);
        return user;
    }

    public boolean matchesPassword(String rawPassword, String storedHash) {
        return passwordEncoder.matches(rawPassword, storedHash);
    }

    public void deleteById(int id) {
        userRepository.deleteById(id);
    }

    public GoogleAuthenticatorKey setup2FA(User u) {
        if (u.getSecret2FA() == null) {
            GoogleAuthenticator gAuth = new GoogleAuthenticator();
            GoogleAuthenticatorKey key = gAuth.createCredentials();
            u.setSecret2FA(key.getKey());      // store the Base32 string
            userRepository.save(u);
            return key;
        }
        // If already generated, you’ll need to reconstruct the Key object:
        return new GoogleAuthenticatorKey.Builder(u.getSecret2FA()).build();
    }

    public boolean verify2FACode(User u, int code) {
        GoogleAuthenticator gAuth = new GoogleAuthenticator();
        return gAuth.authorize(u.getSecret2FA(), code);
    }
}
