package pl.edu.agh.to.backendspringboot.application.schedule;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.edu.agh.to.backendspringboot.domain.consulting_room.exception.ConsultingRoomNotFoundException;
import pl.edu.agh.to.backendspringboot.domain.doctor.exception.DoctorNotFoundException;
import pl.edu.agh.to.backendspringboot.domain.schedule.exception.ConflictInScheduleTimePeriod;
import pl.edu.agh.to.backendspringboot.domain.schedule.exception.InvalidScheduleTimePeriod;
import pl.edu.agh.to.backendspringboot.infrastructure.consulting_room.ConsultingRoomRepository;
import pl.edu.agh.to.backendspringboot.infrastructure.doctor.DoctorRepository;
import pl.edu.agh.to.backendspringboot.infrastructure.schedule.ScheduleRepository;
import pl.edu.agh.to.backendspringboot.presentation.consulting_room.dto.ConsultingRoomBriefResponse;
import pl.edu.agh.to.backendspringboot.presentation.doctor.dto.DoctorBriefResponse;
import pl.edu.agh.to.backendspringboot.presentation.schedule.dto.AvailabilityResponse;
import pl.edu.agh.to.backendspringboot.presentation.schedule.dto.ScheduleRequest;

import java.time.LocalTime;

/**
 * Serwis realizujący logikę biznesową związaną z harmonogramem wizyt i dyżurów.
 * Odpowiada za planowanie terminów, sprawdzanie dostępności zasobów (lekarzy i gabinetów)
 * oraz walidację konfliktów czasowych.
 */
@Service
public class ScheduleService {

    private static final LocalTime MIN_TIME_SLOT = LocalTime.of(0, 30);
    private static final LocalTime MAX_TIME_SLOT = LocalTime.of(12, 0);
    private final ScheduleRepository scheduleRepository;
    private final DoctorRepository doctorRepository;
    private final ConsultingRoomRepository consultingRoomRepository;

    /**
     * Konstruktor serwisu wstrzykujący wymagane repozytoria.
     *
     * @param scheduleRepository Repozytorium do zarządzania grafikami.
     * @param doctorRepository Repozytorium do weryfikacji lekarzy.
     * @param consultingRoomRepository Repozytorium do weryfikacji gabinetów.
     */
    public ScheduleService(ScheduleRepository scheduleRepository, DoctorRepository doctorRepository, ConsultingRoomRepository consultingRoomRepository) {
        this.scheduleRepository = scheduleRepository;
        this.doctorRepository = doctorRepository;
        this.consultingRoomRepository = consultingRoomRepository;
    }

    /**
     * Wyszukuje lekarzy i gabinety dostępne w zadanym przedziale czasowym.
     * Metoda weryfikuje poprawność ram czasowych, a następnie pobiera zasoby,
     * które nie mają zaplanowanych dyżurów w tym czasie.
     *
     * @param startTime Godzina rozpoczęcia szukanego okna czasowego.
     * @param endTime Godzina zakończenia szukanego okna czasowego.
     * @return Obiekt {@link AvailabilityResponse} zawierający listy dostępnych lekarzy i gabinetów.
     * @throws InvalidScheduleTimePeriod jeśli podany przedział czasowy jest nieprawidłowy (np. koniec przed początkiem).
     */
    @Transactional(readOnly=true)
    public AvailabilityResponse getAvailableDoctorsAndConsultingRooms(LocalTime startTime, LocalTime endTime) {
        validateScheduleTimePeriod(startTime,endTime);

        var doctors = scheduleRepository.findAvailableDoctorsInPeriod(startTime, endTime)
                .stream().map(DoctorBriefResponse::from).toList();

        var consultingRooms = scheduleRepository.findAvailableConsultingRoomsInPeriod(startTime, endTime)
                .stream().map(ConsultingRoomBriefResponse::from).toList();

        return new AvailabilityResponse(doctors, consultingRooms);
    }

    /**
     * Tworzy i zapisuje nowy dyżur (Schedule) w systemie.
     * Metoda przeprowadza szereg walidacji: poprawność czasu, istnienie lekarza i gabinetu,
     * oraz sprawdza czy nie występują konflikty w harmonogramie (czy lekarz lub gabinet nie są już zajęci).
     *
     * @param scheduleRequest Obiekt DTO z danymi dotyczącymi planowanego dyżuru.
     * @throws DoctorNotFoundException jeśli lekarz o podanym ID nie istnieje.
     * @throws ConsultingRoomNotFoundException jeśli gabinet o podanym ID nie istnieje.
     * @throws ConflictInScheduleTimePeriod jeśli lekarz lub gabinet ma już zaplanowany dyżur w tym czasie.
     * @throws InvalidScheduleTimePeriod jeśli ramy czasowe są niepoprawne (np. zbyt krótki dyżur).
     */
    @Transactional
    public void addSchedule(ScheduleRequest scheduleRequest) {
        validateScheduleTimePeriod(scheduleRequest.startTime(), scheduleRequest.endTime());

        int doctorId = scheduleRequest.doctorId();
        int consultingRoomId = scheduleRequest.consultingRoomId();

        var doctor = doctorRepository.findById(doctorId)
                .orElseThrow(()->new DoctorNotFoundException("Doctor with id " + doctorId + " not found"));
        var consultingRoom = consultingRoomRepository.findById(consultingRoomId)
                .orElseThrow(()->new ConsultingRoomNotFoundException("Consulting room with id " + consultingRoomId + " not found"));

        if(scheduleRepository.existsScheduleInPeriodForConsultingDoctor(scheduleRequest.startTime(), scheduleRequest.endTime(), consultingRoomId)){
            throw new ConflictInScheduleTimePeriod("Doctor is already scheduled for this consulting room");
        }

        if(scheduleRepository.existsScheduleInPeriodForDoctor(scheduleRequest.startTime(), scheduleRequest.endTime(), doctorId)){
            throw new ConflictInScheduleTimePeriod("Consulting room is already scheduled for this doctor");
        }

        scheduleRepository.save(scheduleRequest.toSchedule(doctor, consultingRoom));
    }

    /**
     * Metoda pomocnicza walidująca poprawność logiczną przedziału czasowego.
     * Sprawdza kolejność godzin oraz minimalną i maksymalną długość trwania dyżuru.
     *
     * @param startTime Godzina rozpoczęcia.
     * @param endTime Godzina zakończenia.
     * @throws InvalidScheduleTimePeriod jeśli walidacja nie powiedzie.
     */
    private void validateScheduleTimePeriod(LocalTime startTime, LocalTime endTime){
        var timePeriod = endTime.minusMinutes(startTime.getMinute()).minusHours(startTime.getHour());
        if(endTime.isBefore(startTime)){
            throw new InvalidScheduleTimePeriod("End time must be after start time");
        }
        if(timePeriod.isAfter(MAX_TIME_SLOT)){
            throw new InvalidScheduleTimePeriod("Shift period must be less than 12 hours");
        }
        if(timePeriod.isBefore(MIN_TIME_SLOT)){
            throw new InvalidScheduleTimePeriod("Shift period must be greater than 30 minutes");
        }

    }
}
