package pl.edu.agh.to.backendspringboot.presentation.visit.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.format.annotation.DateTimeFormat;
import pl.edu.agh.to.backendspringboot.domain.consulting_room.model.ConsultingRoom;
import pl.edu.agh.to.backendspringboot.domain.doctor.model.Doctor;
import pl.edu.agh.to.backendspringboot.domain.patient.model.Patient;
import pl.edu.agh.to.backendspringboot.domain.visit.Visit;

import java.time.LocalTime;

public record VisitRequest(
        int doctorId,
        int patientId,
        int consultingRoomId,
        @Schema(
                description = "Godzina rozpoczęcia wizyty",
                type = "string",
                pattern = "^\\d{2}:\\d{2}$",
                example = "08:00"
        )
        @DateTimeFormat(pattern ="HH:mm")
        LocalTime visitStart,

        @Schema(
                description = "Godzina zakończenia wizyty",
                type = "string",
                pattern = "^\\d{2}:\\d{2}$",
                example = "08:30"
        )
        @DateTimeFormat(pattern ="HH:mm")
        LocalTime visitEnd

) {
    public Visit toEntity(Doctor doctor, Patient patient, ConsultingRoom consultingRoom)
    {
        return new Visit(
                patient,
                doctor,
                visitStart,
                visitEnd,
                consultingRoom
        );
    }
}
