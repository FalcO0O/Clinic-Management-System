package pl.edu.agh.to.backendspringboot.domain.visit;

import pl.edu.agh.to.backendspringboot.domain.doctor.model.Doctor;
import pl.edu.agh.to.backendspringboot.domain.doctor.model.DoctorBrief;
import pl.edu.agh.to.backendspringboot.domain.patient.model.PatientBrief;

public interface VisitDetail extends VisitBrief {
    DoctorBrief getDoctor();

    PatientBrief getPatient();

}
