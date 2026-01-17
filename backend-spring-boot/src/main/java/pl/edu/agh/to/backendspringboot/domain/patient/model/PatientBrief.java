package pl.edu.agh.to.backendspringboot.domain.patient.model;

import pl.edu.agh.to.backendspringboot.domain.doctor.model.MedicalSpecialization;

public interface PatientBrief {
    Integer getId();
    String getFirstName();
    String getLastName();
    String getPesel();
}