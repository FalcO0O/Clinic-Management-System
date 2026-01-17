package pl.edu.agh.to.backendspringboot.domain.visit;

import pl.edu.agh.to.backendspringboot.domain.consulting_room.model.ConsultingRoomBrief;
import pl.edu.agh.to.backendspringboot.domain.patient.model.PatientBrief;

import java.time.LocalTime;

public interface VisitBriefDoctor extends VisitBrief {

    ConsultingRoomBrief getConsultingRoom();

    PatientBrief getPatient();
}
