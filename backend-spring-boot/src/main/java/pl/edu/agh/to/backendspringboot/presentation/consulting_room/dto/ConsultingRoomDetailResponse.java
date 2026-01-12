package pl.edu.agh.to.backendspringboot.presentation.consulting_room.dto;

import pl.edu.agh.to.backendspringboot.domain.consulting_room.model.ConsultingRoom;
import pl.edu.agh.to.backendspringboot.domain.doctor.model.DoctorDetail;
import pl.edu.agh.to.backendspringboot.presentation.doctor.dto.DoctorBriefResponse;
import pl.edu.agh.to.backendspringboot.presentation.doctor.dto.DoctorDetailResponse;
import pl.edu.agh.to.backendspringboot.presentation.schedule.dto.ConsultingRoomScheduleResponse;
import pl.edu.agh.to.backendspringboot.presentation.schedule.dto.ScheduleResponse;

import java.util.List;

public record ConsultingRoomDetailResponse(
        Integer id,
        String roomNumber,
        List<ConsultingRoomScheduleResponse> schedules
) {
    public static ConsultingRoomDetailResponse from(ConsultingRoom consultingRoom){
        List<ConsultingRoomScheduleResponse> schedules = consultingRoom.getSchedules()
                .stream()
                .map((schedule)->new ConsultingRoomScheduleResponse(
                        DoctorBriefResponse.from(schedule.getDoctor()),
                        ScheduleResponse.from(schedule))
                )
                .toList();

        return new ConsultingRoomDetailResponse(
                consultingRoom.getId(),
                consultingRoom.getRoomNumber(),
                schedules
        );
    }
}
