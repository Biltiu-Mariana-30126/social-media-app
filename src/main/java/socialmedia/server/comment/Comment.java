package socialmedia.server.comment;

import jakarta.persistence.*;
import socialmedia.server.post.Post;
import socialmedia.server.user.User;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "comment", schema = "socialmedia", catalog = "")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idcomment")
    private int idcomment;

    @Basic
    @Column(name = "content", nullable = false)
    private String content;

    // Establish a Many-to-One relationship with Post
    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    // Establish a Many-to-One relationship with User
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Basic
    @Column(name = "create_on", nullable = false)
    private Date createOn;

    // Getters and Setters
    public int getIdcomment() {
        return idcomment;
    }

    public void setIdcomment(int idcomment) {
        this.idcomment = idcomment;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getCreateOn() {
        return createOn;
    }

    public void setCreateOn(Date createOn) {
        this.createOn = createOn;
    }

    // Override equals and hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comment comment = (Comment) o;
        return idcomment == comment.idcomment &&
                Objects.equals(content, comment.content) &&
                Objects.equals(post, comment.post) &&
                Objects.equals(user, comment.user) &&
                Objects.equals(createOn, comment.createOn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idcomment, content, post, user, createOn);
    }
}
