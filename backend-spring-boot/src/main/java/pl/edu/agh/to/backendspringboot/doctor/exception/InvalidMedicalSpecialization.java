package pl.edu.agh.to.backendspringboot.doctor.exception;

public class InvalidMedicalSpecialization extends RuntimeException {
    public InvalidMedicalSpecialization(String message) {
        super(message);
    }
}
