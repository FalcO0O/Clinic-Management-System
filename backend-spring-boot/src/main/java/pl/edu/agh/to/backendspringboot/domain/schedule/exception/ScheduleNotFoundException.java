package pl.edu.agh.to.backendspringboot.domain.schedule.exception;

public class ScheduleNotFoundException extends RuntimeException {
    public ScheduleNotFoundException(String message) {
        super(message);
    }
}
