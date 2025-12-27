package pl.edu.agh.to.backendspringboot.presentation.schedule.dto;

import pl.edu.agh.to.backendspringboot.domain.schedule.model.Schedule;

import java.time.LocalTime;

public record ScheduleResponse(
        Integer id,
        LocalTime shiftStart,
        LocalTime shiftEnd
) {
    public static ScheduleResponse from(Schedule schedule) {
        return new ScheduleResponse(
                schedule.getId(),
                schedule.getShiftStart(),
                schedule.getShiftEnd()
        );
    }
}
