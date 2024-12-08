package fr.hoenheimsports.trainingservice.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "email")
        }
)
public class Coach {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 100)
    private String name;
    @Column(length = 100)
    private String surname;
    @Column(length = 100)
    private String email;
    @Column(length = 15)
    private String phone;

    @ManyToMany(mappedBy = "coaches")
    private Set<Team> teams = new HashSet<>();;


    public Set<Team> getTeams() {
        return new HashSet<>(teams);
    }

    void addTeam(Team team) {
        this.teams.add(team);
    }

    void removeTeam(Team team) {
        this.teams.remove(team);
    }


    @Override
    public String toString() {
        return "Coach{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", teams=" + teams.stream().map(Team::getId).map(String::valueOf).collect(Collectors.joining(",")) +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Coach user = (Coach) o;
        return id != null && id.equals(user.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode(); // ou Objects.hash(id)
    }

}
