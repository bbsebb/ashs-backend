package fr.hoenheimsports.instagramservice.models;

import com.google.cloud.firestore.annotation.DocumentId;
import com.google.cloud.spring.data.firestore.Document;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Represents an access token.
 */
@Document(collectionName = "accessTokens")
public class AccessToken {
    @Id
    private String id = "singletonToken";
    private String accessToken;
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;
    private String tokenType;

    /**
     * No-args constructor.
     */
    public AccessToken() {}

    /**
     * All-args constructor.
     */
    public AccessToken(String accessToken, LocalDateTime createdAt, LocalDateTime expiresAt, String tokenType) {
        this.accessToken = accessToken;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
        this.tokenType = tokenType;
    }

    private AccessToken(Builder builder) {
        this.accessToken = builder.accessToken;
        this.createdAt = builder.createdAt;
        this.expiresAt = builder.expiresAt;
        this.tokenType = builder.tokenType;
    }




    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    // hashCode and equals
    @Override
    public int hashCode() {
        return Objects.hash(id, accessToken, createdAt, expiresAt, tokenType);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        AccessToken that = (AccessToken) obj;
        return Objects.equals(id, that.id) &&
                Objects.equals(accessToken, that.accessToken) &&
                Objects.equals(createdAt, that.createdAt) &&
                Objects.equals(expiresAt, that.expiresAt) &&
                Objects.equals(tokenType, that.tokenType);
    }

    // toString
    @Override
    public String toString() {
        return "AccessToken{" +
                "id='" + id + '\'' +
                ", accessToken='" + accessToken + '\'' +
                ", createdAt=" + createdAt +
                ", expiresAt=" + expiresAt +
                ", tokenType='" + tokenType + '\'' +
                '}';
    }

    // Builder
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String accessToken;
        private LocalDateTime createdAt;
        private LocalDateTime expiresAt;
        private String tokenType;

        public Builder accessToken(String accessToken) {
            this.accessToken = accessToken;
            return this;
        }

        public Builder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder expiresAt(LocalDateTime expiresAt) {
            this.expiresAt = expiresAt;
            return this;
        }

        public Builder tokenType(String tokenType) {
            this.tokenType = tokenType;
            return this;
        }

        public AccessToken build() {
            return new AccessToken(this);
        }
    }
}