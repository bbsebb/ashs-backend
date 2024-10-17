package fr.hoenheimsports.trainingservice.processors;

import fr.hoenheimsports.trainingservice.dto.CoachDto;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.RepresentationModelProcessor;
import org.springframework.hateoas.server.core.EmbeddedWrapper;
import org.springframework.hateoas.server.core.EmbeddedWrappers;
import org.springframework.lang.NonNull;

import java.util.Collections;
import java.util.List;

//@Component
public class CoachPagedModelProcessor implements RepresentationModelProcessor<PagedModel<?>> {
    @Override
    @NonNull
    public PagedModel<?> process(@NonNull PagedModel<?> model) {

        if (model.getContent().isEmpty()) {
            EmbeddedWrappers wrappers = new EmbeddedWrappers(false);
            EmbeddedWrapper wrapper = wrappers.emptyCollectionOf(CoachDto.class);
            List<EmbeddedWrapper> embedded = Collections.singletonList(wrapper);
            model = PagedModel.of(embedded, model.getMetadata(),model.getLinks());
        }

        return model;
    }

}
