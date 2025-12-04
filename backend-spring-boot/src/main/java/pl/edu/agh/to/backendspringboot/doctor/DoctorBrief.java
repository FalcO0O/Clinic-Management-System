package pl.edu.agh.to.backendspringboot.doctor;

public interface DoctorBrief {
    String getFirstName();
    String getLastName();
    MedicalSpecialization getSpecialization();
}