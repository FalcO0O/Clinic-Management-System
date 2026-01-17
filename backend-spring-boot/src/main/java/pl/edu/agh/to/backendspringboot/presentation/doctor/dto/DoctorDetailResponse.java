package pl.edu.agh.to.backendspringboot.presentation.doctor.dto;

import pl.edu.agh.to.backendspringboot.domain.doctor.model.DoctorDetail;
import pl.edu.agh.to.backendspringboot.domain.schedule.model.ScheduleBrief;
import pl.edu.agh.to.backendspringboot.domain.visit.VisitBriefDoctor;
import pl.edu.agh.to.backendspringboot.presentation.schedule.dto.DoctorScheduleResponse;
import pl.edu.agh.to.backendspringboot.presentation.visit.dto.VisitBriefDoctorResponse;

import java.util.List;

public record DoctorDetailResponse(
        Integer id,
        String firstName,
        String lastName,
        String specialization,
        String pesel,
        String postalCode,
        String street,
        String city,
        List<DoctorScheduleResponse> schedules,
        List<VisitBriefDoctorResponse> visits
){
    public static DoctorDetailResponse from(DoctorDetail doctor, List<ScheduleBrief> schedules, List<VisitBriefDoctor> visits) {

        List<DoctorScheduleResponse> scheduleDtos = schedules.stream()
                .map(DoctorScheduleResponse::from)
                .toList();

        List<VisitBriefDoctorResponse> visitDtos = visits.stream()
                .map(VisitBriefDoctorResponse::from)
                .toList();

        return new DoctorDetailResponse(
                doctor.getId(),
                doctor.getFirstName(),
                doctor.getLastName(),
                doctor.getSpecialization().toString(),
                doctor.getPesel(),
                doctor.getAddress().getPostalCode(),
                doctor.getAddress().getStreet(),
                doctor.getAddress().getCity(),
                scheduleDtos,
                visitDtos
        );
    }
}