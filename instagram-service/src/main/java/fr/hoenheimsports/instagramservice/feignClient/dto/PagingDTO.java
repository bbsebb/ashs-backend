package fr.hoenheimsports.instagramservice.feignClient.dto;

public record PagingDTO(cursorDTO cursors, String next) {
}
