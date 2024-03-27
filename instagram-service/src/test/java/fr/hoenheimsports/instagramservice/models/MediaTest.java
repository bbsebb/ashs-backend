package fr.hoenheimsports.instagramservice.models;

import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.URL;
import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.*;

class MediaTest {
    @Test
    void testMediaObjectConstruction() throws Exception {
        String id = "testId";
        MediaType mediaType = MediaType.IMAGE;
        URL mediaUrl = URI.create("http://example.com/media.jpg").toURL();
        String username = "testUser";
        String caption = "Test Caption";
        OffsetDateTime timestamp = OffsetDateTime.now();
        URL permalink = URI.create("http://example.com/permalink").toURL();
        URL thumbnailUrl = URI.create("http://example.com/thumbnail.jpg").toURL();

        Media media = Media.builder(id)
                .mediaType(mediaType)
                .mediaUrl(mediaUrl)
                .username(username)
                .caption(caption)
                .timestamp(timestamp)
                .permalink(permalink)
                .thumbnailUrl(thumbnailUrl)
                .build();

        assertEquals(id, media.getId());
        assertEquals(mediaType, media.getMediaType());
        assertEquals(mediaUrl, media.getMediaUrl());
        assertEquals(username, media.getUsername());
        assertEquals(caption, media.getCaption());
        assertEquals(timestamp, media.getTimestamp());
        assertEquals(permalink, media.getPermalink());
        assertEquals(thumbnailUrl, media.getThumbnailUrl());
    }

    @Test
    void testEqualityAndHashcode() {
        String id = "testId";
        Media media1 = Media.builder(id).build();
        Media media2 = Media.builder(id).build();
        Media media3 = Media.builder("differentId").build();

        // Equality tests
        assertEquals(media1, media2, "Media objects with the same id should be equal");
        assertNotEquals(media1, media3, "Media objects with different ids should not be equal");

        // Hashcode tests
        assertEquals(media1.hashCode(), media2.hashCode(), "Hashcodes should be equal for equal Media objects");
        assertNotEquals(media1.hashCode(), media3.hashCode(), "Hashcodes should not be equal for non-equal Media objects");

        // Equality with null
        assertNotNull(media1, "Media object should not be equal to null");
    }
}