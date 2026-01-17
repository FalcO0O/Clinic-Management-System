package pl.edu.agh.to.backendspringboot.presentation.visit.dto;

import pl.edu.agh.to.backendspringboot.domain.visit.VisitBrief;
import pl.edu.agh.to.backendspringboot.presentation.consulting_room.dto.ConsultingRoomBriefResponse;
import pl.edu.agh.to.backendspringboot.presentation.doctor.dto.DoctorBriefResponse;

import java.time.LocalTime;

public record VisitBriefResponse(
    int id,
    LocalTime visitStart,
    LocalTime visitEnd,
    ConsultingRoomBriefResponse consultingRoom
)
{
    public static VisitBriefResponse from(VisitBrief visit) {
        return new VisitBriefResponse(
                visit.getId(),
                visit.getVisitStart(),
                visit.getVisitEnd(),
                ConsultingRoomBriefResponse.from(visit.getConsultingRoom())
        );
    }





}
