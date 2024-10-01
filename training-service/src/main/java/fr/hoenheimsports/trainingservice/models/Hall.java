package fr.hoenheimsports.trainingservice.models;

import jakarta.persistence.*;

@Entity
public class Hall {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Embedded
    private Address address;

    private Hall(Builder builder) {
        setId(builder.id);
        setName(builder.name);
        setAddress(builder.address);
    }

    public Hall() {
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

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public static Builder builder() {
        return new Builder();
    }

    /**
     * {@code Halle} builder static inner class.
     */
    public static final class Builder {
        private Long id;
        private String name;
        private Address address;

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
         * Sets the {@code address} and returns a reference to this Builder enabling method chaining.
         *
         * @param address the {@code address} to set
         * @return a reference to this Builder
         */
        public Builder address(Address address) {
            this.address = address;
            return this;
        }

        /**
         * Returns a {@code Halle} built from the parameters previously set.
         *
         * @return a {@code Halle} built with parameters of this {@code Halle.Builder}
         */
        public Hall build() {
            return new Hall(this);
        }
    }
}
