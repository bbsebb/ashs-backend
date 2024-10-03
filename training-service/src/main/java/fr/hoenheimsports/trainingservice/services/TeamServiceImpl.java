package fr.hoenheimsports.trainingservice.services;

import fr.hoenheimsports.trainingservice.Exception.TeamNotFoundException;
import fr.hoenheimsports.trainingservice.assemblers.TeamModelAssembler;
import fr.hoenheimsports.trainingservice.assemblers.TeamPagedModelAssembler;
import fr.hoenheimsports.trainingservice.dto.TeamDto;
import fr.hoenheimsports.trainingservice.mappers.TeamMapper;
import fr.hoenheimsports.trainingservice.models.Team;
import fr.hoenheimsports.trainingservice.repositories.TeamRepository;
import fr.hoenheimsports.trainingservice.ressources.TeamModel;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeamServiceImpl implements TeamService {
    private final TeamRepository teamRepository;
    private final TeamModelAssembler teamModelAssembler;
    private final TeamPagedModelAssembler teamPagedModelAssembler;
    private final TeamMapper teamMapper;
    private final SortUtil sortUtil;

    public TeamServiceImpl(TeamRepository teamRepository, TeamModelAssembler teamModelAssemblerImpl, TeamPagedModelAssembler teamPagedModelAssembler, TeamMapper teamMapper, SortUtil sortUtil) {
        this.teamRepository = teamRepository;
        this.teamModelAssembler = teamModelAssemblerImpl;
        this.teamPagedModelAssembler = teamPagedModelAssembler;
        this.teamMapper = teamMapper;
        this.sortUtil = sortUtil;
    }

    @Override
    public TeamModel createTeam(TeamDto teamDto) {
       Team newTeam = this.teamRepository.save(this.teamMapper.toEntity(teamDto));
       return this.teamModelAssembler.toModel(this.teamMapper.toDto(newTeam));
    }

    @Override
    public PagedModel<TeamModel> getAllTeams(int page, int size, List<String> sort) {
        Pageable pageable = PageRequest.of(page, size, this.sortUtil.createSort(sort));
        return teamPagedModelAssembler.toModel(teamRepository.findAll(pageable).map(teamMapper::toDto).map(teamModelAssembler::toModel));
    }

    @Override
    public TeamModel getTeamById(Long id) throws TeamNotFoundException {
        return teamRepository.findById(id)
                .map(teamMapper::toDto)
                .map(teamModelAssembler::toModel)
                .orElseThrow(TeamNotFoundException::new);
    }

    @Override
    public TeamModel updateTeam(Long id, TeamDto teamDto) throws TeamNotFoundException {
        Team existingTeam = teamRepository.findById(id)
                .orElseThrow(TeamNotFoundException::new);

        Team updatedTeam = teamMapper.partialUpdate(teamDto, existingTeam);
        updatedTeam = teamRepository.save(updatedTeam);

        return teamModelAssembler.toModel(teamMapper.toDto(updatedTeam));
    }

    @Override
    public void deleteTeam(Long id) {
        teamRepository.deleteById(id);
    }
}