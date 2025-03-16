package kg.nurtelecom.coffee_sale.entity;

import jakarta.persistence.*;
@Entity
@Table(name = "authorities", uniqueConstraints = @UniqueConstraint(columnNames = {"username", "authority"}))
public class Authority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "username", nullable = false)
    private UserEntity user;

    @Column(name = "authority", length = 50, nullable = false)
    private String authority;

    public Authority(UserEntity user, String authority) {
        this.user = user;
        this.authority = authority;
    }
    public Authority() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }
}