package fr.hoenheimsports.trainingservice.repositories;

import fr.hoenheimsports.trainingservice.models.TrainingSession;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrainingSessionRepository extends JpaRepository<TrainingSession, Long> {
}