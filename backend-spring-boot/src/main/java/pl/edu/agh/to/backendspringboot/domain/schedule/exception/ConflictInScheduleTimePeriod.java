package pl.edu.agh.to.backendspringboot.domain.schedule.exception;

public class ConflictInScheduleTimePeriod extends RuntimeException {
    public ConflictInScheduleTimePeriod(String message) {
        super(message);
    }
}
