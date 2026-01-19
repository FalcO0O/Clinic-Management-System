package pl.edu.agh.to.backendspringboot.presentation.schedule.dto;

import pl.edu.agh.to.backendspringboot.domain.schedule.model.Schedule;
import pl.edu.agh.to.backendspringboot.domain.schedule.model.ScheduleBrief;

import java.time.LocalDateTime;

public record ScheduleResponse(
        Integer id,
        LocalDateTime shiftStart,
        LocalDateTime shiftEnd
) {
    public static ScheduleResponse from(ScheduleBrief schedule) {
        return new ScheduleResponse(
                schedule.getId(),
                schedule.getShiftStart(),
                schedule.getShiftEnd()
        );
    }

    public static ScheduleResponse from(Schedule schedule) {
        return new ScheduleResponse(
                schedule.getId(),
                schedule.getShiftStart(),
                schedule.getShiftEnd()
        );
    }
}
