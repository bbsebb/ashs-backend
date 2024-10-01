package fr.hoenheimsports.trainingservice.services;

import fr.hoenheimsports.trainingservice.Exception.CoachNotFoundException;
import fr.hoenheimsports.trainingservice.assemblers.CoachModelAssembler;
import fr.hoenheimsports.trainingservice.assemblers.CoachPagedModelAssembler;
import fr.hoenheimsports.trainingservice.dto.CoachDto;
import fr.hoenheimsports.trainingservice.mappers.CoachMapper;
import fr.hoenheimsports.trainingservice.models.Coach;
import fr.hoenheimsports.trainingservice.repositories.CoachRepository;
import fr.hoenheimsports.trainingservice.ressources.CoachModel;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CoachServiceImpl implements CoachService {
    private final CoachRepository coachRepository;
    private final CoachModelAssembler coachModelAssemblerImpl;
    private final CoachPagedModelAssembler coachPagedModelAssemblerImpl;
    private final CoachMapper coachMapper;
    private final SortUtil sortUtil;

    public CoachServiceImpl(CoachRepository coachRepository, CoachModelAssembler coachModelAssemblerImpl, CoachPagedModelAssembler coachPagedModelAssemblerImpl, CoachMapper coachMapper, SortUtil sortUtil) {
        this.coachRepository = coachRepository;
        this.coachModelAssemblerImpl = coachModelAssemblerImpl;
        this.coachPagedModelAssemblerImpl = coachPagedModelAssemblerImpl;
        this.coachMapper = coachMapper;
        this.sortUtil = sortUtil;
    }

    @Override
    public CoachModel createCoach(CoachDto coachDto) {
        Coach savedCoach = coachRepository.save(Coach.builder().email(coachDto.email()).name(coachDto.name()).phone(coachDto.phone()).surname(coachDto.surname()).build());
        return coachModelAssemblerImpl.toModel(coachMapper.toDto(savedCoach));
    }

    @Override
    public PagedModel<CoachModel> getAllCoaches(int page, int size, List<String> sort) {

        sort = (sort == null || sort.isEmpty())?  List.of("name,asc") : sort;
        Pageable pageable = PageRequest.of(page, size, this.sortUtil.createSort(sort));
        return coachPagedModelAssemblerImpl.toModel(coachRepository.findAll(pageable).map(coachMapper::toDto).map(coachModelAssemblerImpl::toModel));
    }



    @Override
    public CoachModel getCoachById(Long id) throws CoachNotFoundException {
        return coachRepository.findById(id).map(coachMapper::toDto).map(coachModelAssemblerImpl::toModel).orElseThrow(CoachNotFoundException::new);
    }

    @Override
    public CoachModel updateCoach(Long id, CoachDto coachDto) throws CoachNotFoundException {
        Coach coachToUpdate = coachRepository.findById(id).orElseThrow(CoachNotFoundException::new);
        coachToUpdate.setEmail(coachDto.email());
        coachToUpdate.setName(coachDto.name());
        coachToUpdate.setPhone(coachDto.phone());
        coachToUpdate.setSurname(coachDto.surname());
        Coach savedCoach = coachRepository.save(coachToUpdate);

        return coachModelAssemblerImpl.toModel(coachMapper.toDto(savedCoach));
    }

    @Override
    public void deleteCoach(Long id) {
        coachRepository.deleteById(id);
    }
}