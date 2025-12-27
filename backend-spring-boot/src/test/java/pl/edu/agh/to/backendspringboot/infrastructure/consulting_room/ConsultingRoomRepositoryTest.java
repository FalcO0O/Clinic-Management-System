package pl.edu.agh.to.backendspringboot.infrastructure.consulting_room;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import pl.edu.agh.to.backendspringboot.domain.consulting_room.model.ConsultingRoom;
import pl.edu.agh.to.backendspringboot.domain.consulting_room.model.ConsultingRoomBrief;
import pl.edu.agh.to.backendspringboot.domain.consulting_room.model.MedicalFacilities;
import pl.edu.agh.to.backendspringboot.domain.doctor.model.Address;
import pl.edu.agh.to.backendspringboot.domain.doctor.model.Doctor;
import pl.edu.agh.to.backendspringboot.domain.doctor.model.MedicalSpecialization;
import pl.edu.agh.to.backendspringboot.domain.schedule.model.Schedule;

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

    @Test
    void shouldReturnTrue_WhenRoomNumberExists() {
        // given
        ConsultingRoom room = new ConsultingRoom("101", new MedicalFacilities(true, false, false, false, false));
        entityManager.persist(room);

        // when
        boolean exists = consultingRoomRepository.existsByRoomNumber("101");

        // then
        assertThat(exists).isTrue();
    }

    @Test
    void shouldReturnFalseWhenRoomNumberDoesNotExist() {
        // given
        // database is empty

        // when
        boolean exists = consultingRoomRepository.existsByRoomNumber("999");

        // then
        assertThat(exists).isFalse();
    }

    @Test
    void shouldReturnConsultingRoomsBrief() {
        // given
        ConsultingRoom room1 = new ConsultingRoom("101", new MedicalFacilities(true, true, true, true, true));
        ConsultingRoom room2 = new ConsultingRoom("102", new MedicalFacilities(false, false, false, false, false));

        entityManager.persist(room1);
        entityManager.persist(room2);

        // when
        List<ConsultingRoomBrief> result = consultingRoomRepository.findConsultingRoomsBrief();

        // then
        assertThat(result).hasSize(2);

        // Weryfikacja przykładowego elementu
        assertThat(result).extracting(ConsultingRoomBrief::getRoomNumber)
                .containsExactlyInAnyOrder("101", "102");
    }

    @Test
    void shouldFindByIdWithSchedules() {
        // given
        // Tworzymy Lekarza
        Address address = new Address("Jana Pawla II","Krakow","35-323");
        Doctor doctor = new Doctor("Jan", "Kowalski","05237401211",address, MedicalSpecialization.CARDIOLOGY);
        entityManager.persist(doctor);

        //Tworzymy Gabinet
        ConsultingRoom room = new ConsultingRoom("202", new MedicalFacilities(true, false, false, false, false));
        entityManager.persist(room);

        // Tworzymy grafik dyżurów
        Schedule schedule = new Schedule(doctor, room, LocalTime.of(8, 0), LocalTime.of(10, 0));
        entityManager.persist(schedule);

        // Czyścimy cache entity managera, aby wymusić pobranie danych z bazy (SELECT)
        entityManager.flush();
        entityManager.clear();

        // when
        Optional<ConsultingRoom> foundRoom = consultingRoomRepository.findByIdWithSchedules(room.getId());

        // then
        assertThat(foundRoom).isPresent();
        ConsultingRoom result = foundRoom.get();

        assertThat(result.getRoomNumber()).isEqualTo("202");

        // Sprawdzamy czy relacja schedules została załadowana
        assertThat(result.getSchedules()).hasSize(1);

        // Sprawdzamy czy wewnątrz grafiku załadowano lekarza
        Schedule loadedSchedule = result.getSchedules().iterator().next();
        assertThat(loadedSchedule.getDoctor()).isNotNull();
        assertThat(loadedSchedule.getDoctor().getFirstName()).isEqualTo("Jan");
    }

    @Test
    void shouldFindByIdWithSchedulesWhenNoSchedulesExist() {
        // given
        ConsultingRoom room = new ConsultingRoom("303", new MedicalFacilities(false, false, false, false, false));
        entityManager.persist(room);

        entityManager.flush();
        entityManager.clear();

        // when
        Optional<ConsultingRoom> foundRoom = consultingRoomRepository.findByIdWithSchedules(room.getId());

        // then
        assertThat(foundRoom).isPresent();
        assertThat(foundRoom.get().getSchedules()).isEmpty();
    }
}