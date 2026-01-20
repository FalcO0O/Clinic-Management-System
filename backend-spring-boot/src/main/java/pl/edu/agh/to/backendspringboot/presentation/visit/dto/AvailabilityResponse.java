package pl.edu.agh.to.backendspringboot.presentation.visit.dto;


import pl.edu.agh.to.backendspringboot.presentation.consulting_room.dto.ConsultingRoomBriefResponse;
import pl.edu.agh.to.backendspringboot.presentation.doctor.dto.DoctorBriefResponse;

import java.time.LocalDateTime;

public class AvailabilityResponse {
    private DoctorBriefResponse doctor;

    private ConsultingRoomBriefResponse consultingRoom;

    private LocalDateTime visitStart;

    private LocalDateTime visitEnd;

    public AvailabilityResponse(DoctorBriefResponse doctor, ConsultingRoomBriefResponse consultingRoom, LocalDateTime visitStart, LocalDateTime visitEnd) {
        this.doctor = doctor;
        this.consultingRoom = consultingRoom;
        this.visitStart = visitStart;
        this.visitEnd = visitEnd;
    }

    public LocalDateTime getVisitStart() {
        return visitStart;
    }

    public LocalDateTime getVisitEnd() {
        return visitEnd;
    }

    public DoctorBriefResponse getDoctor() {
        return doctor;
    }

    public ConsultingRoomBriefResponse getConsultingRoom() {
        return consultingRoom;
    }

}
