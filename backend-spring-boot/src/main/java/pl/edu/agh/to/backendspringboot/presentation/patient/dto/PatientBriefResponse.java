package pl.edu.agh.to.backendspringboot.presentation.patient.dto;

import pl.edu.agh.to.backendspringboot.domain.patient.model.Patient;
import pl.edu.agh.to.backendspringboot.domain.patient.model.PatientBrief;

public record PatientBriefResponse(
        Integer id,
        String firstName,
        String lastName,
        String pesel
) {
    public static PatientBriefResponse from(Patient patient) {
        return new PatientBriefResponse(
                patient.getId(),
                patient.getFirstName(),
                patient.getLastName(),
                patient.getPesel()
        );
    }

    public static PatientBriefResponse from(PatientBrief patientBrief) {
        return new PatientBriefResponse(
                patientBrief.getId(),
                patientBrief.getFirstName(),
                patientBrief.getLastName(),
                patientBrief.getPesel()
        );
    }
}