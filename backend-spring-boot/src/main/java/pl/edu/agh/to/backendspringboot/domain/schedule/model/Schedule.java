package pl.edu.agh.to.backendspringboot.domain.schedule.model;

import jakarta.persistence.*;
import pl.edu.agh.to.backendspringboot.domain.consulting_room.model.ConsultingRoom;
import pl.edu.agh.to.backendspringboot.domain.doctor.model.Doctor;

import java.time.LocalDateTime;

@Entity
public class Schedule {
    @Id
    @GeneratedValue
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = Columns.DOCTOR_ID)
    private Doctor doctor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = Columns.CONSULTING_ROOM_ID)
    private ConsultingRoom consultingRoom;

    @Column(name = Columns.SHIFT_START)
    private LocalDateTime shiftStart;
    @Column(name = Columns.SHIFT_END)
    private LocalDateTime shiftEnd;

    public Schedule(Doctor doctor, ConsultingRoom consultingRoom, LocalDateTime shiftStart, LocalDateTime shiftEnd) {
        this.doctor = doctor;
        this.consultingRoom = consultingRoom;
        this.shiftStart = shiftStart;
        this.shiftEnd = shiftEnd;
    }

    public Schedule() {
    }
    public static class Columns {

        public static final String SHIFT_START = "shift_start";

        public static final String SHIFT_END = "shift_end";

        public static final String CONSULTING_ROOM_ID = "consulting_room_id";

        public static final String DOCTOR_ID = "doctor_id";

    }

    public Doctor getDoctor() {
        return doctor;
    }

    public Integer getId() {
        return id;
    }

    public LocalDateTime getShiftEnd() {
        return shiftEnd;
    }

    public LocalDateTime getShiftStart() {
        return shiftStart;
    }

    public ConsultingRoom getConsultingRoom() {
        return consultingRoom;
    }
}
