package pl.edu.agh.to.backendspringboot.presentation.visit.dto;

import pl.edu.agh.to.backendspringboot.domain.visit.VisitBriefDoctor;
import pl.edu.agh.to.backendspringboot.presentation.consulting_room.dto.ConsultingRoomBriefResponse;
import pl.edu.agh.to.backendspringboot.presentation.patient.dto.PatientBriefResponse;

import java.time.LocalDateTime;

public record VisitBriefDoctorResponse(
                                       int id,
                                       LocalDateTime visitStart,
                                       LocalDateTime visitEnd,
                                       ConsultingRoomBriefResponse consultingRoom,
                                       PatientBriefResponse patient
)
{
    public static VisitBriefDoctorResponse from(VisitBriefDoctor visit) {
        return new VisitBriefDoctorResponse(
                visit.getId(),
                visit.getVisitStart(),
                visit.getVisitEnd(),
                ConsultingRoomBriefResponse.from(visit.getConsultingRoom()),
                PatientBriefResponse.from(visit.getPatient()));
    }
}