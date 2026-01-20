package pl.edu.agh.to.backendspringboot.domain.visit;

import pl.edu.agh.to.backendspringboot.domain.consulting_room.model.ConsultingRoomBrief;

import java.time.LocalDateTime;

public interface VisitBrief {
    Integer getId();

    LocalDateTime getVisitStart();

    LocalDateTime getVisitEnd();

    ConsultingRoomBrief getConsultingRoom();
}
