package pl.edu.agh.to.backendspringboot.domain.visit;

import pl.edu.agh.to.backendspringboot.domain.doctor.model.DoctorBrief;

public interface  VisitBriefPatient extends VisitBrief {
    DoctorBrief getDoctor();
}
