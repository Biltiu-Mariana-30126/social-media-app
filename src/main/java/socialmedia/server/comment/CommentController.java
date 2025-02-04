package socialmedia.server.comment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comments")
@CrossOrigin
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping
    public Comment create(@RequestBody Comment myComment)
    {
        return commentService.create(myComment);
    }

    @GetMapping
    public List<Comment> getAllComments(){
        return commentService.findAllComments();
    }

    @PutMapping("/{id}")
    public Comment updateComment(@PathVariable int id, @RequestBody Comment updatedComment) {
        return commentService.updateComment(id, updatedComment);
    }

    @DeleteMapping("/{id}")
    public void deleteComment(@PathVariable int id) {
        commentService.deleteById(id);
    }

}
