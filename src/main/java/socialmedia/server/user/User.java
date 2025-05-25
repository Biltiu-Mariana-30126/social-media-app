package socialmedia.server.user;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "user", schema = "socialmedia", catalog = "")
public class User {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;
    @Basic
    @Column(name = "email")
    private String email;
    @Basic
    @Column(name = "name")
    private String name;

    /** Whether the user has completed 2FA setup and must provide a TOTP code at login */
    @Basic
    @Column(name = "using_2fa", nullable = false)
    private boolean using2FA = false;

    /** Base32‐encoded TOTP secret for Google Authenticator (or similar) */
    @Basic
    @Column(name = "secret_2fa", length = 255)
    private String secret2FA;

    @Basic
    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    public String getPasswordHash() {
        return passwordHash;
    }
    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isUsing2FA() { return using2FA; }
    public void setUsing2FA(boolean using2FA) { this.using2FA = using2FA; }

    public String getSecret2FA() { return secret2FA; }
    public void setSecret2FA(String secret2FA) { this.secret2FA = secret2FA; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User that = (User) o;
        return id == that.id
                && using2FA == that.using2FA
                && Objects.equals(email, that.email)
                && Objects.equals(name, that.name)
                && Objects.equals(secret2FA, that.secret2FA);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, name, using2FA, secret2FA);
    }
}
