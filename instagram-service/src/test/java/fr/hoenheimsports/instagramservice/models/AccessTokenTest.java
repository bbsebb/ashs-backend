package fr.hoenheimsports.instagramservice.models;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.*;

class AccessTokenTest {
    @Test
    void accessTokenCreationWithExpiresInConstructor() {
        AccessToken token = AccessToken.builder().accessToken("access_token").expireIn(3600).build();


        assertEquals("access_token", token.getAccessToken());
        assertEquals("Bearer", token.getTokenType());
        assertFalse(token.isExpired());
    }

    @Test
    void accessTokenCreationWithDateTimeConstructor() {
        LocalDateTime createdAt = LocalDateTime.now().minusHours(1);
        LocalDateTime expiresAt = LocalDateTime.now().plusHours(1);
        AccessToken token = AccessToken.builder().accessToken("access_token").createdAt(createdAt).expiresAt(expiresAt).build();

        assertEquals("access_token", token.getAccessToken());
        assertEquals("Bearer", token.getTokenType());
        assertFalse(token.isExpired());
    }

    @Test
    void tokenShouldBeExpired() {
        // Création d'un token qui expire dans le passé
        AccessToken token = AccessToken.builder().accessToken("expired_token").createdAt(LocalDateTime.now().minusHours(2)).expiresAt(LocalDateTime.now().minusHours(1)).tokenType("Bearer").build();
        assertTrue(token.isExpired());
    }

    @Test
    void tokenShouldNotBeExpired() {
        // Création d'un token qui expire dans le futur
        AccessToken token = AccessToken.builder().accessToken("valid_token").createdAt(LocalDateTime.now().plusHours(1)).expiresAt(LocalDateTime.now().plusHours(2)).tokenType("Bearer").build();
        assertFalse(token.isExpired());
    }

    @Test
    void accessTokenFieldsShouldMatchConstructorArguments() {
        LocalDateTime now = LocalDateTime.now();
        AccessToken token = AccessToken.builder().accessToken("access_token").createdAt(now).expiresAt(now.plusHours(2)).tokenType("Bearer").build();

        assertEquals("access_token", token.getAccessToken());
        assertEquals("Bearer", token.getTokenType());
        assertTrue(now.isEqual(token.getCreatedAt()) || now.isBefore(token.getCreatedAt()));
        assertTrue(now.plusHours(2).isEqual(token.getExpiresAt()) || now.plusHours(2).isAfter(token.getExpiresAt()));
    }

    @Test
    void accessTokenExpiresAtIsCorrectWithExpiresInConstructor() {
        int expiresIn = 3600; // 1 hour
        LocalDateTime beforeCreation = LocalDateTime.now();
        AccessToken token = AccessToken.builder().accessToken("access_token").expireIn(expiresIn).build();
        LocalDateTime afterCreation = LocalDateTime.now();

        // The token's expiration time should be within the expected range
        assertTrue(token.getExpiresAt().isAfter(beforeCreation.plusSeconds(expiresIn)) || token.getExpiresAt().isEqual(beforeCreation.plusSeconds(expiresIn)));
        assertTrue(token.getExpiresAt().isBefore(afterCreation.plusSeconds(expiresIn)) || token.getExpiresAt().isEqual(afterCreation.plusSeconds(expiresIn)));
    }

    @Test
    void createdAtAndExpiresAtDifferenceMatchesExpiresIn() {
        int expiresIn = 1800; // 30 minutes
        AccessToken token = AccessToken.builder().accessToken("access_token").expireIn(expiresIn).tokenType("Bearer").build();
        long actualSecondsBetween = ChronoUnit.SECONDS.between(token.getCreatedAt(), token.getExpiresAt());

        assertEquals(expiresIn, actualSecondsBetween);
    }

    @Test
    void tokenWithZeroSecondExpiresInShouldBeExpiredImmediately() {
        AccessToken token = AccessToken.builder().accessToken("immediate_expire").expireIn(0).tokenType("Bearer").build();
        assertTrue(token.isExpired());
    }

    @Test
    void tokenWithNegativeExpiresInShouldBeExpired() {
        AccessToken token = AccessToken.builder().accessToken("negative_expire").expireIn(-1).tokenType("Bearer").build();
        assertTrue(token.isExpired());
    }

    @Test
    void checkTokenTypeIsNotNullOrEmpty() {
        AccessToken token = AccessToken.builder().accessToken("valid_token").expireIn(3600).build();
        assertNotNull(token.getTokenType());
        assertFalse(token.getTokenType().isEmpty());
    }

    @Test
    void createdAtIsBeforeExpiresAt() {
        AccessToken token = AccessToken.builder().accessToken("valid_token").expireIn(3600).tokenType("Bearer").build();
        assertTrue(token.getCreatedAt().isBefore(token.getExpiresAt()));
    }

    @Test
    void expiresAtAccuracyToTheSecond() {
        int expiresIn = 7200; // 2 hours
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        AccessToken token = AccessToken.builder().accessToken("access_token").expireIn(expiresIn).tokenType("Bearer").build();

        LocalDateTime expectedExpiresAt = now.plusSeconds(expiresIn).truncatedTo(ChronoUnit.SECONDS);
        LocalDateTime actualExpiresAt = token.getExpiresAt().truncatedTo(ChronoUnit.SECONDS);

        assertEquals(expectedExpiresAt, actualExpiresAt);
    }

    @Test
    void tokenExpiresAtMidnight() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime endOfDay = now.withHour(23).withMinute(59).withSecond(59);
        long secondsUntilEndOfDay = ChronoUnit.SECONDS.between(now, endOfDay);

        AccessToken token = AccessToken.builder().accessToken("end_of_day").expireIn((int) secondsUntilEndOfDay).tokenType("Bearer").build();
        assertFalse(token.isExpired());
        assertTrue(token.getExpiresAt().isBefore(LocalDateTime.now().plusDays(1)));
    }

    @Test
    void checkForExactTokenType() {
        AccessToken token = AccessToken.builder().accessToken("valid_token").expireIn(3600).tokenType("Bearer").build();
        assertEquals("Bearer", token.getTokenType());
    }


    @Test
    void tokenStringRepresentationIsCorrect() {
        AccessToken token = AccessToken.builder().accessToken("valid_token").createdAt(LocalDateTime.now()).expiresAt(LocalDateTime.now().plusHours(1)).tokenType("Bearer").build();
        String expected = "AccessToken{accessToken='valid_token', createdAt=" + token.getCreatedAt() + ", expiresAt=" + token.getExpiresAt() + ", tokenType='Bearer'}";
        assertEquals(expected, token.toString());
    }

    @Test
    void tokensWithSameValuesShouldBeEqual() {
        AccessToken token1 = AccessToken.builder().accessToken("token").createdAt(LocalDateTime.now()).expiresAt(LocalDateTime.now().plusHours(1)).tokenType("Bearer").build();
        AccessToken token2 = AccessToken.builder().accessToken("token").createdAt(token1.getCreatedAt()).expiresAt(token1.getExpiresAt()).tokenType("Bearer").build();
        assertEquals(token1, token2);
        assertEquals(token1.hashCode(), token2.hashCode());
    }

    @Test
    void tokensWithDifferentValuesShouldNotBeEqual() {
        AccessToken token1 = AccessToken.builder().accessToken("token1").createdAt(LocalDateTime.now()).expiresAt(LocalDateTime.now().plusHours(1)).tokenType("Bearer").build();
        AccessToken token2 = AccessToken.builder().accessToken("token2").createdAt(LocalDateTime.now()).expiresAt(LocalDateTime.now().plusHours(2)).tokenType("Bearer").build();
        assertNotEquals(token1, token2);
        // hashCode() test is not strictly necessary here since unequal objects can have the same hash code
    }

}