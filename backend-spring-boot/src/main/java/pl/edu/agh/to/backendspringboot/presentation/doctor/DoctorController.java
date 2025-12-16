package pl.edu.agh.to.backendspringboot.presentation.doctor;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import pl.edu.agh.to.backendspringboot.application.doctor.DoctorService;
import pl.edu.agh.to.backendspringboot.shared.doctor.dto.DoctorBriefResponse;
import pl.edu.agh.to.backendspringboot.shared.doctor.dto.DoctorInfoResponse;
import pl.edu.agh.to.backendspringboot.shared.doctor.dto.DoctorRequest;
import pl.edu.agh.to.backendspringboot.domain.doctor.exception.DoctorNotFoundException;
import pl.edu.agh.to.backendspringboot.domain.doctor.exception.InvalidMedicalSpecialization;

import java.util.List;
/**
 * Kontroler REST obsługujący operacje związane z zarządzaniem lekarzami.
 * Udostępnia endpointy do tworzenia, pobierania i usuwania lekarzy.
 */
@RestController
@RequestMapping("doctors")
@CrossOrigin(origins = "http://localhost:3000")
public class DoctorController {
    private final DoctorService doctorService;
    
    /**
     * Konstruktor kontrolera wstrzykujący zależność serwisu lekarzy.
     *
     * @param doctorService Serwis zawierający logikę biznesową dotyczącą lekarzy.
     */
    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    /**
     * Dodaje nowego lekarza do systemu.
     * <p>
     * Metoda waliduje poprawność danych wejściowych. Jeśli specjalizacja medyczna
     * jest nieprawidłowa, zwracany jest błąd 400 Bad Request.
     *
     * @param doctorRequest Obiekt DTO zawierający dane wymagane do utworzenia lekarza.
     * @throws ResponseStatusException (HttpStatus.BAD_REQUEST) jeśli podana specjalizacja jest nieprawidłowa.
     */
    @PostMapping
    public void addDoctor(@Valid @RequestBody DoctorRequest doctorRequest){
        try {
            doctorService.addDoctor(doctorRequest);
        }catch(InvalidMedicalSpecialization e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getLocalizedMessage());
        }
    }

    /**
     * Pobiera listę wszystkich lekarzy dostępnych w systemie.
     * Zwraca dane w formie skróconej (Brief).
     *
     * @return Lista obiektów {@link DoctorBriefResponse} reprezentujących lekarzy.
     */
    @GetMapping
    public List<DoctorBriefResponse> getDoctors(){
        return doctorService.getDoctors();
    }

    /**
     * Pobiera szczegółowe informacje o konkretnym lekarzu na podstawie jego identyfikatora.
     *
     * @param id Unikalny identyfikator lekarza.
     * @return Obiekt {@link DoctorInfoResponse} zawierający szczegółowe dane lekarza.
     * @throws ResponseStatusException (HttpStatus.NOT_FOUND) jeśli lekarz o podanym ID nie istnieje.
     */
    @GetMapping("/{id}")
    public DoctorInfoResponse getDoctorById(@PathVariable Integer id) {
        try {
            return doctorService.getDoctorInfoById(id);
        } catch (DoctorNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getLocalizedMessage());
        }
    }

    /**
     * Usuwa lekarza z systemu na podstawie jego identyfikatora.
     *
     * @param id Unikalny identyfikator lekarza do usunięcia.
     * @throws ResponseStatusException (HttpStatus.NOT_FOUND) jeśli lekarz o podanym ID nie istnieje.
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteDoctorById(@PathVariable Integer id) {
        try {
            doctorService.deleteDoctorById(id);
        } catch (DoctorNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getLocalizedMessage());
        }
    }

}
