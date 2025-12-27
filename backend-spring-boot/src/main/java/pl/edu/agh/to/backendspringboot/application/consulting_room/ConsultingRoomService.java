package pl.edu.agh.to.backendspringboot.application.consulting_room;

import org.springframework.stereotype.Service;
import pl.edu.agh.to.backendspringboot.domain.consulting_room.exception.ConsultingRoomNumberAlreadyExistsException;
import pl.edu.agh.to.backendspringboot.domain.consulting_room.exception.ConsultingRoomNotFoundException;
import pl.edu.agh.to.backendspringboot.infrastructure.consulting_room.ConsultingRoomRepository;
import pl.edu.agh.to.backendspringboot.presentation.consulting_room.dto.ConsultingRoomBriefResponse;
import pl.edu.agh.to.backendspringboot.presentation.consulting_room.dto.ConsultingRoomDetailResponse;
import pl.edu.agh.to.backendspringboot.presentation.consulting_room.dto.ConsultingRoomRequest;

import java.util.List;

/**
 * Serwis realizujący logikę biznesową związaną z zarządzaniem gabinetami lekarskimi.
 * Odpowiada za weryfikację unikalności gabinetów, ich tworzenie, usuwanie
 * oraz pobieranie danych prostych i szczegółowych.
 */
@Service
public class ConsultingRoomService {
    private final ConsultingRoomRepository consultingRoomRepository;

    /**
     * Konstruktor serwisu wstrzykujący zależność repozytorium.
     *
     * @param consultingRoomRepository Repozytorium umożliwiające operacje na bazie danych gabinetów.
     */
    public ConsultingRoomService(ConsultingRoomRepository consultingRoomRepository){
        this.consultingRoomRepository = consultingRoomRepository;
    }

    /**
     * Tworzy i zapisuje nowy gabinet lekarski w systemie.
     * Przed zapisem weryfikuje, czy gabinet o podanym numerze już istnieje.
     *
     * @param consultingRoomRequest Obiekt DTO zawierający dane niezbędne do utworzenia gabinetu.
     * @throws ConsultingRoomNumberAlreadyExistsException jeśli gabinet o podanym numerze już istnieje w bazie.
     */
    public void addConsultingRoom(ConsultingRoomRequest consultingRoomRequest) {
        if (consultingRoomRepository.existsByRoomNumber(consultingRoomRequest.roomNumber())) {
            throw new ConsultingRoomNumberAlreadyExistsException(
                    "Consulting room " + consultingRoomRequest.roomNumber() + " already exists."
            );
        }

        consultingRoomRepository.save(consultingRoomRequest.toEntity());
    }

    /**
     * Pobiera listę wszystkich gabinetów w formacie skróconym.
     * Dane są pobierane z repozytorium (projekcja), a następnie mapowane na obiekty {@link ConsultingRoomBriefResponse}.
     *
     * @return Lista skróconych informacji o gabinetach.
     */
    public List<ConsultingRoomBriefResponse> getConsultingRooms(){
        return consultingRoomRepository.findConsultingRoomsBrief().stream()
                .map(ConsultingRoomBriefResponse::from).toList();
    }

    /**
     * Pobiera szczegółowe informacje o gabinecie na podstawie jego identyfikatora.
     * W skład szczegółów wchodzi również harmonogram przypisany do danego gabinetu.
     *
     * @param id Unikalny identyfikator gabinetu.
     * @return Obiekt {@link ConsultingRoomDetailResponse} zawierający pełne dane gabinetu i jego grafiku.
     * @throws ConsultingRoomNotFoundException jeśli gabinet o podanym identyfikatorze nie zostanie znaleziony.
     */
    public ConsultingRoomDetailResponse getConsultingRoomDetailResponse(Integer id){
        return consultingRoomRepository.findByIdWithSchedules(id).map(ConsultingRoomDetailResponse::from)
                .orElseThrow(()->new ConsultingRoomNotFoundException("Consulting room with id " + id + " not found"));
    }

    /**
     * Usuwa gabinet z systemu na podstawie jego identyfikatora.
     * Przed usunięciem następuje weryfikacja, czy gabinet o danym ID istnieje.
     *
     * @param id Unikalny identyfikator gabinetu do usunięcia.
     * @throws ConsultingRoomNotFoundException jeśli gabinet o podanym identyfikatorze nie istnieje.
     */
    public void deleteConsultingRoomById(Integer id) {
        if (!consultingRoomRepository.existsById(id)) {
            throw new ConsultingRoomNotFoundException("Consulting room with id " + id + " not found");
        }
        consultingRoomRepository.deleteById(id);
    }
}