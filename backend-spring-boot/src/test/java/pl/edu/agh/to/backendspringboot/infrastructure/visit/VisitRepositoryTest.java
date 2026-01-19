package pl.edu.agh.to.backendspringboot.infrastructure.visit;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import pl.edu.agh.to.backendspringboot.domain.consulting_room.model.ConsultingRoom;
import pl.edu.agh.to.backendspringboot.domain.consulting_room.model.MedicalFacilities;
import pl.edu.agh.to.backendspringboot.domain.doctor.model.Doctor;
import pl.edu.agh.to.backendspringboot.domain.doctor.model.MedicalSpecialization;
import pl.edu.agh.to.backendspringboot.domain.patient.model.Patient;
import pl.edu.agh.to.backendspringboot.domain.shared.model.Address;
import pl.edu.agh.to.backendspringboot.domain.visit.Visit;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class VisitRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private VisitRepository visitRepository;

    private final LocalDate TEST_DATE = LocalDate.of(2025, 1, 20);

    @Test
    void shouldDetectCollidingVisitForDoctor() {
        Doctor doc = new Doctor("Jan", "L", "1", new Address(), MedicalSpecialization.CARDIOLOGY);
        Patient pat = new Patient("Jan", "P", "2", new Address());
        ConsultingRoom room = new ConsultingRoom("101", new MedicalFacilities(true, false, false, false, false));
        entityManager.persist(doc);
        entityManager.persist(pat);
        entityManager.persist(room);

        LocalDateTime start = LocalDateTime.of(TEST_DATE, LocalTime.of(10, 0));
        LocalDateTime end = LocalDateTime.of(TEST_DATE, LocalTime.of(10, 30));

        entityManager.persist(new Visit(pat, doc, start, end, room));
        entityManager.flush();

        boolean exists = visitRepository.collidingVisitExist(start, end, doc.getId());
        assertThat(exists).isTrue();
    }

    @Test
    void shouldCheckVisitsForPatient() {
        Doctor doc = new Doctor("Jan", "L", "1", new Address(), MedicalSpecialization.CARDIOLOGY);
        Patient pat = new Patient("Jan", "P", "2", new Address());
        ConsultingRoom room = new ConsultingRoom("101", new MedicalFacilities(true, false, false, false, false));
        entityManager.persist(doc);
        entityManager.persist(pat);
        entityManager.persist(room);

        LocalDateTime start = LocalDateTime.of(TEST_DATE, LocalTime.of(10, 0));
        LocalDateTime end = LocalDateTime.of(TEST_DATE, LocalTime.of(10, 30));

        entityManager.persist(new Visit(pat, doc, start, end, room));
        entityManager.flush();

        assertThat(visitRepository.visitsExistForPatient(pat.getId())).isTrue();
    }

    @Test
    void shouldCheckVisitForSchedule() {
        Doctor doc = new Doctor("Jan", "L", "1", new Address(), MedicalSpecialization.CARDIOLOGY);
        Patient pat = new Patient("Jan", "P", "2", new Address());
        ConsultingRoom room = new ConsultingRoom("101", new MedicalFacilities(true, false, false, false, false));
        entityManager.persist(doc);
        entityManager.persist(pat);
        entityManager.persist(room);

        LocalDateTime visitStart = LocalDateTime.of(TEST_DATE, LocalTime.of(9, 0));
        LocalDateTime visitEnd = LocalDateTime.of(TEST_DATE, LocalTime.of(9, 30));

        entityManager.persist(new Visit(pat, doc, visitStart, visitEnd, room));
        entityManager.flush();

        LocalDateTime scheduleStart = LocalDateTime.of(TEST_DATE, LocalTime.of(8, 0));
        LocalDateTime scheduleEnd = LocalDateTime.of(TEST_DATE, LocalTime.of(12, 0));

        assertThat(visitRepository.visitExistsForSchedule(doc.getId(), scheduleStart, scheduleEnd)).isTrue();
    }
}