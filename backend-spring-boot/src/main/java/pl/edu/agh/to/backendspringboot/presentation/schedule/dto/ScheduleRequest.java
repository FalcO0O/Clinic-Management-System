package pl.edu.agh.to.backendspringboot.presentation.schedule.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;
import pl.edu.agh.to.backendspringboot.domain.consulting_room.model.ConsultingRoom;
import pl.edu.agh.to.backendspringboot.domain.doctor.model.Doctor;
import pl.edu.agh.to.backendspringboot.domain.schedule.model.Schedule;

import java.time.LocalDateTime;

public record ScheduleRequest(
        @Schema(
                description = "Godzina rozpoczęcia dyżuru",
                type = "string",
                pattern = "^\\d{2}:\\d{2}$",
                example = "08:00"
        )
        @DateTimeFormat(pattern ="HH:mm")
        LocalDateTime startTime,

        @Schema(
                description = "Godzina zakończenia dyżuru",
                type = "string",
                pattern = "^\\d{2}:\\d{2}$",
                example = "16:00"
        )
        @DateTimeFormat(pattern ="HH:mm")
        LocalDateTime endTime,

        @Schema(description = "ID lekarza", example = "10")
        @NotNull
        Integer doctorId,

        @Schema(description = "ID gabinetu lekarskiego", example = "5")
        @NotNull
        Integer consultingRoomId
) {
    public Schedule toSchedule(Doctor doctor, ConsultingRoom consultingRoom){
        return new Schedule(doctor,consultingRoom,startTime,endTime);
    }
}
