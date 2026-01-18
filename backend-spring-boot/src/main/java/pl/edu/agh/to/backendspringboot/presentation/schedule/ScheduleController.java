package pl.edu.agh.to.backendspringboot.presentation.schedule;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import pl.edu.agh.to.backendspringboot.application.schedule.ScheduleService;
import pl.edu.agh.to.backendspringboot.domain.consulting_room.exception.ConsultingRoomNotFoundException;
import pl.edu.agh.to.backendspringboot.domain.doctor.exception.DoctorNotFoundException;
import pl.edu.agh.to.backendspringboot.domain.schedule.exception.ConflictInScheduleTimePeriod;
import pl.edu.agh.to.backendspringboot.domain.schedule.exception.InvalidScheduleTimePeriod;
import pl.edu.agh.to.backendspringboot.domain.schedule.exception.ScheduleNotFoundException;
import pl.edu.agh.to.backendspringboot.domain.schedule.exception.VisitAssignedToScheduleException;
import pl.edu.agh.to.backendspringboot.presentation.schedule.dto.AvailabilityResponse;
import pl.edu.agh.to.backendspringboot.presentation.schedule.dto.ScheduleRequest;

import java.time.LocalTime;

@RestController
@RequestMapping("schedules")
@CrossOrigin(origins = "http://localhost:3000")
@Tag(name = "Schedule Controller", description = "API do zarządzania godzinami dyżurów lekarzy w gabinetach lekarskich")
public class ScheduleController {

    private final ScheduleService scheduleService;

    /**
     * Konstruktor kontrolera wstrzykujący zależność serwisu dyżurów lekarzy.
     *
     * @param scheduleService Serwis zawierający logikę biznesową dotyczącą dyżurów lekarskich.
     */
    public ScheduleController(ScheduleService scheduleService){
        this.scheduleService = scheduleService;
    }

    /**
     * Pobiera listę dostępnych lekarzy i gabinetów w określonym przedziale czasowym.
     * <p>
     * Metoda sprawdza dostępność zasobów dla dnia dzisiejszego na podstawie podanych godzin.
     * Weryfikuje, czy w zadanym oknie czasowym lekarze i gabinety nie mają przypisanych innych dyżurów.
     *
     * @param startTime Godzina rozpoczęcia szukanego okna czasowego (format HH:mm).
     * @param endTime   Godzina zakończenia szukanego okna czasowego (format HH:mm).
     * @return Obiekt {@link AvailabilityResponse} zawierający listy wolnych lekarzy i gabinetów.
     * @throws ResponseStatusException (HttpStatus.BAD_REQUEST) jeśli podany przedział czasu jest nieprawidłowy (np. start > end).
     */
    @Operation(
            summary = "Pobierz dostępnych lekarzy i gabinety",
            description = "Zwraca listę lekarzy oraz gabinetów zabiegowych, które są wolne w podanym przedziale godzinowym.",
            parameters = {
                    @Parameter(
                            in = ParameterIn.QUERY,
                            name = "startTime",
                            description = "Godzina rozpoczęcia (format HH:mm)",
                            required = true,
                            example = "08:00",
                            schema = @Schema(
                                    type = "string",
                                    pattern = "^\\d{2}:\\d{2}$"
                            )
                    ),
                    @Parameter(
                            in = ParameterIn.QUERY,
                            name = "endTime",
                            description = "Godzina zakończenia (format HH:mm)",
                            required = true,
                            example = "12:00",
                            schema = @Schema(
                                    type = "string",
                                    pattern = "^\\d{2}:\\d{2}$"
                            )
                    )
            }
    )
    @GetMapping("/availability")
    public AvailabilityResponse getAvailableDoctorsAndConsultingRooms(
            @RequestParam("startTime") @DateTimeFormat(pattern ="HH:mm") LocalTime startTime,
            @RequestParam("endTime") @DateTimeFormat(pattern ="HH:mm") LocalTime endTime){
        try {
            return scheduleService.getAvailableDoctorsAndConsultingRooms(startTime, endTime);
        }catch(InvalidScheduleTimePeriod e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getLocalizedMessage());
        }
    }

    /**
     * Planuje nowy dyżur dla lekarza w konkretnym gabinecie.
     * <p>
     * Metoda waliduje istnienie lekarza i gabinetu oraz poprawność logiczną ram czasowych.
     * Dodatkowo sprawdza, czy w podanym terminie nie występuje konflikt (zajętość zasobu).
     *
     * @param scheduleRequest Obiekt DTO zawierający ID lekarza, ID gabinetu oraz ramy czasowe dyżuru.
     * @throws ResponseStatusException (HttpStatus.BAD_REQUEST) w przypadku gdy lekarz lub gabinet nie istnieje, lub podano błędny czas.
     * @throws ResponseStatusException (HttpStatus.CONFLICT) w przypadku gdy lekarz lub gabinet jest już zajęty w tym terminie.
     */
    @Operation(
            summary = "Zaplanuj dyżur lekarza",
            description = "Tworzy nowy dyżur dla wybranego lekarza i gabinetu w określonych godzinach. " +
                    "Godziny należy podawać w formacie HH:mm. System domyślnie przyjmuje, że dyżur dotyczy dnia dzisiejszego."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dyżur został pomyślnie utworzony"),
            @ApiResponse(responseCode = "400", description = "Nieprawidłowe dane (np. błędny format czasu, brak ID)"),
            @ApiResponse(responseCode = "409", description = "Konflikt - lekarz lub gabinet jest już zajęty w tym terminie")
    })
    @PostMapping
    public void scheduleDuty(@Valid @RequestBody ScheduleRequest scheduleRequest){
        try {
            scheduleService.addSchedule(scheduleRequest);
        } catch (DoctorNotFoundException | ConsultingRoomNotFoundException | InvalidScheduleTimePeriod e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getLocalizedMessage());
        } catch (ConflictInScheduleTimePeriod e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getLocalizedMessage());
        }
    }

    /**
     * Usuwa istniejący dyżur z harmonogramu.
     * <p>
     * Usunięcie jest możliwe tylko w przypadku, gdy do danego dyżuru nie zostały jeszcze 
     * przypisane żadne konkretne wizyty pacjentów.
     *
     * @param id Unikalny identyfikator dyżuru do usunięcia.
     * @throws ResponseStatusException (HttpStatus.NOT_FOUND) jeśli dyżur o podanym ID nie istnieje.
     * @throws ResponseStatusException (HttpStatus.CONFLICT) jeśli do dyżuru są przypisane wizyty.
     */
    @Operation(
            summary = "Usuń dyżur",
            description = "Trwale usuwa dyżur z systemu na podstawie jego identyfikatora. " +
                    "Operacja zostanie zablokowana, jeśli do tego dyżuru są już przypisane wizyty pacjentów."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dyżur został pomyślnie usunięty"),
            @ApiResponse(responseCode = "404", description = "Dyżur o podanym ID nie został znaleziony"),
            @ApiResponse(responseCode = "409", description = "Nie można usunąć dyżuru - istnieją przypisane do niego wizyty")
    })
    @DeleteMapping("/{id}")
    public void deleteScheduleById(@PathVariable int id) {
        try {
            scheduleService.deleteScheduleById(id);
        } catch(ScheduleNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (VisitAssignedToScheduleException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }
}