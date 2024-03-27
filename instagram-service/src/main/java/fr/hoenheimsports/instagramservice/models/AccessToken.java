package fr.hoenheimsports.instagramservice.models;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;


public class AccessToken implements Serializable{
    @Serial
    private static final long serialVersionUID = -7674245394478912197L;
    private final String accessToken;
    private final LocalDateTime createdAt;
    private final LocalDateTime expiresAt;
    private final String tokenType;

    private AccessToken(Builder builder) {
        this.tokenType = (builder.tokenType==null || builder.tokenType.isBlank())?"Bearer":builder.tokenType;
        if(builder.createdAt == null || builder.expiresAt == null) {
            this.createdAt = LocalDateTime.now();
            this.expiresAt = this.createdAt.plusSeconds(builder.expireIn);
        } else {
            this.createdAt = builder.createdAt;
            this.expiresAt = builder.expiresAt;
        }
        accessToken = builder.accessToken;
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

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiresAt) || LocalDateTime.now().isEqual(expiresAt);
    }

    @Override
    public String toString() {
        return "AccessToken{" +
                "accessToken='" + accessToken + '\'' +
                ", createdAt=" + createdAt +
                ", expiresAt=" + expiresAt +
                ", tokenType='" + tokenType + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccessToken that = (AccessToken) o;
        return Objects.equals(accessToken, that.accessToken) && Objects.equals(createdAt, that.createdAt) && Objects.equals(expiresAt, that.expiresAt) && Objects.equals(tokenType, that.tokenType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accessToken, createdAt, expiresAt, tokenType);
    }

    public static Builder builder() {
        return new Builder();
    }
    /**
     * {@code AccessToken} builder static inner class.
     */
    public static final class Builder {
        private String accessToken;
        private LocalDateTime createdAt;
        private LocalDateTime expiresAt;
        private String tokenType;
        private long expireIn;

        private Builder() {
        }



        /**
         * Sets the {@code accessToken} and returns a reference to this Builder enabling method chaining.
         *
         * @param accessToken the {@code accessToken} to set
         * @return a reference to this Builder
         */
        public Builder accessToken(String accessToken) {
            this.accessToken = accessToken;
            return this;
        }

        /**
         * Sets the {@code createdAt} and returns a reference to this Builder enabling method chaining.
         *
         * @param createdAt the {@code createdAt} to set
         * @return a reference to this Builder
         */
        public Builder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        /**
         * Sets the {@code expiresAt} and returns a reference to this Builder enabling method chaining.
         *
         * @param expiresAt the {@code expiresAt} to set
         * @return a reference to this Builder
         */
        public Builder expiresAt(LocalDateTime expiresAt) {
            this.expiresAt = expiresAt;
            return this;
        }

        /**
         * Sets the {@code expireIn} and returns a reference to this Builder enabling method chaining.
         *
         * @param expireIn the {@code expireIn} to set
         * @return a reference to this Builder
         */
        public Builder expireIn(long expireIn) {
            this.expireIn = expireIn;
            return this;
        }

        /**
         * Sets the {@code tokenType} and returns a reference to this Builder enabling method chaining.
         *
         * @param tokenType the {@code tokenType} to set
         * @return a reference to this Builder
         */
        public Builder tokenType(String tokenType) {
            this.tokenType = tokenType;
            return this;
        }

        /**
         * Returns a {@code AccessToken} built from the parameters previously set.
         *
         * @return a {@code AccessToken} built with parameters of this {@code AccessToken.Builder}
         */
        public AccessToken build() {
            return new AccessToken(this);
        }
    }
}
