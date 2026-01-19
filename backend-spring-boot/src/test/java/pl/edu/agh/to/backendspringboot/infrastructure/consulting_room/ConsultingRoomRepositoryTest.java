package pl.edu.agh.to.backendspringboot.infrastructure.consulting_room;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import pl.edu.agh.to.backendspringboot.domain.consulting_room.model.ConsultingRoom;
import pl.edu.agh.to.backendspringboot.domain.consulting_room.model.ConsultingRoomBrief;
import pl.edu.agh.to.backendspringboot.domain.consulting_room.model.MedicalFacilities;
import pl.edu.agh.to.backendspringboot.domain.doctor.model.Doctor;
import pl.edu.agh.to.backendspringboot.domain.doctor.model.MedicalSpecialization;
import pl.edu.agh.to.backendspringboot.domain.schedule.model.Schedule;
import pl.edu.agh.to.backendspringboot.domain.shared.model.Address;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ConsultingRoomRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ConsultingRoomRepository consultingRoomRepository;

    private final LocalDate TEST_DATE = LocalDate.of(2025, 1, 20);

    @Test
    void shouldReturnTrue_WhenRoomNumberExists() {
        ConsultingRoom room = new ConsultingRoom("101", new MedicalFacilities(true, false, false, false, false));
        entityManager.persist(room);

        boolean exists = consultingRoomRepository.existsByRoomNumber("101");

        assertThat(exists).isTrue();
    }

    @Test
    void shouldReturnFalseWhenRoomNumberDoesNotExist() {
        boolean exists = consultingRoomRepository.existsByRoomNumber("999");

        assertThat(exists).isFalse();
    }

    @Test
    void shouldReturnConsultingRoomsBrief() {
        ConsultingRoom room1 = new ConsultingRoom("101", new MedicalFacilities(true, true, true, true, true));
        ConsultingRoom room2 = new ConsultingRoom("102", new MedicalFacilities(false, false, false, false, false));

        entityManager.persist(room1);
        entityManager.persist(room2);

        List<ConsultingRoomBrief> result = consultingRoomRepository.findConsultingRoomsBrief();

        assertThat(result).hasSize(2);
        assertThat(result).extracting(ConsultingRoomBrief::getRoomNumber)
                .containsExactlyInAnyOrder("101", "102");
    }

    @Test
    void shouldFindByIdWithSchedules() {
        Address address = new Address("Jana Pawla II", "Krakow", "35-323");
        Doctor doctor = new Doctor("Jan", "Kowalski", "05237401211", address, MedicalSpecialization.CARDIOLOGY);
        entityManager.persist(doctor);

        ConsultingRoom room = new ConsultingRoom("202", new MedicalFacilities(true, false, false, false, false));
        entityManager.persist(room);

        Schedule schedule = new Schedule(
                doctor,
                room,
                LocalDateTime.of(TEST_DATE, LocalTime.of(8, 0)),
                LocalDateTime.of(TEST_DATE, LocalTime.of(10, 0))
        );
        entityManager.persist(schedule);

        entityManager.flush();
        entityManager.clear();

        Optional<ConsultingRoom> foundRoom = consultingRoomRepository.findByIdWithSchedules(room.getId());

        assertThat(foundRoom).isPresent();
        ConsultingRoom result = foundRoom.get();

        assertThat(result.getRoomNumber()).isEqualTo("202");
        assertThat(result.getSchedules()).hasSize(1);

        Schedule loadedSchedule = result.getSchedules().iterator().next();
        assertThat(loadedSchedule.getDoctor()).isNotNull();
        assertThat(loadedSchedule.getDoctor().getFirstName()).isEqualTo("Jan");
    }

    @Test
    void shouldFindByIdWithSchedulesWhenNoSchedulesExist() {
        ConsultingRoom room = new ConsultingRoom("303", new MedicalFacilities(false, false, false, false, false));
        entityManager.persist(room);

        entityManager.flush();
        entityManager.clear();

        Optional<ConsultingRoom> foundRoom = consultingRoomRepository.findByIdWithSchedules(room.getId());

        assertThat(foundRoom).isPresent();
        assertThat(foundRoom.get().getSchedules()).isEmpty();
    }
}