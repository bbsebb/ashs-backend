package fr.hoenheimsports.instagramservice.feignClient.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.net.URL;
import java.time.OffsetDateTime;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)

public record MediaDTO(
        String caption,
        String id,
        boolean isSharedToFeed,
        MediaType mediaType,
        URL mediaUrl,
        URL permalink,
        URL thumbnailUrl,
        OffsetDateTime timestamp,
        String username

) {

    public static Builder builder() {
        return new Builder();
    }

    /**
     * {@code MediaDTO} builder static inner class.
     */
    public static final class Builder {
        private String caption;
        private String id;
        private boolean isSharedToFeed;
        private MediaType mediaType;
        private URL mediaUrl;
        private URL permalink;
        private URL thumbnailUrl;
        private OffsetDateTime timestamp;
        private String username;

        private Builder() {
        }



        /**
         * Sets the {@code caption} and returns a reference to this Builder enabling method chaining.
         *
         * @param caption the {@code caption} to set
         * @return a reference to this Builder
         */
        public Builder caption(String caption) {
            this.caption = caption;
            return this;
        }

        /**
         * Sets the {@code id} and returns a reference to this Builder enabling method chaining.
         *
         * @param id the {@code id} to set
         * @return a reference to this Builder
         */
        public Builder id(String id) {
            this.id = id;
            return this;
        }

        /**
         * Sets the {@code isSharedToFeed} and returns a reference to this Builder enabling method chaining.
         *
         * @param isSharedToFeed the {@code isSharedToFeed} to set
         * @return a reference to this Builder
         */
        public Builder isSharedToFeed(boolean isSharedToFeed) {
            this.isSharedToFeed = isSharedToFeed;
            return this;
        }

        /**
         * Sets the {@code mediaType} and returns a reference to this Builder enabling method chaining.
         *
         * @param mediaType the {@code mediaType} to set
         * @return a reference to this Builder
         */
        public Builder mediaType(MediaType mediaType) {
            this.mediaType = mediaType;
            return this;
        }

        /**
         * Sets the {@code mediaUrl} and returns a reference to this Builder enabling method chaining.
         *
         * @param mediaUrl the {@code mediaUrl} to set
         * @return a reference to this Builder
         */
        public Builder mediaUrl(URL mediaUrl) {
            this.mediaUrl = mediaUrl;
            return this;
        }

        /**
         * Sets the {@code permalink} and returns a reference to this Builder enabling method chaining.
         *
         * @param permalink the {@code permalink} to set
         * @return a reference to this Builder
         */
        public Builder permalink(URL permalink) {
            this.permalink = permalink;
            return this;
        }

        /**
         * Sets the {@code thumbnailUrl} and returns a reference to this Builder enabling method chaining.
         *
         * @param thumbnailUrl the {@code thumbnailUrl} to set
         * @return a reference to this Builder
         */
        public Builder thumbnailUrl(URL thumbnailUrl) {
            this.thumbnailUrl = thumbnailUrl;
            return this;
        }

        /**
         * Sets the {@code timestamp} and returns a reference to this Builder enabling method chaining.
         *
         * @param timestamp the {@code timestamp} to set
         * @return a reference to this Builder
         */
        public Builder timestamp(OffsetDateTime timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        /**
         * Sets the {@code username} and returns a reference to this Builder enabling method chaining.
         *
         * @param username the {@code username} to set
         * @return a reference to this Builder
         */
        public Builder username(String username) {
            this.username = username;
            return this;
        }

        /**
         * Returns a {@code MediaDTO} built from the parameters previously set.
         *
         * @return a {@code MediaDTO} built with parameters of this {@code MediaDTO.Builder}
         */
        public MediaDTO build() {
            return new MediaDTO(caption, id, isSharedToFeed, mediaType, mediaUrl, permalink, thumbnailUrl, timestamp, username);
        }
    }
}