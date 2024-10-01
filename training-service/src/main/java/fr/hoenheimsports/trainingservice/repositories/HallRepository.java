package fr.hoenheimsports.trainingservice.repositories;

import fr.hoenheimsports.trainingservice.models.Hall;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HallRepository extends JpaRepository<Hall, Long> {
}