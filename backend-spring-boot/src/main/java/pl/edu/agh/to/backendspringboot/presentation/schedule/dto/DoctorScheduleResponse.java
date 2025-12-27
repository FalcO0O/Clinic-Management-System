package pl.edu.agh.to.backendspringboot.presentation.schedule.dto;

import pl.edu.agh.to.backendspringboot.presentation.consulting_room.dto.ConsultingRoomBriefResponse;

public record DoctorScheduleResponse(
        ConsultingRoomBriefResponse consultingRoom,
        ScheduleResponse dutyTime
) {
}
