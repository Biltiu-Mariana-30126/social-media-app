package socialmedia.server.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@CrossOrigin
public class UserController {

    @Autowired
    private UserService userService;

    // Create a new user
    @PostMapping
    public User createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    // Retrieve all users
    @GetMapping
    public List<User> getAllUsers() {
        return userService.findAllUsers();
    }

    // Retrieve a user by ID
    @GetMapping("/{id}")
    public User getUserById(@PathVariable int id) {
        return userService.findUserById(id);
    }

    // Delete a user by ID
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable int id) {
        userService.deleteById(id);
    }

    // Login endpoint: authenticate the user by email
    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody String email) {
        try {
            // Call the service to find user by email
            User user = userService.authenticateByEmail(email);

            // If user is found, return 200 OK
            if (user != null) {
                return ResponseEntity.ok(user);
            } else {
                // If no user is found, return 404 Not Found
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        } catch (Exception e) {
            // Log the error for debugging purposes
            e.printStackTrace();
            // Return 500 if any server-side issue occurs
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
