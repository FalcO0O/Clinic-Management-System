package pl.edu.agh.to.backendspringboot.domain.schedule.model;

import pl.edu.agh.to.backendspringboot.domain.consulting_room.model.ConsultingRoomBrief;

import java.time.LocalTime;

public interface ScheduleBrief {
    Integer getId();

    LocalTime getShiftStart();

    LocalTime getShiftEnd();

    ConsultingRoomBrief getConsultingRoom();
}
