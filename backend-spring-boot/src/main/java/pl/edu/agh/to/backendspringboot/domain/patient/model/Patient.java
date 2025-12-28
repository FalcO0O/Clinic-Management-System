package pl.edu.agh.to.backendspringboot.domain.patient.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import pl.edu.agh.to.backendspringboot.domain.shared.model.Address;

@Entity
public class Patient {

    @Id
    @GeneratedValue
    private Integer id;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotBlank
    private String pesel;

    @Embedded
    @NotNull
    private Address address;

    public Patient() {
    }

    public Patient(String firstName, String lastName, String pesel, Address address) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.pesel = pesel;
        this.address = address;
    }

    public Integer getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPesel() {
        return pesel;
    }

    public Address getAddress() {
        return address;
    }
}