package fr.hoenheimsports.trainingservice.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.util.Objects;

@Entity
public class Coach {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String surname;
    private String email;
    private String phone;

    private Coach(Builder builder) {
        setId(builder.id);
        setName(builder.name);
        setSurname(builder.surname);
        setEmail(builder.email);
        setPhone(builder.phone);
    }

    public Coach() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coach coach = (Coach) o;
        return Objects.equals(getId(), coach.getId()) && Objects.equals(getName(), coach.getName()) && Objects.equals(getSurname(), coach.getSurname()) && Objects.equals(getEmail(), coach.getEmail()) && Objects.equals(getPhone(), coach.getPhone());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getSurname(), getEmail(), getPhone());
    }

    @Override
    public String toString() {
        return "Coach{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    /**
     * {@code Coach} builder static inner class.
     */
    public static final class Builder {
        private Long id;
        private String name;
        private String surname;
        private String email;
        private String phone;

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
         * Sets the {@code name} and returns a reference to this Builder enabling method chaining.
         *
         * @param name the {@code name} to set
         * @return a reference to this Builder
         */
        public Builder name(String name) {
            this.name = name;
            return this;
        }

        /**
         * Sets the {@code surname} and returns a reference to this Builder enabling method chaining.
         *
         * @param surname the {@code surname} to set
         * @return a reference to this Builder
         */
        public Builder surname(String surname) {
            this.surname = surname;
            return this;
        }

        /**
         * Sets the {@code email} and returns a reference to this Builder enabling method chaining.
         *
         * @param email the {@code email} to set
         * @return a reference to this Builder
         */
        public Builder email(String email) {
            this.email = email;
            return this;
        }

        /**
         * Sets the {@code phone} and returns a reference to this Builder enabling method chaining.
         *
         * @param phone the {@code phone} to set
         * @return a reference to this Builder
         */
        public Builder phone(String phone) {
            this.phone = phone;
            return this;
        }

        /**
         * Returns a {@code Coach} built from the parameters previously set.
         *
         * @return a {@code Coach} built with parameters of this {@code Coach.Builder}
         */
        public Coach build() {
            return new Coach(this);
        }
    }
}
