package pl.edu.agh.to.backendspringboot.presentation.schedule.dto;

import pl.edu.agh.to.backendspringboot.domain.schedule.model.ScheduleDetail;
import pl.edu.agh.to.backendspringboot.presentation.consulting_room.dto.ConsultingRoomBriefResponse;
import pl.edu.agh.to.backendspringboot.presentation.doctor.dto.DoctorBriefResponse;

public record ScheduleDetailResponse(
        ConsultingRoomBriefResponse consultingRoom,
        DoctorBriefResponse doctor,
        ScheduleResponse dutyTime
) {
    public static ScheduleDetailResponse from(ScheduleDetail schedule) {
        return new ScheduleDetailResponse(
                ConsultingRoomBriefResponse.from(schedule.getConsultingRoom()),
                DoctorBriefResponse.from(schedule.getDoctor()),
                ScheduleResponse.from(schedule)
        );
    }
}
