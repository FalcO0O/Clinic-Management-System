package pl.edu.agh.to.backendspringboot.doctor;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DoctorRepository extends JpaRepository<Doctor,Integer> {

    @Query("select d.firstName as firstName, d.lastName as lastName,d.specialization as specialization  from Doctor d")
    List<DoctorBrief> findDoctorsBrief();
}
