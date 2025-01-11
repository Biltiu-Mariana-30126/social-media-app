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
    @JoinC olumn(name="user_id", nullable = true)
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Post that = (Post) o;
        return id == that.id && Objects.equals(content, that.content) && Objects.equals(createdOn, that.createdOn) && Objects.equals(status, that.status) && Objects.equals(title, that.title) && Objects.equals(userId, that.userId);
    }
}
