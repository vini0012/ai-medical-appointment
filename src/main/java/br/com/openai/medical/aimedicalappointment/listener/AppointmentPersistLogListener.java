package br.com.openai.medical.aimedicalappointment.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import br.com.openai.medical.aimedicalappointment.event.AppointmentPersistEvent;
import br.com.openai.medical.aimedicalappointment.repository.model.Appointment;

@Component
public class AppointmentPersistLogListener {

    private final Logger logger = LoggerFactory.getLogger(AppointmentPersistLogListener.class);

    @EventListener
    @Order(2)
    public void onApplicationEvent(AppointmentPersistEvent event) {
        Appointment appointment = event.getAppointment();
        logger.info("Inserindo o agendamento: {}", appointment);
    }
}
