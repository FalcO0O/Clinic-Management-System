package pl.edu.agh.to.backendspringboot.presentation.visit.dto;

import pl.edu.agh.to.backendspringboot.domain.visit.VisitBriefPatient;
import pl.edu.agh.to.backendspringboot.presentation.consulting_room.dto.ConsultingRoomBriefResponse;
import pl.edu.agh.to.backendspringboot.presentation.doctor.dto.DoctorBriefResponse;

import java.time.LocalDateTime;

public record VisitBriefPatientResponse(
                                        int id,
                                        LocalDateTime visitStart,
                                        LocalDateTime visitEnd,
                                        ConsultingRoomBriefResponse consultingRoom,
                                        DoctorBriefResponse doctor
)
{
    public static VisitBriefPatientResponse from(VisitBriefPatient visit) {
        return new VisitBriefPatientResponse(
                visit.getId(),
                visit.getVisitStart(),
                visit.getVisitEnd(),
                ConsultingRoomBriefResponse.from(visit.getConsultingRoom()),
                DoctorBriefResponse.from(visit.getDoctor()));
    }
}