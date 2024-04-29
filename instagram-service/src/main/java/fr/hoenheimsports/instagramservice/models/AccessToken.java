package fr.hoenheimsports.instagramservice.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Represents an access token.
 */
@Entity
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


    private AccessToken(Builder builder) {
        this.accessToken = builder.accessToken;
        this.createdAt = builder.createdAt;
        this.expiresAt = builder.expiresAt;
        this.tokenType = builder.tokenType;
    }






    public void setId(String id) {
        this.id = id;
    }

    public String getAccessToken() {
        return accessToken;
    }


    public LocalDateTime getCreatedAt() {
        return createdAt;
    }


    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }


    public String getTokenType() {
        return tokenType;
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