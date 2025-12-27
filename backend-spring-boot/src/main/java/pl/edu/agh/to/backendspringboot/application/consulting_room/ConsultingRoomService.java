package pl.edu.agh.to.backendspringboot.application.consulting_room;

import org.springframework.stereotype.Service;
import pl.edu.agh.to.backendspringboot.domain.consulting_room.exception.ConsultingRoomNotFoundException;
import pl.edu.agh.to.backendspringboot.infrastructure.consulting_room.ConsultingRoomRepository;
import pl.edu.agh.to.backendspringboot.presentation.consulting_room.dto.ConsultingRoomBriefResponse;
import pl.edu.agh.to.backendspringboot.presentation.consulting_room.dto.ConsultingRoomDetailResponse;
import pl.edu.agh.to.backendspringboot.presentation.consulting_room.dto.ConsultingRoomRequest;

import java.util.List;

@Service
public class ConsultingRoomService {
    private final ConsultingRoomRepository consultingRoomRepository;

    public ConsultingRoomService(ConsultingRoomRepository consultingRoomRepository){
        this.consultingRoomRepository = consultingRoomRepository;
    }

    public void addConsultingRoom(ConsultingRoomRequest consultingRoomRequest) {
        consultingRoomRepository.save(consultingRoomRequest.toEntity());
    }

    public List<ConsultingRoomBriefResponse> getConsultingRooms(){
        return consultingRoomRepository.findConsultingRoomsBrief().stream()
                .map(ConsultingRoomBriefResponse::from).toList();
    }

    public void deleteConsultingRoomById(Integer id) {
        if (!consultingRoomRepository.existsById(id)) {
            throw new ConsultingRoomNotFoundException("Consulting room with id " + id + " not found");
        }
        consultingRoomRepository.deleteById(id);
    }
}
