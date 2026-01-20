package pl.edu.agh.to.backendspringboot.domain.patient.model;

import pl.edu.agh.to.backendspringboot.domain.shared.model.Address;

public interface PatientDetail extends PatientBrief {
    Address getAddress();
}
