package socialmedia.server.comment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import socialmedia.server.post.PostRepository;
import socialmedia.server.user.UserRepository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import static com.fasterxml.jackson.databind.type.LogicalType.DateTime;

@Service
public class CommentService {

    @Autowired private CommentRepository commentRepo;
    @Autowired private PostRepository postRepo;
    @Autowired private UserRepository userRepo;

    /** List all comments for a given post */
    public List<Comment> findByPostId(int postId) {
        return commentRepo.findByPostId(postId);
    }

    /**
     * Create and save a new comment on postId by userId with given content.
     * Throws if post or user not found.
     */
    public Comment create(int postId, int userId, String content) {
        var postOpt = postRepo.findById(postId);
        var userOpt = userRepo.findById(userId);
        if (postOpt.isEmpty() || userOpt.isEmpty()) {
            throw new RuntimeException("Post or User not found");
        }

        Comment c = new Comment();
        c.setPost(postOpt.get());
        c.setUser(userOpt.get());
        c.setContent(content);
        c.setCreateOn(new Date());

        return commentRepo.save(c);
    }

    // delete a comment by its ID */
    public void deleteById(int commentId) {
        commentRepo.deleteById(commentId);
    }
}
