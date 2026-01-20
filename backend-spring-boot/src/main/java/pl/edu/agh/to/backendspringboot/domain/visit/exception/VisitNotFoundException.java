package pl.edu.agh.to.backendspringboot.domain.visit.exception;

public class VisitNotFoundException extends RuntimeException {
    public VisitNotFoundException(String message) {
        super(message);
    }
}
