package fr.hoenheimsports.trainingservice.models;

import jakarta.persistence.*;

import java.util.Objects;
import java.util.Set;

@Entity
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Enumerated(EnumType.ORDINAL)
    private Gender gender;
    @Enumerated(EnumType.ORDINAL)
    private Category category;
    private int teamNumber;
    @ManyToOne
    private Coach coach;
    @OneToMany(cascade = {CascadeType.REMOVE})
    private Set<TrainingSession> trainingSession;

    private Team(Builder builder) {
        setId(builder.id);
        setGender(builder.gender);
        setCategory(builder.category);
        setTeamNumber(builder.teamNumber);
        setCoach(builder.coach);
        setTrainingSession(builder.trainingSession);
    }

    public Team() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public int getTeamNumber() {
        return teamNumber;
    }

    public void setTeamNumber(int teamNumber) {
        this.teamNumber = teamNumber;
    }

    public Coach getCoach() {
        return coach;
    }

    public void setCoach(Coach coach) {
        this.coach = coach;
    }

    public Set<TrainingSession> getTrainingSession() {
        return trainingSession;
    }

    public void setTrainingSession(Set<TrainingSession> trainingSession) {
        this.trainingSession = trainingSession;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Team team = (Team) o;
        return getTeamNumber() == team.getTeamNumber() && Objects.equals(getId(), team.getId()) && getGender() == team.getGender() && getCategory() == team.getCategory() && Objects.equals(getCoach(), team.getCoach()) && Objects.equals(getTrainingSession(), team.getTrainingSession());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getGender(), getCategory(), getTeamNumber(), getCoach(), getTrainingSession());
    }

    @Override
    public String toString() {
        return "Team{" +
                "id=" + id +
                ", gender=" + gender +
                ", category=" + category +
                ", teamNumber=" + teamNumber +
                ", coach=" + coach +
                ", trainingSession=" + trainingSession +
                '}';
    }


    public static Builder builder() {
        return new Builder();
    }

    /**
     * {@code Team} builder static inner class.
     */
    public static final class Builder {
        private Long id;
        private Gender gender;
        private Category category;
        private int teamNumber;
        private Coach coach;
        private Set<TrainingSession> trainingSession;

        private Builder() {
        }

        public static Builder builder() {
            return new Builder();
        }

        /**
         * Sets the {@code id} and returns a reference to this Builder enabling method chaining.
         *
         * @param id the {@code id} to set
         * @return a reference to this Builder
         */
        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        /**
         * Sets the {@code gender} and returns a reference to this Builder enabling method chaining.
         *
         * @param gender the {@code gender} to set
         * @return a reference to this Builder
         */
        public Builder gender(Gender gender) {
            this.gender = gender;
            return this;
        }

        /**
         * Sets the {@code category} and returns a reference to this Builder enabling method chaining.
         *
         * @param category the {@code category} to set
         * @return a reference to this Builder
         */
        public Builder category(Category category) {
            this.category = category;
            return this;
        }

        /**
         * Sets the {@code teamNumber} and returns a reference to this Builder enabling method chaining.
         *
         * @param teamNumber the {@code teamNumber} to set
         * @return a reference to this Builder
         */
        public Builder teamNumber(int teamNumber) {
            this.teamNumber = teamNumber;
            return this;
        }

        /**
         * Sets the {@code coach} and returns a reference to this Builder enabling method chaining.
         *
         * @param coach the {@code coach} to set
         * @return a reference to this Builder
         */
        public Builder coach(Coach coach) {
            this.coach = coach;
            return this;
        }

        /**
         * Sets the {@code trainingSession} and returns a reference to this Builder enabling method chaining.
         *
         * @param trainingSession the {@code trainingSession} to set
         * @return a reference to this Builder
         */
        public Builder trainingSession(Set<TrainingSession> trainingSession) {
            this.trainingSession = trainingSession;
            return this;
        }

        /**
         * Returns a {@code Team} built from the parameters previously set.
         *
         * @return a {@code Team} built with parameters of this {@code Team.Builder}
         */
        public Team build() {
            return new Team(this);
        }
    }
}
