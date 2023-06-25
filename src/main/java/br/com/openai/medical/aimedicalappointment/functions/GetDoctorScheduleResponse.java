package br.com.openai.medical.aimedicalappointment.functions;

import br.com.openai.medical.aimedicalappointment.repository.model.Appointment;

import java.util.List;

public record GetDoctorScheduleResponse(
  List<Appointment> doctorsAppointments, String description
) {
}
