package pl.edu.agh.to.backendspringboot.application.consulting_room;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.edu.agh.to.backendspringboot.domain.consulting_room.exception.ConsultingRoomNotFoundException;
import pl.edu.agh.to.backendspringboot.domain.consulting_room.exception.ConsultingRoomNumberAlreadyExistsException;
import pl.edu.agh.to.backendspringboot.domain.consulting_room.model.ConsultingRoom;
import pl.edu.agh.to.backendspringboot.domain.consulting_room.model.ConsultingRoomBrief;
import pl.edu.agh.to.backendspringboot.domain.consulting_room.model.MedicalFacilities;
import pl.edu.agh.to.backendspringboot.infrastructure.consulting_room.ConsultingRoomRepository;
import pl.edu.agh.to.backendspringboot.infrastructure.schedule.ScheduleRepository;
import pl.edu.agh.to.backendspringboot.presentation.consulting_room.dto.ConsultingRoomBriefResponse;
import pl.edu.agh.to.backendspringboot.presentation.consulting_room.dto.ConsultingRoomDetailResponse;
import pl.edu.agh.to.backendspringboot.presentation.consulting_room.dto.ConsultingRoomRequest;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ConsultingRoomServiceTest {

    @Mock
    private ConsultingRoomRepository consultingRoomRepository;

    @InjectMocks
    private ConsultingRoomService consultingRoomService;

    @Mock
    private ScheduleRepository scheduleRepository;

    @Test
    void shouldAddConsultingRoomWhenRoomNumberIsUnique() {
        // given
        ConsultingRoomRequest request = new ConsultingRoomRequest(
                "101", true, true, false, false, true
        );
        when(consultingRoomRepository.existsByRoomNumber("101")).thenReturn(false);

        // when
        consultingRoomService.addConsultingRoom(request);

        // then
        verify(consultingRoomRepository, times(1)).save(any(ConsultingRoom.class));
    }

    @Test
    void shouldThrowExceptionWhenAddingExistingRoomNumber() {
        // given
        ConsultingRoomRequest request = new ConsultingRoomRequest(
                "101", true, true, false, false, true
        );
        when(consultingRoomRepository.existsByRoomNumber("101")).thenReturn(true);

        // when & then
        assertThrows(ConsultingRoomNumberAlreadyExistsException.class,
                () -> consultingRoomService.addConsultingRoom(request));

        verify(consultingRoomRepository, never()).save(any());
    }


    @Test
    void shouldReturnAllConsultingRooms() {
        // given
        ConsultingRoomBrief briefMock = mock(ConsultingRoomBrief.class);
        MedicalFacilities facilitiesMock = mock(MedicalFacilities.class);

        // Konfigurujemy zachowanie mocków
        when(briefMock.getId()).thenReturn(1);
        when(briefMock.getRoomNumber()).thenReturn("101");
        when(briefMock.getMedicalFacilities()).thenReturn(facilitiesMock);

        when(facilitiesMock.isHasExaminationBed()).thenReturn(true);

        when(consultingRoomRepository.findConsultingRoomsBrief()).thenReturn(List.of(briefMock));

        // when
        List<ConsultingRoomBriefResponse> result = consultingRoomService.getConsultingRooms();

        // then
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals("101", result.get(0).roomNumber());
        assertTrue(result.get(0).hasExaminationBed());
    }


    @Test
    void shouldReturnConsultingRoomDetailWhenExists() {
        // given
        int roomId = 1;

        ConsultingRoom roomMock = mock(ConsultingRoom.class);

        when(roomMock.getId()).thenReturn(roomId);
        when(roomMock.getRoomNumber()).thenReturn("202");
        when(roomMock.getSchedules()).thenReturn(Collections.emptySet());

        when(consultingRoomRepository.findByIdWithSchedules(roomId)).thenReturn(Optional.of(roomMock));

        // when
        ConsultingRoomDetailResponse result = consultingRoomService.getConsultingRoomDetailResponse(roomId);

        // then
        assertNotNull(result);
        assertEquals(roomId, result.id());
        assertEquals("202", result.roomNumber());
        assertTrue(result.schedules().isEmpty());
    }

    @Test
    void shouldThrowExceptionWhenGettingNonExistentRoomDetails() {
        // given
        int roomId = 99;
        when(consultingRoomRepository.findByIdWithSchedules(roomId)).thenReturn(Optional.empty());

        // when & then
        assertThrows(ConsultingRoomNotFoundException.class,
                () -> consultingRoomService.getConsultingRoomDetailResponse(roomId));
    }



    @Test
    void shouldDeleteConsultingRoomWhenExists() {
        // given
        int roomId = 1;
        when(consultingRoomRepository.existsById(roomId)).thenReturn(true);

        // when
        consultingRoomService.deleteConsultingRoomById(roomId);

        // then
        verify(consultingRoomRepository, times(1)).deleteById(roomId);
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistentRoom() {
        // given
        int roomId = 99;
        when(consultingRoomRepository.existsById(roomId)).thenReturn(false);

        // when & then
        assertThrows(ConsultingRoomNotFoundException.class,
                () -> consultingRoomService.deleteConsultingRoomById(roomId));

        verify(consultingRoomRepository, never()).deleteById(anyInt());
    }
}

