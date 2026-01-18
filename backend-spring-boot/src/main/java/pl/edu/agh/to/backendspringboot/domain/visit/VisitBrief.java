package pl.edu.agh.to.backendspringboot.domain.visit;

import pl.edu.agh.to.backendspringboot.domain.consulting_room.model.ConsultingRoomBrief;

import java.time.LocalTime;

public interface VisitBrief {
    Integer getId();

    LocalTime getVisitStart();

    LocalTime getVisitEnd();

    ConsultingRoomBrief getConsultingRoom();
}
