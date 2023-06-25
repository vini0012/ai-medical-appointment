package br.com.openai.medical.aimedicalappointment.functions;

import com.fasterxml.jackson.annotation.JsonPropertyDescription;

public class CreateAppointment {
  @JsonPropertyDescription("Doctor's specialization")
  public String specialization;

  @JsonPropertyDescription("Doctor's name")
  public String doctorName;

  @JsonPropertyDescription("Date and time of appointment")
  public String date;
}
