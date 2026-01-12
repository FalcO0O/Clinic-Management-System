package pl.edu.agh.to.backendspringboot.domain.doctor.model;

import pl.edu.agh.to.backendspringboot.domain.shared.model.Address;

public interface DoctorDetail extends DoctorBrief {
    String getPesel();
    Address getAddress();
}
