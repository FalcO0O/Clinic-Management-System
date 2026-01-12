package pl.edu.agh.to.backendspringboot.domain.consulting_room.exception;

public class ConsultingRoomNumberAlreadyExistsException extends RuntimeException {
    public ConsultingRoomNumberAlreadyExistsException(String message) {
        super(message);
    }
}
