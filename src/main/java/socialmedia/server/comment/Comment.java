package socialmedia.server.comment;

import jakarta.persistence.*;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "comment", schema = "socialmedia", catalog = "")
public class Comment {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "idcomment")
    private int idcomment;
    @Basic
    @Column(name = "content")
    private String content;
    @Basic
    @Column(name = "post_id")
    private Integer postId;
    @Basic
    @Column(name = "user_id")
    private Integer userId;
    @Basic
    @Column(name = "create_on")
    private Date createOn;

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

    public Integer getPostId() {
        return postId;
    }

    public void setPostId(Integer postId) {
        this.postId = postId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Date getCreateOn() {
        return createOn;
    }

    public void setCreateOn(Date createOn) {
        this.createOn = createOn;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comment that = (Comment) o;
        return idcomment == that.idcomment && Objects.equals(content, that.content) && Objects.equals(postId, that.postId) && Objects.equals(userId, that.userId) && Objects.equals(createOn, that.createOn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idcomment, content, postId, userId, createOn);
    }
}
