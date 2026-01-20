package pl.edu.agh.to.backendspringboot.domain.schedule.model;

import pl.edu.agh.to.backendspringboot.domain.consulting_room.model.ConsultingRoomBrief;

import java.time.LocalDateTime;

public interface ScheduleBrief {
    Integer getId();

    LocalDateTime getShiftStart();

    LocalDateTime getShiftEnd();

    ConsultingRoomBrief getConsultingRoom();
}
