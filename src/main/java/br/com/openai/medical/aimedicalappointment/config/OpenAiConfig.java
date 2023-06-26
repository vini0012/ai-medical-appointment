package br.com.openai.medical.aimedicalappointment.config;

import br.com.openai.medical.aimedicalappointment.functions.CreateAppointment;
import br.com.openai.medical.aimedicalappointment.functions.GetDoctorSchedule;
import br.com.openai.medical.aimedicalappointment.functions.GetSchedule;
import br.com.openai.medical.aimedicalappointment.service.AppointmentService;
import com.theokanning.openai.completion.chat.ChatFunction;
import com.theokanning.openai.service.FunctionExecutor;
import com.theokanning.openai.service.OpenAiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenAiConfig {

  @Value("${openAi.token}")
  private String openAiToken;

  @Autowired
  private AppointmentService appointmentService;

  @Bean
  public OpenAiService getOpenApiService() {
    return new OpenAiService(openAiToken);
  }


  @Bean
  public FunctionExecutor getFunctionExecutor() {
    return new FunctionExecutor(List.of(
      ChatFunction.builder()
        .name("create_appointment")
        .description("Create an appointment at a specific date for an specialization")
        .executor(CreateAppointment.class, a -> appointmentService.createAppointment(a))
        .build(),
      ChatFunction.builder()
        .name("get_schedule")
        .description("Get all appointments for all doctors and patients")
        .executor(GetSchedule.class, a -> appointmentService.getSchedule(a))
        .build(),
      ChatFunction.builder()
        .name("get_doctor_schedule")
        .description("Get all appointments for a doctor")
        .executor(GetDoctorSchedule.class, a -> appointmentService.getDoctorSchedule(a))
        .build()
    ));
  }
}
