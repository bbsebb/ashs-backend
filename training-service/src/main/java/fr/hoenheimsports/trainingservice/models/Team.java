package fr.hoenheimsports.trainingservice.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"gender", "category", "teamNumber"})
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false)
    private Gender gender;
    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false)
    private Category category;
    @Column(nullable = false, columnDefinition = "int CHECK (team_number > 0)")
    private int teamNumber;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<Coach> coaches = new HashSet<>();

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("timeSlot.dayOfWeek ASC")
    private Set<TrainingSession> trainingSessions = new HashSet<>();

    public Set<Coach> getCoaches() {
        return new HashSet<>(this.coaches);
    }

    public Set<TrainingSession> getTrainingSessions() {
        return new HashSet<>(this.trainingSessions);
    }

    public void addTrainingSession(TrainingSession trainingSession) {
        if (trainingSessions.add(trainingSession)) {
            trainingSession.setTeam(this);
        }
    }

    public void removeTrainingSession(TrainingSession trainingSession) {
        if (trainingSessions.remove(trainingSession)) {
            trainingSession.setTeam(null);
        }
    }

    public void addCoach(Coach coach) {
            if(this.coaches.add(coach)) {
                coach.addTeam(this);
            }
    }

    public void removeCoach(Coach coach) {
            if(this.coaches.remove(coach))
        {
            coach.removeTeam(this);
        }
    }

    @Override
    public String toString() {
        return "Team{" +
                "id=" + id +
                ", gender=" + gender +
                ", category=" + category +
                ", teamNumber=" + teamNumber +
                ", coaches=" + coaches.stream().map(Coach::getId).map(String::valueOf).collect(Collectors.joining(",")) +
                ", trainingSessions=" + trainingSessions.stream().map(TrainingSession::getId).map(String::valueOf).collect(Collectors.joining(",")) +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Team user = (Team) o;
        return id != null && id.equals(user.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode(); // ou Objects.hash(id)
    }
}
