package br.com.openai.medical.aimedicalappointment.repository;

import br.com.openai.medical.aimedicalappointment.repository.model.Appointment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AppointmentRepository extends MongoRepository<Appointment, UUID> {
  List<Appointment> getAppointmentsByDoctorNameIgnoreCase(String doctorName);
}
