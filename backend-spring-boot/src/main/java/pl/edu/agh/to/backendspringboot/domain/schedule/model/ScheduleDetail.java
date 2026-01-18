package pl.edu.agh.to.backendspringboot.domain.schedule.model;

import pl.edu.agh.to.backendspringboot.domain.doctor.model.Doctor;

public interface ScheduleDetail extends ScheduleBrief {
    Doctor getDoctor();
}
