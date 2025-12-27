package pl.edu.agh.to.backendspringboot.presentation.consulting_room;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import pl.edu.agh.to.backendspringboot.application.consulting_room.ConsultingRoomService;
import pl.edu.agh.to.backendspringboot.domain.doctor.exception.InvalidMedicalSpecialization;
import pl.edu.agh.to.backendspringboot.presentation.consulting_room.dto.ConsultingRoomBriefResponse;
import pl.edu.agh.to.backendspringboot.presentation.consulting_room.dto.ConsultingRoomDetailResponse;
import pl.edu.agh.to.backendspringboot.presentation.consulting_room.dto.ConsultingRoomRequest;

import java.util.List;

@RestController
@RequestMapping("consulting-room")
@CrossOrigin(origins = "http://localhost:3000")
@Tag(name = "Consulting Room Controller", description = "API do zarządzania danymi gabinetu lekarskiego")
public class ConsultingRoomController {
    private final ConsultingRoomService consultingRoomService;
    /**
     * Konstruktor kontrolera wstrzykujący zależność serwisu gabientów lekarskich.
     *
     * @param consultingRoomService Serwis zawierający logikę biznesową dotyczącą gabientów lekarskich.
     */
    public ConsultingRoomController(ConsultingRoomService consultingRoomService){
        this.consultingRoomService = consultingRoomService;
    }

    /**
     * Dodaje nowy gabinet lekraski do systemu.
     * <p>
     * Metoda waliduje poprawność danych wejściowych. Jeśli specjalizacja medyczna
     * jest nieprawidłowa, zwracany jest błąd 400 Bad Request.
     *
     * @param consultingRoomRequest Obiekt DTO zawierający dane wymagane do utworzenia lekarza.
     * @throws ResponseStatusException (HttpStatus.BAD_REQUEST) jeśli podana specjalizacja jest nieprawidłowa.
     */
    @Operation(
            summary = "Dodaj nowy gabinet lekarski",
            description = "Tworzy rekord gabinetu lekarskiego w bazie danych. " +
                    "Wymaga podania numer pokoju, reszta pól opcjonalne. Umożliwia określenie dostępnego sprzętu medycznego w pomieszczeniu. "+
                    "Dostępne opcjonalne pola: hasExaminationBed (Czy gabinet ma łóżko do badań), " +
                    "hasECGMachine (Czy gabinet ma aparat EKG), " +
                    "hasScale (Czy gabinet ma wagę), " +
                    "hasThermometer (Czy gabinet ma termometr), " +
                    "hasDiagnosticSet (Zestaw diagnostyczny (otoskop, oftalmoskop))"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Gabinet lekarski został pomyślnie dodany"),
            @ApiResponse(responseCode = "400", description = "Błąd walidacji danych",
                    content = @Content)
    })
    @PostMapping
    public void addConsultingRoom(@Valid @RequestBody ConsultingRoomRequest consultingRoomRequest){
        try {
            consultingRoomService.addConsultingRoom(consultingRoomRequest);
        }catch(InvalidMedicalSpecialization e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getLocalizedMessage());
        }
    }

    @GetMapping
    public List<ConsultingRoomBriefResponse> getConsultingRooms(){
        return consultingRoomService.getConsultingRooms();
    }

    @DeleteMapping("/{id}")
    public void deleteConsultingRoomById(@PathVariable Integer id){
        consultingRoomService.deleteConsultingRoomById(id);
    }


}
