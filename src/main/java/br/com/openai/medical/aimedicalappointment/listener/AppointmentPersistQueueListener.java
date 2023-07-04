package br.com.openai.medical.aimedicalappointment.listener;

import java.util.logging.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import br.com.openai.medical.aimedicalappointment.event.AppointmentPersistEvent;

@Component
@Order(1)
public class AppointmentPersistQueueListener implements ApplicationListener<AppointmentPersistEvent> {

    private static final Logger logger = Logger.getLogger(AppointmentPersistQueueListener.class.getName());
    private final ObjectMapper objectMapper;
    private final AmqpTemplate amqpTemplate;

    public AppointmentPersistQueueListener(ObjectMapper objectMapper, AmqpTemplate amqpTemplate) {
        this.objectMapper = objectMapper;
        this.amqpTemplate = amqpTemplate;
    }

    @Override
    public void onApplicationEvent(AppointmentPersistEvent event) {
        try {
            var appointment = event.getAppointment();
            var json = objectMapper.writeValueAsString(appointment);
            amqpTemplate.convertAndSend("appointment.persist.queue", json);
        } catch (JsonProcessingException e) {
            logger.finer(e.getMessage());
        }
    }
}
