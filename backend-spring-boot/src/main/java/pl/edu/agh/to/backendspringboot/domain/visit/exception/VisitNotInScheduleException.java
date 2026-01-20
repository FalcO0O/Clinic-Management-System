package pl.edu.agh.to.backendspringboot.domain.visit.exception;

public class VisitNotInScheduleException extends RuntimeException {
    public VisitNotInScheduleException(String message) {
        super(message);
    }
}
