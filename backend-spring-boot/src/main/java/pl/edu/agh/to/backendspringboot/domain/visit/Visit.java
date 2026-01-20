package pl.edu.agh.to.backendspringboot.domain.visit;

import jakarta.persistence.*;
import pl.edu.agh.to.backendspringboot.domain.consulting_room.model.ConsultingRoom;
import pl.edu.agh.to.backendspringboot.domain.doctor.model.Doctor;
import pl.edu.agh.to.backendspringboot.domain.patient.model.Patient;

import java.time.LocalDateTime;

@Entity
public class Visit {

    @Id
    @GeneratedValue
    private Integer id;

    @ManyToOne
    @JoinColumn(name = Columns.PATIENT_ID)
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = Columns.DOCTOR_ID)
    private Doctor doctor;

    @ManyToOne
    @JoinColumn(name = Columns.CONSULTING_ROOM_ID)
    private ConsultingRoom consultingRoom;

    @Column(name = Columns.VISIT_START)
    private LocalDateTime visitStart;
    @Column(name = Columns.VISIT_END)
    private LocalDateTime visitEnd;

    public Visit(Patient patient, Doctor doctor, LocalDateTime visitStart, LocalDateTime visitEnd, ConsultingRoom consultingRoom) {
        this.patient = patient;
        this.doctor = doctor;
        this.visitStart = visitStart;
        this.visitEnd = visitEnd;
        this.consultingRoom = consultingRoom;
    }

    public Visit() {

    }

    public static class Columns {

        public static final String VISIT_START = "visit_start";

        public static final String VISIT_END = "visit_end";

        public static final String PATIENT_ID = "patient_id";

        public static final String DOCTOR_ID = "doctor_id";

        public static final String CONSULTING_ROOM_ID = "consulting_room_id";
    }

    public Integer getId() {
        return id;
    }

    public Patient getPatient() {
        return patient;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public LocalDateTime getVisitStart() {
        return visitStart;}

    public LocalDateTime getVisitEnd() {
        return visitEnd;
    }



}
