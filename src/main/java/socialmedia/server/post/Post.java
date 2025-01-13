package socialmedia.server.post;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import socialmedia.server.user.User;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "post", schema = "socialmedia", catalog = "")
public class Post {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;
    @Basic
    @Column(name = "content")
    private String content;
    @CreationTimestamp
    @Column(updatable = false, nullable = false)
    private Date createdOn;
    @Basic
    @Column(name = "status")
    private Status status;
    @Basic
    @Column(name = "title")
    private String title;

    @ManyToOne
    @JoinColumn(name="user_id", nullable = true)
    private User user;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public User getUser() { return user; }

    public void setUser(User user) { this.user = user; }
}
