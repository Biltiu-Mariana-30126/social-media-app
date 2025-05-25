package socialmedia.server.post;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import socialmedia.server.services.JwtService;
import socialmedia.server.user.User;
import socialmedia.server.user.UserService;

import java.util.List;

@RestController
@RequestMapping("/posts")
@CrossOrigin
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private UserService userService;

    @Autowired private JwtService jwtService;

    @PostMapping
    public Post create(
            @RequestBody Post post,
            @RequestHeader("Authorization") String authHeader
    ) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Missing or invalid Authorization header");
        }
        String token = authHeader.substring(7);

        // 1) Verify & parse the JWT
        Claims claims = jwtService.verifyToken(token);

        // 2) Extract the userId claim
        Integer userId = claims.get("userId", Integer.class);
        if (userId == null) {
            throw new RuntimeException("Invalid token: no userId");
        }

        // 3) Load the User
        User user = userService.findById(userId);

        // 4) Associate and save
        post.setUser(user);
        post.setStatus(Status.PENDING);
        return postService.create(post);
    }

    @GetMapping
    public List<Post> getAllPosts(){
        return postService.findAllPosts();
    }

    @PutMapping("/{id}")
    public Post updatePost(@PathVariable int id, @RequestBody Post updatedPost) {
        return postService.updatePost(id, updatedPost);
    }

    @DeleteMapping("/{id}")
    public void deletePost(@PathVariable int id) {
        postService.deleteById(id);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Post> getById(@PathVariable int id) {
        return postService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

}
