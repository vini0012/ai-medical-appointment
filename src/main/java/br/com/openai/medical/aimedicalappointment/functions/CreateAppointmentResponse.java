package br.com.openai.medical.aimedicalappointment.functions;

public record CreateAppointmentResponse(
  String specialization,
  String doctorName,
  String paciente,
  String date) {
}
