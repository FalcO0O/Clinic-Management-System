package pl.edu.agh.to.backendspringboot.domain.patient.exception;

public class PatientAssignedToVisitException extends RuntimeException {
    public PatientAssignedToVisitException(String message) {
        super(message);
    }
}
