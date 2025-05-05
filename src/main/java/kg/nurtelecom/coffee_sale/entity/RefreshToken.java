package kg.nurtelecom.coffee_sale.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity

public class RefreshToken {
    @Id
    private UUID id;
    private String token;
    private OffsetDateTime expiryDate;
    private String username;

    public RefreshToken() {
    }

    public RefreshToken(UUID id, String token, OffsetDateTime expiryDate, String username) {
        this.id = id;
        this.token = token;
        this.expiryDate = expiryDate;
        this.username = username;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public OffsetDateTime getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(OffsetDateTime expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}

