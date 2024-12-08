package fr.hoenheimsports.trainingservice.dto;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;

@Data
@EqualsAndHashCode(callSuper = false)
@RequiredArgsConstructor
public class AddressDTO extends RepresentationModel<AddressDTO> {
    private final String street;
    private final String city;
    private final String postalCode;
    private final String country;
}
