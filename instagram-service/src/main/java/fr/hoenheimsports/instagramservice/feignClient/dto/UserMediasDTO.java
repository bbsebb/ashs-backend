package fr.hoenheimsports.instagramservice.feignClient.dto;

import java.util.List;

public record UserMediasDTO(List<MediaDTO> data, PagingDTO paging) {
}
