package pl.edu.agh.to.backendspringboot.presentation.visit.dto;

import pl.edu.agh.to.backendspringboot.domain.visit.VisitBriefDoctor;
import pl.edu.agh.to.backendspringboot.domain.visit.VisitBriefPatient;
import pl.edu.agh.to.backendspringboot.presentation.consulting_room.dto.ConsultingRoomBriefResponse;
import pl.edu.agh.to.backendspringboot.presentation.doctor.dto.DoctorBriefResponse;
import pl.edu.agh.to.backendspringboot.presentation.patient.dto.PatientBriefResponse;

import java.time.LocalTime;

public record VisitBriefDoctorResponse(
                                       int id,
                                       LocalTime visitStart,
                                       LocalTime visitEnd,
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