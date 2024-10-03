package fr.hoenheimsports.trainingservice.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.hateoas.config.EnableHypermediaSupport;

import static org.springframework.hateoas.config.EnableHypermediaSupport.HypermediaType.HAL;
import static org.springframework.hateoas.config.EnableHypermediaSupport.HypermediaType.HAL_FORMS;

@Configuration
@EnableHypermediaSupport(type = {HAL,HAL_FORMS})
public class HateoasConfig {
}
