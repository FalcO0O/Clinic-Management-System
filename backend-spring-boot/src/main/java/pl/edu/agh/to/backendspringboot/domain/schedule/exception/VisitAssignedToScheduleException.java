package pl.edu.agh.to.backendspringboot.domain.schedule.exception;

public class VisitAssignedToScheduleException extends RuntimeException {
    public VisitAssignedToScheduleException(String message) {
        super(message);
    }
}
