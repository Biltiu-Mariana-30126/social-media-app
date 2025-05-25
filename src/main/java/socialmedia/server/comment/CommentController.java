package socialmedia.server.comment;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import socialmedia.server.post.Post;
import socialmedia.server.post.PostService;
import socialmedia.server.services.JwtService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/posts/{postId}/comments")
@CrossOrigin(origins="http://localhost:8080")
public class CommentController {

    @Autowired private CommentService commentService;
    @Autowired private PostService postService;
    @Autowired private JwtService jwtService;

    /** GET  /posts/{postId}/comments */
    @GetMapping
    public ResponseEntity<List<Comment>> list(
            @PathVariable int postId,
            @RequestHeader("Authorization") String authHeader
    ) {
        // 1) pull userId from the JWT
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String token = authHeader.substring(7);
        Claims claims;
        try {
            claims = jwtService.verifyToken(token);
        } catch (JwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Integer currentUserId = claims.get("userId", Integer.class);

        // 2) load the post (to find its owner)
        Post post = postService.findById(postId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        // 3) fetch all comments for that post
        List<Comment> allComments = commentService.findByPostId(postId);

        // 4) if you’re the post’s owner, return everything;
        //    otherwise filter to only your own comments
        List<Comment> result;
        if (post.getUser().getId() == currentUserId) {
            result = allComments;
        } else {
            result = allComments.stream()
                    .filter(c -> c.getUser().getId() == currentUserId)
                    .toList();
        }
        return ResponseEntity.ok(result);
    }

    /**
     * POST /posts/{postId}/comments
     * Body: { "userId": 123, "content": "Nice post!" }
     */
    @PostMapping
    public ResponseEntity<Comment> create(
            @PathVariable int postId,
            @RequestBody Map<String,String> body
    ) {
        int userId = Integer.parseInt(body.get("userId"));
        String content = body.get("content");
        Comment created = commentService.create(postId, userId, content);
        return ResponseEntity.status(201).body(created);
    }

    /** DELETE /posts/{postId}/comments/{commentId} */
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> delete(
            @PathVariable int postId,
            @PathVariable int commentId
    ) {
        // optionally verify that comment belongs to postId
        commentService.deleteById(commentId);
        return ResponseEntity.noContent().build();
    }
}