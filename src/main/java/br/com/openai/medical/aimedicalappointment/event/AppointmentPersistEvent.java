package br.com.openai.medical.aimedicalappointment.event;

import org.springframework.context.ApplicationEvent;

import br.com.openai.medical.aimedicalappointment.repository.model.Appointment;

public class AppointmentPersistEvent extends ApplicationEvent {
    private final Appointment appointment;
    public AppointmentPersistEvent(Object source, Appointment appointment) {
        super(source);
        this.appointment = appointment;
    }
    public Appointment getAppointment() {
        return appointment;
    }
}
