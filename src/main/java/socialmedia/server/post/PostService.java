package socialmedia.server.post;

import aj.org.objectweb.asm.commons.Remapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Transactional
    public Post create(Post myPost) {
        return postRepository.save(myPost);
    }

    public List<Post> findAllPosts(){
        return postRepository.findAll();
    }

    @Transactional
    public void deleteById(int id) { postRepository.deleteById(id); }

    public Post updatePost(int id, Post updatedPost) {
        Post existingPost = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found with id: " + id));

        // Update the existing post's fields
        existingPost.setTitle(updatedPost.getTitle());
        existingPost.setContent(updatedPost.getContent());
        existingPost.setStatus(updatedPost.getStatus());

        return postRepository.save(existingPost);
    }

    public Optional<Post> findById(int id) {
        return postRepository.findById(id);
    }
}
