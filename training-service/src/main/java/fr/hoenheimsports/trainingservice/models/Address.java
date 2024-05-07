package fr.hoenheimsports.trainingservice.models;

import jakarta.persistence.Embeddable;

import java.util.Objects;

@Embeddable
public class Address {
    private String street;
    private String city;
    private String postalCode;
    private String country;

    public Address() {
    }

    public Address(String street, String city, String postalCode, String country) {
        this.street = street;
        this.city = city;
        this.postalCode = postalCode;
        this.country = country;
    }

    private Address(Builder builder) {
        setStreet(builder.street);
        setCity(builder.city);
        setPostalCode(builder.postalCode);
        setCountry(builder.country);
    }

    public String getStreet() {
        return street;
    }

    public String getCity() {
        return city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getCountry() {
        return country;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return Objects.equals(getStreet(), address.getStreet()) && Objects.equals(getCity(), address.getCity()) && Objects.equals(getPostalCode(), address.getPostalCode()) && Objects.equals(getCountry(), address.getCountry());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getStreet(), getCity(), getPostalCode(), getCountry());
    }

    @Override
    public String toString() {
        return "Address{" +
                "street='" + street + '\'' +
                ", city='" + city + '\'' +
                ", postalCode='" + postalCode + '\'' +
                ", country='" + country + '\'' +
                '}';
    }

    /**
     * {@code Address} builder static inner class.
     */
    public static final class Builder {
        private String street;
        private String city;
        private String postalCode;
        private String country;

        private Builder() {
        }

        public static Builder builder() {
            return new Builder();
        }

        /**
         * Sets the {@code street} and returns a reference to this Builder enabling method chaining.
         *
         * @param street the {@code street} to set
         * @return a reference to this Builder
         */
        public Builder street(String street) {
            this.street = street;
            return this;
        }

        /**
         * Sets the {@code city} and returns a reference to this Builder enabling method chaining.
         *
         * @param city the {@code city} to set
         * @return a reference to this Builder
         */
        public Builder city(String city) {
            this.city = city;
            return this;
        }

        /**
         * Sets the {@code postalCode} and returns a reference to this Builder enabling method chaining.
         *
         * @param postalCode the {@code postalCode} to set
         * @return a reference to this Builder
         */
        public Builder postalCode(String postalCode) {
            this.postalCode = postalCode;
            return this;
        }

        /**
         * Sets the {@code country} and returns a reference to this Builder enabling method chaining.
         *
         * @param country the {@code country} to set
         * @return a reference to this Builder
         */
        public Builder country(String country) {
            this.country = country;
            return this;
        }

        /**
         * Returns a {@code Address} built from the parameters previously set.
         *
         * @return a {@code Address} built with parameters of this {@code Address.Builder}
         */
        public Address build() {
            return new Address(this);
        }
    }
}
