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

        var doc5 = doctorRepository.save(new Doctor(
                "Sarah", "Davis", "78912345678",
                new Address("Fifth Lane", "Capital City", "78-912"),
                MedicalSpecialization.DERMATOLOGY
        ));

        var doc6 = doctorRepository.save(new Doctor(
                "David", "Wilson", "32165498765",
                new Address("Sixth Road", "Cypress Creek", "32-165"),
                MedicalSpecialization.ALLERGOLOGY
        ));

        var doc7 = doctorRepository.save(new Doctor(
                "Laura", "Miller", "14725836901",
                new Address("Seventh Street", "Brockway", "14-725"),
                MedicalSpecialization.GENERAL_SURGERY
        ));

        // Dodatkowi lekarze do testowania filtrowania po specjalizacji i różnych czasów wizyt
        var doc8 = doctorRepository.save(new Doctor(
                "Adam", "Nowicki", "11122233344",
                new Address("Kasztanowa 2", "Kraków", "30-001"),
                MedicalSpecialization.INTERNAL_MEDICINE
        ));

        var doc9 = doctorRepository.save(new Doctor(
                "Ewa", "Kowalczyk", "22233344455",
                new Address("Jesionowa 9", "Warszawa", "00-101"),
                MedicalSpecialization.FAMILY_MEDICINE
        ));

        var doc10 = doctorRepository.save(new Doctor(
                "Marta", "Lis", "33344455566",
                new Address("Modrzewiowa 14", "Gdańsk", "80-201"),
                MedicalSpecialization.PEDIATRICS
        ));

        var doc11 = doctorRepository.save(new Doctor(
                "Paweł", "Kaczmarek", "44455566677",
                new Address("Sosnowa 6", "Wrocław", "50-222"),
                MedicalSpecialization.NEUROLOGY
        ));

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

        // Dodatkowe gabinety do testów
        var room103 = consultingRoomRepository.save(new ConsultingRoom(
                "103",
                new MedicalFacilities(true, true, true, false, true)
        ));

        var room104 = consultingRoomRepository.save(new ConsultingRoom(
                "104",
                new MedicalFacilities(false, false, true, true, false)
        ));

        var room202 = consultingRoomRepository.save(new ConsultingRoom(
                "202",
                new MedicalFacilities(true, false, false, true, true)
        ));

        var room203 = consultingRoomRepository.save(new ConsultingRoom(
                "203",
                new MedicalFacilities(true, true, true, true, true)
        ));

        // Uwaga: serwisy walidują, że nie można planować w przeszłości.
        // Seed ustawiamy od jutra, żeby dało się od razu testować umawianie wizyt i sprawdzanie dostępności.
        LocalDate baseDay = LocalDate.now().plusDays(1);
        LocalDate day1 = baseDay;
        LocalDate day2 = baseDay.plusDays(1);
        LocalDate day3 = baseDay.plusDays(2);
        LocalDate day4 = baseDay.plusDays(3);
        LocalDate day5 = baseDay.plusDays(4);
                LocalDate day6 = baseDay.plusDays(5);
                LocalDate day7 = baseDay.plusDays(6);

        // ===== Dyżury (wiele dni, różne gabinety/specjalizacje) =====
                // Krótsze dyżury, ale na większej liczbie dni (wciąż w limicie maxDaysInAdvance=7)
                // CARDIOLOGY (2h dziennie)
                for (LocalDate d : new LocalDate[]{day1, day2, day3, day4, day5, day6, day7}) {
                        scheduleRepository.save(new Schedule(doc1, room101, LocalDateTime.of(d, LocalTime.of(8, 0)), LocalDateTime.of(d, LocalTime.of(10, 0))));
                        scheduleRepository.save(new Schedule(doc2, room102, LocalDateTime.of(d, LocalTime.of(10, 0)), LocalDateTime.of(d, LocalTime.of(12, 0))));
                        scheduleRepository.save(new Schedule(doc3, room103, LocalDateTime.of(d, LocalTime.of(13, 0)), LocalDateTime.of(d, LocalTime.of(15, 0))));
                }

                // DERMATOLOGY (15 min) - 2h dziennie
                for (LocalDate d : new LocalDate[]{day1, day2, day3, day4, day5, day6, day7}) {
                        scheduleRepository.save(new Schedule(doc4, room201, LocalDateTime.of(d, LocalTime.of(9, 0)), LocalDateTime.of(d, LocalTime.of(11, 0))));
                        scheduleRepository.save(new Schedule(doc5, room202, LocalDateTime.of(d, LocalTime.of(11, 0)), LocalDateTime.of(d, LocalTime.of(13, 0))));
                }

                // ALLERGOLOGY (45 min) - 2h dziennie
                for (LocalDate d : new LocalDate[]{day1, day2, day3, day4, day5, day6, day7}) {
                        scheduleRepository.save(new Schedule(doc6, room104, LocalDateTime.of(d, LocalTime.of(14, 0)), LocalDateTime.of(d, LocalTime.of(16, 0))));
                }

                // GENERAL_SURGERY (45 min) - 2h w wybrane dni
                for (LocalDate d : new LocalDate[]{day2, day4, day6}) {
                        scheduleRepository.save(new Schedule(doc7, room203, LocalDateTime.of(d, LocalTime.of(9, 0)), LocalDateTime.of(d, LocalTime.of(11, 0))));
                }

                // INTERNAL_MEDICINE (30 min) - 2h dziennie
                for (LocalDate d : new LocalDate[]{day1, day2, day3, day4, day5, day6, day7}) {
                        scheduleRepository.save(new Schedule(doc8, room103, LocalDateTime.of(d, LocalTime.of(16, 0)), LocalDateTime.of(d, LocalTime.of(18, 0))));
                }

                // FAMILY_MEDICINE (15 min) - 2h dziennie
                for (LocalDate d : new LocalDate[]{day1, day2, day3, day4, day5, day6, day7}) {
                        scheduleRepository.save(new Schedule(doc9, room104, LocalDateTime.of(d, LocalTime.of(8, 0)), LocalDateTime.of(d, LocalTime.of(10, 0))));
                }

                // PEDIATRICS (60 min) - 2h dziennie
                for (LocalDate d : new LocalDate[]{day1, day2, day3, day4, day5, day6, day7}) {
                        scheduleRepository.save(new Schedule(doc10, room202, LocalDateTime.of(d, LocalTime.of(8, 0)), LocalDateTime.of(d, LocalTime.of(10, 0))));
                }

                // NEUROLOGY (15 min) - 2h dziennie
                for (LocalDate d : new LocalDate[]{day1, day2, day3, day4, day5, day6, day7}) {
                        scheduleRepository.save(new Schedule(doc11, room201, LocalDateTime.of(d, LocalTime.of(12, 0)), LocalDateTime.of(d, LocalTime.of(14, 0))));
                }

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

        var p5 = patientRepository.save(new Patient(
                "Piotr", "Mazur", "92081512345",
                new Address("Słoneczna 12", "Poznań", "60-001")
        ));

        var p6 = patientRepository.save(new Patient(
                "Katarzyna", "Krawczyk", "88101054321",
                new Address("Lipowa 3", "Lublin", "20-111")
        ));

        var p7 = patientRepository.save(new Patient(
                "Agnieszka", "Szymańska", "74040467890",
                new Address("Topolowa 8", "Łódź", "90-001")
        ));

        var p8 = patientRepository.save(new Patient(
                "Michał", "Woźniak", "83090911234",
                new Address("Brzozowa 11", "Katowice", "40-101")
        ));

        // ===== Wizyty (część dyżurów ma wizyty, część zostaje pusta dla testów usuwania dyżuru) =====
        // Cardiology (30 min)
        visitRepository.save(new Visit(p1, doc1, LocalDateTime.of(day1, LocalTime.of(8, 0)), LocalDateTime.of(day1, LocalTime.of(8, 30)), room101));
        visitRepository.save(new Visit(p2, doc1, LocalDateTime.of(day1, LocalTime.of(9, 0)), LocalDateTime.of(day1, LocalTime.of(9, 30)), room101));
        visitRepository.save(new Visit(p3, doc2, LocalDateTime.of(day1, LocalTime.of(10, 0)), LocalDateTime.of(day1, LocalTime.of(10, 30)), room102));

        // Cardiology - kolejny dzień
        visitRepository.save(new Visit(p4, doc1, LocalDateTime.of(day2, LocalTime.of(9, 30)), LocalDateTime.of(day2, LocalTime.of(10, 0)), room101));
        visitRepository.save(new Visit(p5, doc2, LocalDateTime.of(day2, LocalTime.of(10, 30)), LocalDateTime.of(day2, LocalTime.of(11, 0)), room102));

        // Cardiology - jeszcze jeden dzień (żeby testować filtr zakresu dat)
        visitRepository.save(new Visit(p6, doc3, LocalDateTime.of(day3, LocalTime.of(13, 0)), LocalDateTime.of(day3, LocalTime.of(13, 30)), room103));

        // Dermatology (15 min)
        visitRepository.save(new Visit(p6, doc4, LocalDateTime.of(day1, LocalTime.of(10, 0)), LocalDateTime.of(day1, LocalTime.of(10, 15)), room201));
        visitRepository.save(new Visit(p7, doc4, LocalDateTime.of(day1, LocalTime.of(10, 30)), LocalDateTime.of(day1, LocalTime.of(10, 45)), room201));

        // Dermatology - inny lekarz
        visitRepository.save(new Visit(p8, doc5, LocalDateTime.of(day2, LocalTime.of(11, 0)), LocalDateTime.of(day2, LocalTime.of(11, 15)), room202));

        // Internal medicine (30 min)
        visitRepository.save(new Visit(p8, doc8, LocalDateTime.of(day2, LocalTime.of(16, 0)), LocalDateTime.of(day2, LocalTime.of(16, 30)), room103));

        // Pediatrics (60 min)
        visitRepository.save(new Visit(p1, doc10, LocalDateTime.of(day4, LocalTime.of(9, 0)), LocalDateTime.of(day4, LocalTime.of(10, 0)), room202));

        // Family medicine (15 min)
        visitRepository.save(new Visit(p7, doc9, LocalDateTime.of(day5, LocalTime.of(8, 0)), LocalDateTime.of(day5, LocalTime.of(8, 15)), room104));

        // Allergology (45 min)
        visitRepository.save(new Visit(p2, doc6, LocalDateTime.of(day6, LocalTime.of(14, 0)), LocalDateTime.of(day6, LocalTime.of(14, 45)), room104));

        // Neurology (15 min)
        visitRepository.save(new Visit(p3, doc11, LocalDateTime.of(day7, LocalTime.of(12, 0)), LocalDateTime.of(day7, LocalTime.of(12, 15)), room201));

        // (intencjonalnie) zostawiamy część dyżurów bez wizyt, żeby dało się testować usuwanie dyżuru.

        System.out.println("Database initialized successfully");
    }
}