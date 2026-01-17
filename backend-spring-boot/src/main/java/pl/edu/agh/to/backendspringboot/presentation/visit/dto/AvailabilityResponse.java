package pl.edu.agh.to.backendspringboot.presentation.visit.dto;

import pl.edu.agh.to.backendspringboot.domain.consulting_room.model.ConsultingRoom;
import pl.edu.agh.to.backendspringboot.domain.doctor.model.Doctor;
import pl.edu.agh.to.backendspringboot.domain.doctor.model.DoctorBrief;
import pl.edu.agh.to.backendspringboot.presentation.consulting_room.dto.ConsultingRoomBriefResponse;
import pl.edu.agh.to.backendspringboot.presentation.doctor.dto.DoctorBriefResponse;

import java.time.LocalDate;
import java.time.LocalTime;

public class AvailabilityResponse {
    private DoctorBriefResponse doctor;

    private ConsultingRoomBriefResponse consultingRoom;

    private LocalTime visitStart;

    private LocalTime visitEnd;

    public AvailabilityResponse(DoctorBriefResponse doctor, ConsultingRoomBriefResponse consultingRoom, LocalTime visitStart, LocalTime visitEnd) {
        this.doctor = doctor;
        this.consultingRoom = consultingRoom;
        this.visitStart = visitStart;
        this.visitEnd = visitEnd;
    }

    public LocalTime getVisitStart() {
        return visitStart;
    }

    public LocalTime getVisitEnd() {
        return visitEnd;
    }

    public DoctorBriefResponse getDoctor() {
        return doctor;
    }

}
