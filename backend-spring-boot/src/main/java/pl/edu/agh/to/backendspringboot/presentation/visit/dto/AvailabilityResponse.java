package pl.edu.agh.to.backendspringboot.presentation.visit.dto;


import pl.edu.agh.to.backendspringboot.presentation.consulting_room.dto.ConsultingRoomBriefResponse;
import pl.edu.agh.to.backendspringboot.presentation.doctor.dto.DoctorBriefResponse;

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

    public ConsultingRoomBriefResponse getConsultingRoom() {
        return consultingRoom;
    }

}
