package pl.edu.agh.to.backendspringboot.presentation.visit;

import io.reactivex.rxjava3.core.Observable;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import pl.edu.agh.to.backendspringboot.application.visit.VisitService;
import pl.edu.agh.to.backendspringboot.domain.consulting_room.exception.ConsultingRoomNotFoundException;
import pl.edu.agh.to.backendspringboot.domain.consulting_room.exception.ConsultingRoomNumberAlreadyExistsException;
import pl.edu.agh.to.backendspringboot.domain.doctor.exception.DoctorNotFoundException;
import pl.edu.agh.to.backendspringboot.domain.doctor.model.MedicalSpecialization;
import pl.edu.agh.to.backendspringboot.domain.patient.exception.PatientNotFoundException;
import pl.edu.agh.to.backendspringboot.domain.visit.exception.VisitAlreadyExistsException;
import pl.edu.agh.to.backendspringboot.domain.visit.exception.VisitNotFoundException;
import pl.edu.agh.to.backendspringboot.domain.visit.exception.VisitNotInScheduleException;
import pl.edu.agh.to.backendspringboot.presentation.consulting_room.dto.ConsultingRoomRequest;
import pl.edu.agh.to.backendspringboot.presentation.visit.dto.AvailabilityResponse;
import pl.edu.agh.to.backendspringboot.presentation.visit.dto.VisitBriefResponse;
import pl.edu.agh.to.backendspringboot.presentation.visit.dto.VisitDetailResponse;
import pl.edu.agh.to.backendspringboot.presentation.visit.dto.VisitRequest;

import java.util.List;

@RestController
@RequestMapping("visits")
@CrossOrigin(origins = "http://localhost:3000")
public class VisitController
{
    private final VisitService visitService;

    public VisitController(VisitService visitService) {
        this.visitService = visitService;
    }

    @GetMapping("/availability")
    public Observable<List<AvailabilityResponse>> getAvailability(String specialization) {
        MedicalSpecialization medicalSpecialization = MedicalSpecialization.valueOf(specialization);
        return visitService.getPossibleVisits(medicalSpecialization).toList().toObservable();
    }

    @GetMapping
    public List<VisitBriefResponse> getAllVisits() {
        return visitService.getAllVisits().stream().map(VisitBriefResponse::from).toList();
    }

    @GetMapping("/{id}")
    public VisitDetailResponse getVisitById(@PathVariable  int id) {
        return VisitDetailResponse.from(visitService.getVisitById(id));
    }

    @DeleteMapping("/{id}")
    public void deleteVisitById(@PathVariable int id) {
        try {
            visitService.deleteVisitById(id);
        }catch (VisitNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }

    }

    @PostMapping
    public void addVisit(@Valid @RequestBody VisitRequest visitDataRequest) {
        try{
            visitService.addVisit(visitDataRequest);
        }
        catch (DoctorNotFoundException | PatientNotFoundException | ConsultingRoomNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
        catch (VisitAlreadyExistsException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
        catch (VisitNotInScheduleException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }


    }



}
