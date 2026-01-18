package pl.edu.agh.to.backendspringboot.domain.visit.exception;

public class VisitAlreadyExistsException extends RuntimeException {
    public VisitAlreadyExistsException(String message) {
        super(message);
    }
}
