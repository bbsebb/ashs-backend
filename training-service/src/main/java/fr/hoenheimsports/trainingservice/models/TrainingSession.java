package fr.hoenheimsports.trainingservice.models;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
public class TrainingSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Embedded
    private TimeSlot timeSlot;
    @ManyToOne
    private Hall hall;


    public TrainingSession() {

    }

    private TrainingSession(Builder builder) {
        setId(builder.id);
        setTimeSlot(builder.timeSlot);
        setHall(builder.hall);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TimeSlot getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(TimeSlot timeSlot) {
        this.timeSlot = timeSlot;
    }

    public Hall getHall() {
        return hall;
    }

    public void setHall(Hall hall) {
        this.hall = hall;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TrainingSession that = (TrainingSession) o;
        return Objects.equals(getId(), that.getId()) && Objects.equals(getTimeSlot(), that.getTimeSlot()) && Objects.equals(getHall(), that.getHall());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getTimeSlot(), getHall());
    }

    @Override
    public String toString() {
        return "TrainingSession{" +
                "id=" + id +
                ", timeSlot=" + timeSlot +
                ", hall=" + hall +
                '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    /**
     * {@code TrainingSession} builder static inner class.
     */
    public static final class Builder {
        private Long id;
        private TimeSlot timeSlot;
        private Hall hall;

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
         * Sets the {@code timeSlot} and returns a reference to this Builder enabling method chaining.
         *
         * @param timeSlot the {@code timeSlot} to set
         * @return a reference to this Builder
         */
        public Builder timeSlot(TimeSlot timeSlot) {
            this.timeSlot = timeSlot;
            return this;
        }

        /**
         * Sets the {@code hall} and returns a reference to this Builder enabling method chaining.
         *
         * @param hall the {@code hall} to set
         * @return a reference to this Builder
         */
        public Builder hall(Hall hall) {
            this.hall = hall;
            return this;
        }

        /**
         * Returns a {@code TrainingSession} built from the parameters previously set.
         *
         * @return a {@code TrainingSession} built with parameters of this {@code TrainingSession.Builder}
         */
        public TrainingSession build() {
            return new TrainingSession(this);
        }
    }
}
