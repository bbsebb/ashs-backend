package fr.hoenheimsports.trainingservice.repositories;

import fr.hoenheimsports.trainingservice.models.Category;
import fr.hoenheimsports.trainingservice.models.Gender;
import fr.hoenheimsports.trainingservice.models.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TeamRepository extends JpaRepository<Team, Long> {
    @Query("select (count(t) > 0) from Team t where t.gender = ?1 and t.category = ?2 and t.teamNumber = ?3")
    boolean existsByGenderAndCategoryAndTeamNumber(Gender gender, Category category, int teamNumber);
}