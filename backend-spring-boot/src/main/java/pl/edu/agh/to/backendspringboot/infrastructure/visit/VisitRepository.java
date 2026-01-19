package pl.edu.agh.to.backendspringboot.infrastructure.visit;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.edu.agh.to.backendspringboot.domain.visit.*;

import java.time.LocalDateTime;
import java.util.List;

public interface VisitRepository extends JpaRepository<Visit, Integer> {
    @Query("SELECT v FROM Visit v WHERE v.id = :id")
    public VisitDetail findById(int id);

    @Query("SELECT v FROM Visit v")
    public List<VisitBrief> findAllVisits();

    @Query("SELECT CASE WHEN COUNT(v) > 0 THEN true ELSE false END FROM Visit v " +
            "WHERE ( (v.visitStart < :visitEnd AND v.visitEnd > :visitStart) AND v.doctor.id = :DoctorId )")
    public boolean collidingVisitExist(LocalDateTime visitStart, LocalDateTime visitEnd, int DoctorId);

    @Query("SELECT v FROM Visit v WHERE v.patient.id = :patientId")
    public List<VisitBriefPatient> findAllByPatientId(int patientId);

    @Query("SELECT v FROM Visit v WHERE v.doctor.id = :doctorId")
    public List<VisitBriefDoctor> findAllByDoctorId(int doctorId);

    @Query("SELECT CASE WHEN COUNT(v) > 0 THEN true ELSE false END FROM Visit v WHERE v.patient.id = :patientId")
    public boolean visitsExistForPatient(int patientId);
    @Query("SELECT CASE WHEN COUNT(v) > 0 THEN true ELSE false END FROM Visit v " +
            "WHERE v.doctor.id = :doctorId AND " +
            "(v.visitStart >= :shiftStart AND v.visitEnd <= :shiftEnd)")
    public boolean visitExistsForSchedule(int doctorId, LocalDateTime shiftStart, LocalDateTime shiftEnd);

    @Query("SELECT CASE WHEN COUNT(v) > 0 THEN true ELSE false END FROM Visit v " +
            "WHERE v.doctor.id = :doctorId  AND " +
            "(v.visitStart < :visitEnd AND v.visitEnd > :visitStart)")
    public boolean visitAlreadyExistsForDoctor(int doctorId, LocalDateTime visitStart, LocalDateTime visitEnd);

    @Query("SELECT CASE WHEN COUNT(v) > 0 THEN true ELSE false END FROM Visit v " +
            "WHERE v.consultingRoom.id = :consultingRoomId AND " +
            "(v.visitStart < :visitEnd AND v.visitEnd > :visitStart)")
    public boolean visitAlreadyExistsForConsultingRoom(int consultingRoomId, LocalDateTime visitStart, LocalDateTime visitEnd);

    @Query("SELECT CASE WHEN COUNT(v) > 0 THEN true ELSE false END FROM Visit v " +
            "WHERE v.patient.id = :patientId AND " +
            "(v.visitStart < :visitEnd AND v.visitEnd > :visitStart)")
    public boolean visitAlreadyExistsForPatient(int patientId, LocalDateTime visitStart, LocalDateTime visitEnd);



}
