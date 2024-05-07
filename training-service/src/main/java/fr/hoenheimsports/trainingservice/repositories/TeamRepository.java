package fr.hoenheimsports.trainingservice.repositories;

import fr.hoenheimsports.trainingservice.models.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, Long> {
}