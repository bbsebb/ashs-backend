package fr.hoenheimsports.instagramservice.services;

import fr.hoenheimsports.instagramservice.feignClient.GraphInstagram;
import fr.hoenheimsports.instagramservice.feignClient.dto.MediaDTO;
import fr.hoenheimsports.instagramservice.feignClient.dto.UserMediasDTO;
import fr.hoenheimsports.instagramservice.models.Media;
import fr.hoenheimsports.instagramservice.models.MediaType;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class GraphInstagramServiceImpl implements GraphInstagramService{


    private final GraphInstagram graphInstagram;
    private final AuthInstagramService authInstagramService;

    public GraphInstagramServiceImpl(GraphInstagram graphInstagram, AuthInstagramService authInstagramService) {
        this.graphInstagram = graphInstagram;
        this.authInstagramService = authInstagramService;
    }

    @Override
    public List<Media> getMedias() {
        this.authInstagramService.refreshAccessToken(); // refresh token for each request to avoid expiration TODO : remove this line when we have a better solution
        UserMediasDTO userMediasDTO = this.graphInstagram.getAllMediaByUser(this.getId(), "id,caption,media_type,media_url,permalink,thumbnail_url,timestamp,username", this.authInstagramService.retrieveAccessToken().getAccessToken());
        return userMediasDTO.data().stream().map(this::mapMediaDTOToMedia).toList();
    }

    private String getId() {
        return this.graphInstagram.getMe("id,username,account_type,media_count", this.authInstagramService.retrieveAccessToken().getAccessToken()).id();
    }



    private Media mapMediaDTOToMedia(MediaDTO mediaDTO) {
        List<Media> children = null;
        if (mediaDTO.mediaType() == fr.hoenheimsports.instagramservice.feignClient.dto.MediaType.CAROUSEL_ALBUM) {
            List<MediaDTO> childrenDTO = this.graphInstagram.getChildrenMediaByMediaId(mediaDTO.id(), "id,media_type,media_url,permalink,thumbnail_url,timestamp,username", this.authInstagramService.retrieveAccessToken().getAccessToken()).data();
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
