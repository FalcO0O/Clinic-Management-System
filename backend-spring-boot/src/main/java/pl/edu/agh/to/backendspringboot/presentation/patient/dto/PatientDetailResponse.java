package pl.edu.agh.to.backendspringboot.presentation.patient.dto;

import pl.edu.agh.to.backendspringboot.domain.patient.model.Patient;
import pl.edu.agh.to.backendspringboot.domain.visit.VisitBriefPatient;
import pl.edu.agh.to.backendspringboot.presentation.visit.dto.VisitBriefPatientResponse;

import java.util.List;

public record PatientDetailResponse(
        Integer id,
        String firstName,
        String lastName,
        String pesel,
        String street,
        String city,
        String postalCode,
        List<VisitBriefPatientResponse> visits
) {
    public static PatientDetailResponse from(Patient patient, List<VisitBriefPatient> visits) {
        List<VisitBriefPatientResponse> visitDtos = visits.stream().map(VisitBriefPatientResponse::from)
                .toList();

        return new PatientDetailResponse(
                patient.getId(),
                patient.getFirstName(),
                patient.getLastName(),
                patient.getPesel(),
                patient.getAddress().getStreet(),
                patient.getAddress().getCity(),
                patient.getAddress().getPostalCode(),
                visitDtos
        );
    }
}