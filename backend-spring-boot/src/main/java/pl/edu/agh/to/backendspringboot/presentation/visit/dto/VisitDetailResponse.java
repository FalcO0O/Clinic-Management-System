package pl.edu.agh.to.backendspringboot.presentation.visit.dto;

import pl.edu.agh.to.backendspringboot.domain.visit.VisitDetail;
import pl.edu.agh.to.backendspringboot.presentation.consulting_room.dto.ConsultingRoomBriefResponse;
import pl.edu.agh.to.backendspringboot.presentation.doctor.dto.DoctorBriefResponse;
import pl.edu.agh.to.backendspringboot.presentation.patient.dto.PatientBriefResponse;

import java.time.LocalDateTime;

public record VisitDetailResponse(
        int id,
        LocalDateTime visitStart,
        LocalDateTime visitEnd,
        ConsultingRoomBriefResponse consultingRoom,
        DoctorBriefResponse doctor,
        PatientBriefResponse patient
){
    public static VisitDetailResponse from(VisitDetail visit)
    {
        return new VisitDetailResponse(
                visit.getId(),
                visit.getVisitStart(),
                visit.getVisitEnd(),
                ConsultingRoomBriefResponse.from(visit.getConsultingRoom()),
                DoctorBriefResponse.from(visit.getDoctor()),
                PatientBriefResponse.from(visit.getPatient())
        );
    }

}
