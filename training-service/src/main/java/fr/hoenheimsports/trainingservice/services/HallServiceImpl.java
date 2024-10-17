package fr.hoenheimsports.trainingservice.services;

import fr.hoenheimsports.trainingservice.Exception.HallNotFoundException;
import fr.hoenheimsports.trainingservice.assemblers.HallModelAssembler;
import fr.hoenheimsports.trainingservice.assemblers.HallPagedModelAssembler;
import fr.hoenheimsports.trainingservice.dto.HallDto;
import fr.hoenheimsports.trainingservice.mappers.HallMapper;
import fr.hoenheimsports.trainingservice.models.Hall;
import fr.hoenheimsports.trainingservice.repositories.HallRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HallServiceImpl implements HallService {
    private final HallRepository hallRepository;
    private final HallModelAssembler hallModelAssembler;
    private final HallPagedModelAssembler hallPagedModelAssembler;
    private final HallMapper hallMapper;
    private final SortUtil sortUtil;

    public HallServiceImpl(HallRepository hallRepository, HallModelAssembler hallModelAssemblerImpl, HallPagedModelAssembler hallPagedModelAssembler, HallMapper hallMapper, SortUtil sortUtil) {
        this.hallRepository = hallRepository;
        this.hallModelAssembler = hallModelAssemblerImpl;
        this.hallPagedModelAssembler = hallPagedModelAssembler;
        this.hallMapper = hallMapper;
        this.sortUtil = sortUtil;
    }

    @Override
    public EntityModel<HallDto> createHall(HallDto hallDto) {
       Hall newHall = this.hallRepository.save(this.hallMapper.toEntity(hallDto));
         return this.hallModelAssembler.toModel(this.hallMapper.toDto(newHall));
    }

    @Override
    public PagedModel<?> getAllHalls(int page, int size, List<String> sort) {
        Pageable pageable = PageRequest.of(page, size, this.sortUtil.createSort(sort));
        return hallPagedModelAssembler.toModel(hallRepository.findAll(pageable).map(hallMapper::toDto).map(hallModelAssembler::toModel));
    }

    @Override
    public EntityModel<HallDto> getHallById(Long id) throws HallNotFoundException {

        return hallRepository.findById(id)
                .map(hallMapper::toDto)
                .map(hallModelAssembler::toModel).orElseThrow(HallNotFoundException::new);
    }

    @Override
    public EntityModel<HallDto> updateHall(Long id, HallDto hallDto) throws HallNotFoundException {
        Hall existingHall = hallRepository.findById(id)
                .orElseThrow(HallNotFoundException::new);

        Hall updatedHall = hallMapper.partialUpdate(hallDto, existingHall);
        updatedHall = hallRepository.save(updatedHall);

        return hallModelAssembler.toModel(hallMapper.toDto(updatedHall));
    }

    @Override
    public void deleteHall(Long id) {
        hallRepository.deleteById(id);
    }
}