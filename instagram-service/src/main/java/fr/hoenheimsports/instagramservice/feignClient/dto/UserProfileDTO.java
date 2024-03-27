package fr.hoenheimsports.instagramservice.feignClient.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

/**
 * Data Transfer Object for the user profile
 * <p>
 * The {@code UserProfileDTO} includes the following user information:
 * <ul>
 *     <li>{@code accountType} - A string representing the type of the user account
 *     (e.g., personal, admin).</li>
 *     <li>{@code id} - A unique identifier for the user in the system.</li>
 *     <li>{@code mediaCount} - The number of media items associated with the user's account.</li>
 *     <li>{@code username} - The user's chosen username, which is unique within the application.</li>
 * </ul>
 *
 * @param accountType the type of user account
 * @param id the unique identifier for the user
 * @param mediaCount the number of media items associated with the user
 * @param username the user's username
 */
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record UserProfileDTO(String accountType, String id, int mediaCount, String username) {


    public static Builder builder() {
        return new Builder();
    }
    /**
     * {@code UserProfileDTO} builder static inner class.
     */
    public static final class Builder {
        private String accountType;
        private String id;
        private int mediaCount;
        private String username;

        private Builder() {
        }



        /**
         * Sets the {@code accountType} and returns a reference to this Builder enabling method chaining.
         *
         * @param accountType the {@code accountType} to set
         * @return a reference to this Builder
         */
        public Builder accountType(String accountType) {
            this.accountType = accountType;
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
         * Sets the {@code mediaCount} and returns a reference to this Builder enabling method chaining.
         *
         * @param mediaCount the {@code mediaCount} to set
         * @return a reference to this Builder
         */
        public Builder mediaCount(int mediaCount) {
            this.mediaCount = mediaCount;
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
         * Returns a {@code UserProfileDTO} built from the parameters previously set.
         *
         * @return a {@code UserProfileDTO} built with parameters of this {@code UserProfileDTO.Builder}
         */
        public UserProfileDTO build() {
            return new UserProfileDTO(accountType, id, mediaCount, username);
        }
    }
}
