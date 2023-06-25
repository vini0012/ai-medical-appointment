package br.com.openai.medical.aimedicalappointment.functions;

import com.fasterxml.jackson.annotation.JsonPropertyDescription;

public class GetDoctorSchedule {
  @JsonPropertyDescription("Doctor's name")
  public String doctorName;
}
