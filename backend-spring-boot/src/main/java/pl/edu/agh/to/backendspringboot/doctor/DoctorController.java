package pl.edu.agh.to.backendspringboot.doctor;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("doctors")
public class DoctorController {
    private final DoctorService doctorService;

    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @PostMapping
    public void addDoctor(@Valid @RequestBody DoctorRequest doctorRequest){
        try {
            doctorService.addDoctor(DoctorRequest.toEntity(doctorRequest));
        }catch(InvalidMedicalSpecialization e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getLocalizedMessage());
        }
    }

    @GetMapping
    public List<DoctorBriefResponse> getDoctors(){
        return doctorService.getDoctors().stream().map(DoctorBriefResponse::from).toList();
    }

    public record DoctorBriefResponse(
            String firstName,
            String lastName,
            String specialization
    ){
        public static DoctorBriefResponse from(DoctorBrief doctor){
            return new DoctorBriefResponse(
                    doctor.getFirstName(),
                    doctor.getLastName(),
                    doctor.getSpecialization().toString()
            );
        }
    }


}
