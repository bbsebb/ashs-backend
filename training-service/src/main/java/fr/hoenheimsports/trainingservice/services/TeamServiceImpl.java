package fr.hoenheimsports.trainingservice.services;

import fr.hoenheimsports.trainingservice.Exception.CoachNotFoundException;
import fr.hoenheimsports.trainingservice.Exception.HallNotFoundException;
import fr.hoenheimsports.trainingservice.Exception.TeamNotFoundException;
import fr.hoenheimsports.trainingservice.Exception.TrainingSessionNotFoundException;
import fr.hoenheimsports.trainingservice.assemblers.TeamAssembler;
import fr.hoenheimsports.trainingservice.dto.TeamDTO;
import fr.hoenheimsports.trainingservice.dto.request.CoachDTORequest;
import fr.hoenheimsports.trainingservice.dto.request.TeamDTORequest;
import fr.hoenheimsports.trainingservice.dto.request.TrainingSessionDTORequest;
import fr.hoenheimsports.trainingservice.mappers.TeamMapper;
import fr.hoenheimsports.trainingservice.models.Coach;
import fr.hoenheimsports.trainingservice.models.Team;
import fr.hoenheimsports.trainingservice.models.TrainingSession;
import fr.hoenheimsports.trainingservice.repositories.TeamRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class TeamServiceImpl implements TeamService {
    private final TeamRepository teamRepository;
    private final TrainingSessionService trainingSessionService;
    private final TeamAssembler teamAssembler;
    private final TeamMapper teamMapper;
    private final CoachService coachService;

    public TeamServiceImpl(TeamRepository teamRepository, TrainingSessionService trainingSessionService, TeamAssembler teamAssembler, TeamMapper teamMapper, CoachService coachService) {
        this.teamRepository = teamRepository;
        this.trainingSessionService = trainingSessionService;
        this.teamAssembler = teamAssembler;
        this.teamMapper = teamMapper;
        this.coachService = coachService;
    }

    @Override
    public Team findOrCreateOrUpdate(TeamDTORequest teamDtoRequest) throws TeamNotFoundException, CoachNotFoundException, TrainingSessionNotFoundException, HallNotFoundException {
        if(teamDtoRequest == null) {
            throw new TeamNotFoundException();
        }
        Team team;
        if(teamDtoRequest.id() != null) {
            team = this.update(teamDtoRequest.id(), teamDtoRequest);
        } else {
            team =  this.create(teamDtoRequest);
        }
        return this.teamRepository.save(team);
    }

    @Override
    public TeamDTO createAndConvertToModel(TeamDTORequest teamDtoRequest) throws TrainingSessionNotFoundException, CoachNotFoundException, HallNotFoundException {
       return this.teamAssembler.toModel(this.create(teamDtoRequest));
    }

    private Team create(TeamDTORequest teamDtoRequest) throws TrainingSessionNotFoundException, CoachNotFoundException, HallNotFoundException {
        Set<TrainingSession> trainingSessions = getPersistedTrainingSessions(teamDtoRequest);
        teamDtoRequest.trainingSessions().clear();
        Set<Coach> coaches = getPersistedCoaches(teamDtoRequest);
        teamDtoRequest.coaches().clear();

        Team newTeam = this.teamMapper.toEntity(teamDtoRequest);

        //on ajoute celles déjà persistés.
        trainingSessions.forEach(newTeam::addTrainingSession);

        //on ajoute celles déjà persistés.
        coaches.forEach(newTeam::addCoach);

        return this.teamRepository.save(newTeam);
    }

    @Override
    public PagedModel<TeamDTO> getAllModels(Pageable pageable) {
        return teamAssembler.toPagedModel(teamRepository.findAll(pageable));
    }

    @Override
    public TeamDTO getModelById(Long id) throws TeamNotFoundException {
        return teamRepository.findById(id)
                .map(teamAssembler::toModel)
                .orElseThrow(TeamNotFoundException::new);
    }

    @Override
    @Transactional
    public TeamDTO updateAndConvertToModel(Long id, TeamDTORequest teamDtoRequest) throws TeamNotFoundException, CoachNotFoundException, TrainingSessionNotFoundException, HallNotFoundException {
        return teamAssembler.toModel(this.update(id, teamDtoRequest));
    }


    private Team update(Long id, TeamDTORequest teamDtoRequest) throws TeamNotFoundException, CoachNotFoundException, TrainingSessionNotFoundException, HallNotFoundException {
        Team existingTeam = teamRepository.findById(id).orElseThrow(TeamNotFoundException::new);
        Set<TrainingSession> trainingSessions = getPersistedTrainingSessions(teamDtoRequest);
        Set<Coach> coaches = getPersistedCoaches(teamDtoRequest);
        teamMapper.partialUpdate(teamDtoRequest,existingTeam);

        //Je supprime les trainingSessions qui ne sont plus dans la liste
        existingTeam.getTrainingSessions().stream()
                .filter(ts -> trainingSessions.stream().noneMatch(newTs -> newTs.getId().equals(ts.getId())))
                .forEach(existingTeam::removeTrainingSession);

        // J'ajoute les nouvelles trainingSessions
        trainingSessions.forEach(existingTeam::addTrainingSession);


        // Je supprime les coaches qui ne sont plus dans la liste
        existingTeam.getCoaches().stream().filter(coach -> coaches.stream()
                .noneMatch(c -> c.getId().equals(coach.getId()))).forEach(existingTeam::removeCoach);
        // J'ajoute les nouveaux coaches
        coaches.forEach(existingTeam::addCoach);

        return teamRepository.save(existingTeam);
    }


    @Override
    public void deleteById(Long id) {
        teamRepository.deleteById(id);
    }

    /**
     * Crée ou met à jour ou trouve les coaches d'une équipe
     *
     * @param teamDtoRequest le dto de l'équipe
     * @return la liste des coaches persistés
     * @throws CoachNotFoundException si un coach n'existe pas dans la liste
     */
    private Set<Coach> getPersistedCoaches(TeamDTORequest teamDtoRequest) throws CoachNotFoundException {
        Set<Coach> coaches = new HashSet<>();
        if (teamDtoRequest.coaches() != null) {
            for (CoachDTORequest coachDtoRequest : teamDtoRequest.coaches()) {
                Coach newCoach = this.coachService.findOrCreateOrUpdate(coachDtoRequest);
                coaches.add(newCoach);
            }
        }
        return coaches;
    }

    /**
     * Crée ou met à jour ou trouve les trainingSessions d'une équipe
     * @param teamDtoRequest le dto de l'équipe
     * @return la liste des trainingSessions persistés
     * @throws TrainingSessionNotFoundException si un trainingSession n'existe pas dans la liste
     * @throws HallNotFoundException si un hall n'existe pas dans la liste
     */
    private Set<TrainingSession> getPersistedTrainingSessions(TeamDTORequest teamDtoRequest) throws TrainingSessionNotFoundException, HallNotFoundException {
        Set<TrainingSession> trainingSessions = new HashSet<>();
        if (teamDtoRequest.trainingSessions() != null) {
            for (TrainingSessionDTORequest trainingSessionDtoRequest : teamDtoRequest.trainingSessions()) {
                TrainingSession session = this.trainingSessionService.findOrCreateOrUpdate(trainingSessionDtoRequest);
                trainingSessions.add(session);
            }
        }
        return trainingSessions;
    }
}