package pl.edu.agh.to.backendspringboot.presentation.patient.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import pl.edu.agh.to.backendspringboot.domain.patient.model.Patient;
import pl.edu.agh.to.backendspringboot.domain.shared.model.Address;

public record PatientRequest(
        @NotBlank(message = "First name is mandatory") String firstName,
        @NotBlank(message = "Last name is mandatory") String lastName,
        @Pattern(regexp = "^\\d{11}$", message = "Invalid PESEL") String pesel,
        @NotBlank(message = "Street is mandatory") String street,
        @NotBlank(message = "City is mandatory") String city,
        @Pattern(regexp = "^\\d{2}-\\d{3}$", message = "Invalid postal code") String postalCode
) {
    public static Patient toEntity(PatientRequest patientDTO) {
        return new Patient(
                patientDTO.firstName,
                patientDTO.lastName,
                patientDTO.pesel,
                new Address(
                        patientDTO.street,
                        patientDTO.city,
                        patientDTO.postalCode
                )
        );
    }
}