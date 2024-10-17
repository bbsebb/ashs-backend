package fr.hoenheimsports.trainingservice.services;

import fr.hoenheimsports.trainingservice.Exception.DataNotFoundException;
import fr.hoenheimsports.trainingservice.Exception.TeamNotFoundException;
import fr.hoenheimsports.trainingservice.assemblers.TeamModelAssembler;
import fr.hoenheimsports.trainingservice.assemblers.TeamPagedModelAssembler;
import fr.hoenheimsports.trainingservice.dto.TeamDto;
import fr.hoenheimsports.trainingservice.mappers.TeamMapper;
import fr.hoenheimsports.trainingservice.models.Coach;
import fr.hoenheimsports.trainingservice.models.Team;
import fr.hoenheimsports.trainingservice.models.TrainingSession;
import fr.hoenheimsports.trainingservice.repositories.CoachRepository;
import fr.hoenheimsports.trainingservice.repositories.TeamRepository;
import fr.hoenheimsports.trainingservice.repositories.TrainingSessionRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TeamServiceImpl implements TeamService {
    private final TeamRepository teamRepository;
    private final CoachRepository coachRepository;
    private final TrainingSessionRepository trainingSessionRepository;
    private final TeamModelAssembler teamModelAssembler;
    private final TeamPagedModelAssembler teamPagedModelAssembler;
    private final TeamMapper teamMapper;
    private final SortUtil sortUtil;

    public TeamServiceImpl(TeamRepository teamRepository, CoachRepository coachRepository, TrainingSessionRepository trainingSessionRepository, TeamModelAssembler teamModelAssemblerImpl, TeamPagedModelAssembler teamPagedModelAssembler, TeamMapper teamMapper, SortUtil sortUtil) {
        this.teamRepository = teamRepository;
        this.coachRepository = coachRepository;
        this.trainingSessionRepository = trainingSessionRepository;
        this.teamModelAssembler = teamModelAssemblerImpl;
        this.teamPagedModelAssembler = teamPagedModelAssembler;
        this.teamMapper = teamMapper;
        this.sortUtil = sortUtil;
    }

    @Override
    public EntityModel<TeamDto> createTeam(TeamDto teamDto)   {
        Team newTeam = this.teamMapper.toEntity(teamDto);
        persistRelatedEntities(newTeam);
        newTeam = this.teamRepository.save(newTeam);
       return this.teamModelAssembler.toModel(this.teamMapper.toDto(newTeam));
    }

    @Override
    public PagedModel<?> getAllTeams(int page, int size, List<String> sort) {
        Pageable pageable = PageRequest.of(page, size, this.sortUtil.createSort(sort));
        return teamPagedModelAssembler.toModel(teamRepository.findAll(pageable).map(teamMapper::toDto).map(teamModelAssembler::toModel));
    }

    @Override
    public EntityModel<TeamDto> getTeamById(Long id) throws TeamNotFoundException {
        return teamRepository.findById(id)
                .map(teamMapper::toDto)
                .map(teamModelAssembler::toModel)
                .orElseThrow(TeamNotFoundException::new);
    }

    @Override
    public EntityModel<TeamDto> updateTeam(Long id, TeamDto teamDto) throws TeamNotFoundException {
        Team existingTeam = teamRepository.findById(id)
                .orElseThrow(TeamNotFoundException::new);

        persistRelatedEntities(existingTeam);

        Team updatedTeam = teamMapper.partialUpdate(teamDto, existingTeam);
        updatedTeam = teamRepository.save(updatedTeam);

        return teamModelAssembler.toModel(teamMapper.toDto(updatedTeam));
    }

    private void persistRelatedEntities(Team existingTeam) {
        if(existingTeam.getCoach() != null) {
            Coach savedCoach = this.coachRepository.save(existingTeam.getCoach());
            existingTeam.setCoach(savedCoach);
        }
        if(existingTeam.getTrainingSessions() != null) {
            Set<TrainingSession> savedTrainingSessions = existingTeam.getTrainingSessions().stream()
                    .filter(Objects::nonNull)
                    .map(this.trainingSessionRepository::save)
                    .collect(Collectors.toSet());
            existingTeam.setTrainingSessions(savedTrainingSessions);
        }

    }

    @Override
    public void deleteTeam(Long id) {
        teamRepository.deleteById(id);
    }
}