package fr.hoenheimsports.trainingservice.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Hall {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false)
    private String name;

    @Embedded
    private Address address;

    @OneToMany(mappedBy = "hall", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<TrainingSession> trainingSessions = new HashSet<>();

    public Set<TrainingSession> getTrainingSessions() {
        return new HashSet<>(trainingSessions);
    }

    // Méthode d'ajout pour maintenir la relation bidirectionnelle
    public void addTrainingSession(TrainingSession trainingSession) {
        if(trainingSessions.add(trainingSession)) {
            trainingSession.setHall(this);
        } // Associe la session au hall
    }

    // Méthode de suppression pour maintenir la relation bidirectionnelle
    public void removeTrainingSession(TrainingSession trainingSession) {
        if(trainingSessions.remove(trainingSession)) {
            trainingSession.setHall(null);
        } // Dissocie la session du hall
    }


    @Override
    public String toString() {
        return "Hall{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address=" + address +
                ", trainingSessions=" + trainingSessions.stream().map(TrainingSession::getId).map(String::valueOf).collect(Collectors.joining(",")) +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Hall user = (Hall) o;
        return id != null && id.equals(user.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode(); // ou Objects.hash(id)
    }

}
