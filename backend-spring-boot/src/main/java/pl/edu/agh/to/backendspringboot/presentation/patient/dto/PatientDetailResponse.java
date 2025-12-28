package pl.edu.agh.to.backendspringboot.presentation.patient.dto;

import pl.edu.agh.to.backendspringboot.domain.patient.model.Patient;

public record PatientDetailResponse(
        Integer id,
        String firstName,
        String lastName,
        String pesel,
        String street,
        String city,
        String postalCode
) {
    public static PatientDetailResponse from(Patient patient) {
        return new PatientDetailResponse(
                patient.getId(),
                patient.getFirstName(),
                patient.getLastName(),
                patient.getPesel(),
                patient.getAddress().getStreet(),
                patient.getAddress().getCity(),
                patient.getAddress().getPostalCode()
        );
    }
}