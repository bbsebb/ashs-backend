package fr.hoenheimsports.instagramservice.models;

import java.net.URL;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;

public class Media {
    private final String id;
    private final MediaType mediaType;
    private final URL mediaUrl;
    private final String username;
    private final String caption;
    private final OffsetDateTime timestamp;
    private final URL permalink;
    private final URL thumbnailUrl;

    private final List<Media> children;


    private Media(Builder builder) {
        this.id = builder.id;
        this.mediaType = builder.mediaType;
        this.mediaUrl = builder.mediaUrl;
        this.username = builder.username;
        this.caption = builder.caption;
        this.timestamp = builder.timestamp;
        this.permalink = builder.permalink;
        this.thumbnailUrl = builder.thumbnailUrl;
        this.children = builder.children;
    }

    public String getId() {
        return id;
    }

    public MediaType getMediaType() {
        return mediaType;
    }

    public URL getMediaUrl() {
        return mediaUrl;
    }

    public String getUsername() {
        return username;
    }

    public String getCaption() {
        return caption;
    }

    public OffsetDateTime getTimestamp() {
        return timestamp;
    }

    public URL getPermalink() {
        return permalink;
    }

    public URL getThumbnailUrl() {
        return thumbnailUrl;
    }

    public List<Media> getChildren() {
        return children;
    }

    @Override
    public String toString() {
        return "Media{" +
                "id='" + id + '\'' +
                ", mediaType='" + mediaType + '\'' +
                ", mediaUrl='" + mediaUrl + '\'' +
                ", username='" + username + '\'' +
                ", caption='" + caption + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", permalink='" + permalink + '\'' +
                ", thumbnailUrl='" + thumbnailUrl + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Media media)) return false;
        return Objects.equals(id, media.id) &&
                mediaType == media.mediaType &&
                Objects.equals(mediaUrl, media.mediaUrl) &&
                Objects.equals(username, media.username) &&
                Objects.equals(caption, media.caption) &&
                Objects.equals(timestamp, media.timestamp) &&
                Objects.equals(permalink, media.permalink) &&
                Objects.equals(thumbnailUrl, media.thumbnailUrl) &&
                Objects.equals(children, media.children);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, mediaType, mediaUrl, username, caption, timestamp, permalink, thumbnailUrl, children);
    }

    public static Builder builder(String id) {
        return new Builder(id);
    }
    public static class Builder {
        private final String id; // Obligatory
        private MediaType mediaType; // Optional
        private URL mediaUrl; // Optional
        private String username; // Optional
        private String caption; // Optional
        private OffsetDateTime timestamp; // Optional
        private URL permalink; // Optional
        private URL thumbnailUrl; // Optional
        private List<Media> children; // Optional

        public Builder(String id) {
            this.id = id;
        }

        public Builder mediaType(MediaType mediaType) {
            this.mediaType = mediaType;
            return this;
        }

        public Builder mediaUrl(URL mediaUrl) {
            this.mediaUrl = mediaUrl;
            return this;
        }

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder caption(String caption) {
            this.caption = caption;
            return this;
        }

        public Builder timestamp(OffsetDateTime timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public Builder permalink(URL permalink) {
            this.permalink = permalink;
            return this;
        }

        public Builder thumbnailUrl(URL thumbnailUrl) {
            this.thumbnailUrl = thumbnailUrl;
            return this;
        }

        public Builder children(List<Media> children) {
            this.children = children;
            return this;
        }

        public Media build() {
            return new Media(this);
        }
    }

}
