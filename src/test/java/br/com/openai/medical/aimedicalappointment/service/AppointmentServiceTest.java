package br.com.openai.medical.aimedicalappointment.service;

import br.com.openai.medical.aimedicalappointment.functions.CreateAppointment;
import br.com.openai.medical.aimedicalappointment.functions.CreateAppointmentResponse;
import br.com.openai.medical.aimedicalappointment.functions.GetDoctorSchedule;
import br.com.openai.medical.aimedicalappointment.functions.GetDoctorScheduleResponse;
import br.com.openai.medical.aimedicalappointment.functions.GetSchedule;
import br.com.openai.medical.aimedicalappointment.functions.GetScheduleResponse;
import br.com.openai.medical.aimedicalappointment.repository.AppointmentRepository;
import br.com.openai.medical.aimedicalappointment.repository.model.Appointment;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class AppointmentServiceTest {

    @Mock
    private AppointmentRepository repository;

    @Mock
    private ApplicationEventPublisher publisher;

    @InjectMocks
    private AppointmentService appointmentService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        SecurityContext securityContextMock = mock(SecurityContext.class);
        Authentication authenticationMock = mock(Authentication.class);
        when(authenticationMock.getName()).thenReturn("Lucas Santos");

        when(securityContextMock.getAuthentication()).thenReturn(authenticationMock);
        SecurityContextHolder.setContext(securityContextMock);
    }

    @Test
    void testCreateAppointment() {
        CreateAppointment createAppointment = new CreateAppointment();
        createAppointment.date = "2023-07-05T12:50:00";
        createAppointment.doctorName = "Roberto";
        createAppointment.specialization = "Psiquiatra";

        CreateAppointmentResponse response = appointmentService.createAppointment(createAppointment);

        assertNotNull(response);
        assertEquals(response.paciente(), "Lucas Santos");
        assertEquals(response.doctorName(), "Roberto");
        assertEquals(response.date(), "2023-07-05T12:50:00");
        assertEquals(response.specialization(), "Psiquiatra");

    }

    @Test
    void getDoctorSchedule() {
        GetDoctorSchedule doctorSchedule = new GetDoctorSchedule();
        doctorSchedule.doctorName = "Roberto";

        Appointment appointment1 = createAppointment("Roberto", "2023-07-05T12:50:00", "João Vitor", "Dermatologista");
        Appointment appointment2 = createAppointment("Roberto", "2023-07-07T12:50:00", "Vinicius Ramos", "Dermatologista");

        List<Appointment> appointments = new ArrayList<>();
        appointments.add(appointment1);
        appointments.add(appointment2);

        when(repository.getAppointmentsByDoctorNameIgnoreCase(any())).thenReturn(appointments);

        GetDoctorScheduleResponse scheduleResponse = appointmentService.getDoctorSchedule(doctorSchedule);
        assertNotNull(scheduleResponse);
        assertFalse(scheduleResponse.doctorsAppointments().isEmpty());
        assertEquals(scheduleResponse.description(), "Lista de agendamentos, contendo data e hora em formato escrito, paciente e a especialidade para o médico informado");
    }

    @Test
    void getSchedule() {
        GetSchedule schedule = new GetSchedule();

        Appointment appointment1 = createAppointment("Roberto", "2023-07-05T12:50:00", "João Vitor", "Dermatologista");
        Appointment appointment2 = createAppointment("Ricardo", "2023-07-07T12:50:00", "Vinicius Ramos", "Oftamologista");

        List<Appointment> appointments = new ArrayList<>();
        appointments.add(appointment1);
        appointments.add(appointment2);

        when(repository.findAll()).thenReturn(appointments);
        GetScheduleResponse scheduleResponse = appointmentService.getSchedule(schedule);

        assertNotNull(scheduleResponse);
        assertFalse(scheduleResponse.doctorsAppointments().isEmpty());
        assertEquals(scheduleResponse.description(), "Lista de agendamentos, contendo data e hora em formato escrito, paciente e a especialidade para o médico informado");
    }

    private Appointment createAppointment(String doctorName, String date, String patient, String specialization) {
        Appointment appointment = new Appointment();
        appointment.doctorName = doctorName;
        appointment.date = date;
        appointment.patient = patient;
        appointment.specialization = specialization;

        return appointment;
    }
}
