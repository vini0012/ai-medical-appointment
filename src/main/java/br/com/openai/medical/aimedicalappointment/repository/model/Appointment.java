package br.com.openai.medical.aimedicalappointment.repository.model;

import br.com.openai.medical.aimedicalappointment.functions.CreateAppointment;
import org.springframework.data.annotation.Id;

import java.util.UUID;

public class Appointment {
  @Id
  private UUID id = UUID.randomUUID();
  public String specialization;
  public String doctorName;
  public String patient;
  public String date;

  public Appointment() {
  }

  public Appointment(CreateAppointment createAppointment) {
    this.specialization = createAppointment.specialization;
    this.doctorName = createAppointment.doctorName;
    this.date = createAppointment.date;
  }
}
