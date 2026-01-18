package pl.edu.agh.to.backendspringboot.domain.visit;

import pl.edu.agh.to.backendspringboot.domain.consulting_room.model.ConsultingRoomBrief;
import pl.edu.agh.to.backendspringboot.domain.doctor.model.Doctor;
import pl.edu.agh.to.backendspringboot.domain.doctor.model.DoctorBrief;

import java.time.LocalTime;

public interface  VisitBriefPatient extends VisitBrief {
    DoctorBrief getDoctor();
}
