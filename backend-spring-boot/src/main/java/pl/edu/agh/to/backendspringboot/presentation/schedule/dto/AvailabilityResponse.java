package pl.edu.agh.to.backendspringboot.presentation.schedule.dto;

import pl.edu.agh.to.backendspringboot.presentation.consulting_room.dto.ConsultingRoomBriefResponse;
import pl.edu.agh.to.backendspringboot.presentation.doctor.dto.DoctorBriefResponse;

import java.util.List;

public record AvailabilityResponse(
        List<DoctorBriefResponse> doctors,
        List<ConsultingRoomBriefResponse> consultingRooms
) {
}
