package fr.hoenheimsports.instagramservice.services;

import fr.hoenheimsports.instagramservice.feignClient.GraphInstagram;
import fr.hoenheimsports.instagramservice.feignClient.dto.MediaDTO;
import fr.hoenheimsports.instagramservice.feignClient.dto.UserMediasDTO;
import fr.hoenheimsports.instagramservice.models.Media;
import fr.hoenheimsports.instagramservice.models.MediaType;
import fr.hoenheimsports.instagramservice.repositories.AccessTokenRepository;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class GraphInstagramServiceimpl implements GraphInstagramService{


    private final GraphInstagram graphInstagram;
    private final AccessTokenRepository accessTokenRepository;

    public GraphInstagramServiceimpl(GraphInstagram graphInstagram, AccessTokenRepository accessTokenRepository) {
        this.graphInstagram = graphInstagram;
        this.accessTokenRepository = accessTokenRepository;
    }

    @Override
    public List<Media> getMedias() {
        UserMediasDTO userMediasDTO = this.graphInstagram.getAllMediaByUser(this.getId(), "id,caption,media_type,media_url,permalink,thumbnail_url,timestamp,username", this.getToken());
        return userMediasDTO.data().stream().map(this::mapMediaDTOToMedia).toList();
    }

    private String getId() {
        return this.graphInstagram.getMe("id,username,account_type,media_count", this.getToken()).id();
    }

    private String getToken() {
        return this.accessTokenRepository.get().getAccessToken();
    }

    private Media mapMediaDTOToMedia(MediaDTO mediaDTO) {
        List<Media> children = null;
        if (mediaDTO.mediaType() == fr.hoenheimsports.instagramservice.feignClient.dto.MediaType.CAROUSEL_ALBUM) {
            List<MediaDTO> childrenDTO = this.graphInstagram.getChildrenMediaByMediaId(mediaDTO.id(), "id,media_type,media_url,permalink,thumbnail_url,timestamp,username", this.getToken()).data();
            children = childrenDTO.stream().map(this::mapMediaDTOToMedia).toList();
        }
        return  Media.builder(mediaDTO.id())

                .caption(mediaDTO.caption())
                .mediaUrl(mediaDTO.mediaUrl())
                .permalink(mediaDTO.permalink())
                .thumbnailUrl(mediaDTO.thumbnailUrl())
                .timestamp(mediaDTO.timestamp())
                .username(mediaDTO.username())
                .mediaType(mapMediaType(mediaDTO.mediaType()))
                .children(children)
                .build();
    }

    private MediaType mapMediaType(fr.hoenheimsports.instagramservice.feignClient.dto.MediaType mediaType) {
        return switch (mediaType) {
            case IMAGE -> MediaType.IMAGE;
            case VIDEO -> MediaType.VIDEO;
            case CAROUSEL_ALBUM -> MediaType.CAROUSEL_ALBUM;
        };
    }
}
