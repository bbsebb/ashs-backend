package fr.hoenheimsports.trainingservice.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;

import java.util.HashSet;
import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class TrainingSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private TimeSlot timeSlot;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Hall hall;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Team team;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        TrainingSession user = (TrainingSession) o;
        return id != null && id.equals(user.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode(); // ou Objects.hash(id)
    }
}
