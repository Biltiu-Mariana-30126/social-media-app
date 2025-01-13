package socialmedia.server.comment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Transactional
    public Comment create(Comment myComment) {
        return commentRepository.save(myComment);
    }

    public List<Comment> findAllComments(){
        return commentRepository.findAll();
    }

    @Transactional
    public void deleteById(int id) { commentRepository.deleteById(id); }

    public Comment updateComment(int id, Comment updatedComment) {
        Comment existingComment = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment not found with id: " + id));

        // Update the existing comment's fields
        existingComment.setContent(updatedComment.getContent());

        return commentRepository.save(existingComment);
    }

}
