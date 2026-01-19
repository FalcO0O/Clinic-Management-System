package pl.edu.agh.to.backendspringboot;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import pl.edu.agh.to.backendspringboot.domain.consulting_room.model.ConsultingRoom;
import pl.edu.agh.to.backendspringboot.domain.consulting_room.model.MedicalFacilities;
import pl.edu.agh.to.backendspringboot.domain.patient.model.Patient;
import pl.edu.agh.to.backendspringboot.domain.schedule.model.Schedule;
import pl.edu.agh.to.backendspringboot.domain.shared.model.Address;
import pl.edu.agh.to.backendspringboot.infrastructure.consulting_room.ConsultingRoomRepository;
import pl.edu.agh.to.backendspringboot.infrastructure.doctor.DoctorRepository;
import pl.edu.agh.to.backendspringboot.domain.doctor.model.Doctor;
import pl.edu.agh.to.backendspringboot.domain.doctor.model.MedicalSpecialization;
import pl.edu.agh.to.backendspringboot.domain.visit.Visit;
import pl.edu.agh.to.backendspringboot.infrastructure.patient.PatientRepository;
import pl.edu.agh.to.backendspringboot.infrastructure.schedule.ScheduleRepository;
import pl.edu.agh.to.backendspringboot.infrastructure.visit.VisitRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class DatabaseInitializer {

    static void main(String[] args) {
        System.out.println("Starting Database Initialization...");

        ConfigurableApplicationContext context = new SpringApplicationBuilder(BackendSpringBootApplication.class)
                .web(WebApplicationType.NONE)
                .run(args);

        DoctorRepository doctorRepository = context.getBean(DoctorRepository.class);
        ConsultingRoomRepository consultingRoomRepository = context.getBean(ConsultingRoomRepository.class);
        ScheduleRepository scheduleRepository = context.getBean(ScheduleRepository.class);
        PatientRepository patientRepository = context.getBean(PatientRepository.class);
        VisitRepository visitRepository = context.getBean(VisitRepository.class);

        visitRepository.deleteAll();
        scheduleRepository.deleteAll();
        doctorRepository.deleteAll();
        patientRepository.deleteAll();
        consultingRoomRepository.deleteAll();

        var doc1 = doctorRepository.save(new Doctor(
                "John", "Doe", "12345678901",
                new Address("Main Street", "Springfield", "12-345"),
                MedicalSpecialization.CARDIOLOGY
        ));

        var doc2 = doctorRepository.save(new Doctor(
                "Jane", "Smith", "98765432109",
                new Address("Second Street", "Shelbyville", "98-765"),
                MedicalSpecialization.CARDIOLOGY
        ));

        var doc3 = doctorRepository.save(new Doctor(
                "Emily", "Johnson", "45678912345",
                new Address("Third Avenue", "Ogdenville", "56-789"),
                MedicalSpecialization.CARDIOLOGY));

        var doc4 = doctorRepository.save(new Doctor(
                "Michael", "Brown", "65432198765",
                new Address("Fourth Boulevard", "North Haverbrook", "65-432"),
                MedicalSpecialization.DERMATOLOGY));

        doctorRepository.save(new Doctor("Sarah", "Davis", "78912345678", new Address("Fifth Lane", "Capital City", "78-912"), MedicalSpecialization.DERMATOLOGY));
        doctorRepository.save(new Doctor("David", "Wilson", "32165498765", new Address("Sixth Road", "Cypress Creek", "32-165"), MedicalSpecialization.ALLERGOLOGY));
        doctorRepository.save(new Doctor("Laura", "Miller", "14725836901", new Address("Seventh Street", "Brockway", "14-725"), MedicalSpecialization.GENERAL_SURGERY));

        var room101 = consultingRoomRepository.save(new ConsultingRoom(
                "101",
                new MedicalFacilities(true, true, true, true, true)
        ));

        var room102 = consultingRoomRepository.save(new ConsultingRoom(
                "102",
                new MedicalFacilities(true, false, true, true, false)
        ));

        var room201 = consultingRoomRepository.save(new ConsultingRoom(
                "201",
                new MedicalFacilities(true, true, false, false, true)
        ));

        LocalDate today = LocalDate.now();

        scheduleRepository.save(new Schedule(doc1, room101, LocalDateTime.of(today, LocalTime.of(8, 0)), LocalDateTime.of(today, LocalTime.of(12, 0))));
        scheduleRepository.save(new Schedule(doc2, room102, LocalDateTime.of(today, LocalTime.of(8, 0)), LocalDateTime.of(today, LocalTime.of(12, 0))));
        scheduleRepository.save(new Schedule(doc3, room101, LocalDateTime.of(today, LocalTime.of(13, 0)), LocalDateTime.of(today, LocalTime.of(17, 0))));
        scheduleRepository.save(new Schedule(doc4, room201, LocalDateTime.of(today, LocalTime.of(9, 0)), LocalDateTime.of(today, LocalTime.of(15, 0))));

        var p1 = patientRepository.save(new Patient(
                "Jan", "Kowalski", "90010112345",
                new Address("Długa 5", "Kraków", "31-123")
        ));

        var p2 = patientRepository.save(new Patient(
                "Anna", "Nowak", "85050554321",
                new Address("Krótka 10", "Warszawa", "00-999")
        ));

        var p3 = patientRepository.save(new Patient(
                "Tomasz", "Zieliński", "76121209876",
                new Address("Rynek 1", "Wrocław", "50-101")
        ));

        var p4 = patientRepository.save(new Patient(
                "Magdalena", "Wójcik", "99030311223",
                new Address("Polna 7", "Gdańsk", "80-001")
        ));

        patientRepository.save(new Patient(
                "Piotr", "Mazur", "92081512345",
                new Address("Słoneczna 12", "Poznań", "60-001")
        ));

        patientRepository.save(new Patient(
                "Katarzyna", "Krawczyk", "88101054321",
                new Address("Lipowa 3", "Lublin", "20-111")
        ));

        visitRepository.save(new Visit(p1, doc1, LocalDateTime.of(today, LocalTime.of(8, 0)), LocalDateTime.of(today, LocalTime.of(8, 30)), room101));
        visitRepository.save(new Visit(p2, doc1, LocalDateTime.of(today, LocalTime.of(8, 30)), LocalDateTime.of(today, LocalTime.of(9, 0)), room101));
        visitRepository.save(new Visit(p3, doc2, LocalDateTime.of(today, LocalTime.of(9, 0)), LocalDateTime.of(today, LocalTime.of(9, 30)), room102));
        visitRepository.save(new Visit(p4, doc4, LocalDateTime.of(today, LocalTime.of(10, 0)), LocalDateTime.of(today, LocalTime.of(10, 45)), room201));

        System.out.println("Database initialized successfully");
    }
}