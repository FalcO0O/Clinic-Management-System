package pl.edu.agh.to.backendspringboot.domain.schedule.exception;

public class InvalidScheduleTimePeriod extends RuntimeException {
    public InvalidScheduleTimePeriod(String message) {
        super(message);
    }
}
