package socialmedia.server.post;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
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

    @PostMapping
    public Post create(@RequestBody Post post)
    {
        // Find the user with ID 1
        User user = userService.findUserById(1); // You could change the ID if you want to use other IDs


        if (user != null) {
            // Associate the user with the post
            post.setUser(user);
            post.setStatus(Status.PENDING);  // Assuming 'PENDING' is a default status (you can set others)
            return postService.create(post);  // Save the post in the repository
        } else {
            throw new RuntimeException("User not found");
        }
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

}
