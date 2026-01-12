package pl.edu.agh.to.backendspringboot.presentation.schedule.dto;

import pl.edu.agh.to.backendspringboot.presentation.doctor.dto.DoctorBriefResponse;


public record ConsultingRoomScheduleResponse(
        DoctorBriefResponse doctor,
        ScheduleResponse dutyTime
) {

}
