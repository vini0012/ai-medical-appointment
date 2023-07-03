package br.com.openai.medical.aimedicalappointment.service;

import br.com.openai.medical.aimedicalappointment.functions.CreateAppointment;
import br.com.openai.medical.aimedicalappointment.functions.CreateAppointmentResponse;
import br.com.openai.medical.aimedicalappointment.functions.GetDoctorSchedule;
import br.com.openai.medical.aimedicalappointment.functions.GetDoctorScheduleResponse;
import br.com.openai.medical.aimedicalappointment.functions.GetSchedule;
import br.com.openai.medical.aimedicalappointment.functions.GetScheduleResponse;
import br.com.openai.medical.aimedicalappointment.repository.AppointmentRepository;
import br.com.openai.medical.aimedicalappointment.repository.model.Appointment;
import io.micrometer.observation.annotation.Observed;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Observed
public class AppointmentService {

  private final AppointmentRepository appointmentRepository;

  public AppointmentService(AppointmentRepository appointmentRepository) {
    this.appointmentRepository = appointmentRepository;
  }

  public CreateAppointmentResponse createAppointment(CreateAppointment createAppointment) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    Appointment appointment = new Appointment(createAppointment);
    appointment.patient = auth.getName();

    appointmentRepository.save(appointment);

    return new CreateAppointmentResponse(appointment.specialization, appointment.doctorName, appointment.patient, appointment.date);
  }

  public GetDoctorScheduleResponse getDoctorSchedule(GetDoctorSchedule getDoctorSchedule) {
    List<Appointment> results = appointmentRepository.getAppointmentsByDoctorNameIgnoreCase(getDoctorSchedule.doctorName);

    if (results.isEmpty()) {
      return new GetDoctorScheduleResponse(new ArrayList<>(), "Não existe agenda para o medico informado.");
    }
    return new GetDoctorScheduleResponse(results, "Lista de agendamentos, contendo data e hora em formato escrito, paciente e a especialidade para o médico informado");
  }

  public GetScheduleResponse getSchedule(GetSchedule getSchedule) {
    List<Appointment> results = appointmentRepository.findAll();

    if (results.isEmpty()) {
      return new GetScheduleResponse(new ArrayList<>(), "Não existe agenda para nenhum medico.");
    }
    return new GetScheduleResponse(results, "Lista de agendamentos, contendo data e hora em formato escrito, paciente e a especialidade para o médico informado");
  }
}
