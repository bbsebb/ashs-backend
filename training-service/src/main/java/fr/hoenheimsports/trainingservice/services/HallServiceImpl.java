package fr.hoenheimsports.trainingservice.services;

import fr.hoenheimsports.trainingservice.Exception.HallNotFoundException;
import fr.hoenheimsports.trainingservice.assemblers.HallAssembler;
import fr.hoenheimsports.trainingservice.dto.HallDTO;
import fr.hoenheimsports.trainingservice.dto.request.HallDTORequest;
import fr.hoenheimsports.trainingservice.mappers.HallMapper;
import fr.hoenheimsports.trainingservice.models.Hall;
import fr.hoenheimsports.trainingservice.repositories.HallRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;

@Service
public class HallServiceImpl implements HallService {
    private final HallRepository hallRepository;
    private final HallAssembler hallAssembler;
    private final HallMapper hallMapper;

    public HallServiceImpl(HallRepository hallRepository, HallAssembler hallAssembler, HallMapper hallMapper) {
        this.hallRepository = hallRepository;
        this.hallAssembler = hallAssembler;
        this.hallMapper = hallMapper;
    }

    @Override
    public HallDTO createAndConvertToModel(HallDTORequest hallDtoRequest) {
        return this.hallAssembler.toModel(this.create(hallDtoRequest));
    }

    private Hall create(HallDTORequest hallDtoRequest) {
        return hallRepository.save(hallMapper.toEntity(hallDtoRequest));
    }

    @Override
    public Hall findOrCreateOrUpdate(HallDTORequest hallDtoRequest) {
        if (hallDtoRequest == null) {
            throw new HallNotFoundException();
        }
        Hall hall;
        if (hallDtoRequest.id() != null) {
            hall = this.update(hallDtoRequest.id(), hallDtoRequest);
        } else {
            hall = this.create(hallDtoRequest);
        }

        return hall;
    }

    @Override
    public PagedModel<HallDTO> getAllModels(Pageable pageable) {
        return hallAssembler.toPagedModel(hallRepository.findAll(pageable));
    }

    @Override
    public HallDTO getModelById(Long id) {

        return hallRepository.findById(id)
                .map(hallAssembler::toModel).orElseThrow(HallNotFoundException::new);
    }

    @Override
    public HallDTO updateAndConvertToModel(Long id, HallDTORequest hallDtoRequest) {
        return hallAssembler.toModel(this.update(id, hallDtoRequest));
    }

    private Hall update(Long id, HallDTORequest hallDtoRequest) {
        Hall existingHall = hallRepository.findById(id)
                .orElseThrow(HallNotFoundException::new);
        Hall updatedHall = hallMapper.partialUpdate(hallDtoRequest, existingHall);
        updatedHall = hallRepository.save(updatedHall);
        return updatedHall;
    }

    @Override
    public void deleteById(Long id) {
        hallRepository.deleteById(id);
    }
}