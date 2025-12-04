package pl.edu.agh.to.backendspringboot.doctor;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.edu.agh.to.backendspringboot.doctor.model.Doctor;
import pl.edu.agh.to.backendspringboot.doctor.model.DoctorBrief;
import pl.edu.agh.to.backendspringboot.doctor.model.DoctorInfo;

import java.util.List;
import java.util.Optional;

public interface DoctorRepository extends JpaRepository<Doctor,Integer> {

    @Query("select d.firstName as firstName, d.lastName as lastName,d.specialization as specialization  from Doctor d")
    List<DoctorBrief> findDoctorsBrief();

    @Query("select d.firstName as firstName, d.lastName as lastName,d.specialization as specialization, d.address as address  from Doctor d where  d.id = :id")
    Optional<DoctorInfo> findDoctorInfoById(Integer id);
}
