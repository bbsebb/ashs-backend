package fr.hoenheimsports.trainingservice.dto;

import java.io.Serializable;

/**
 * DTO for {@link fr.hoenheimsports.trainingservice.models.Address}
 */
public record AddressDto(String street, String city, String postalCode, String country) implements Serializable {
}