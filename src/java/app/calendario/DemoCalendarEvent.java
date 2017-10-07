package app.calendario;

import com.clinica.veterinaria.citas.Cita;
import java.util.Date;
import org.zkoss.calendar.api.CalendarEvent;
 
import org.zkoss.calendar.impl.SimpleCalendarEvent;
 
public class DemoCalendarEvent extends SimpleCalendarEvent implements CalendarEvent{
    
    private Evento evento;
    private Cita cita;
    private static final long HOUR = 3600*1000;
    
    public DemoCalendarEvent(Cita cita) {
        this.cita = cita;
        int estado = cita.getEstado();
        boolean lock= true;
        String stado;
            if(estado == 0) {
                stado = "#A32929"; //Cancelado - Rojo
            }
            else if(estado == 1) {
                stado = "#00b300"; // Acudido - Verde
            }
            else if(estado == 2){
                stado = "#000000"; //Pendiente - Amarillo
                lock = false;
            }
            else {
                stado = "#0000ff"; //Avisado - Azul
            }    
        setHeaderColor(stado);
        setContentColor(stado);
        setContent("Turno: " +cita.getMascota().getNombre()+" de "+cita.getCliente().getNombre() + " Servicio: "+cita.getServicio().getNombre());
        
        setBeginDate(cita.getFechatiempo());
        setEndDate(new Date(cita.getFechatiempo().getTime()+1*HOUR));
        setLocked(lock);
    }

    public Cita getCita() {
        return cita;
    }

    public void setCita(Cita cita) {
        this.cita = cita;
    }

    public Evento getEvento() {
        return evento;
    }

    public void setEvento(Evento evento) {
        this.evento = evento;
    }

    
    public DemoCalendarEvent(Evento evento) {
        this.evento = evento; 
        setHeaderColor(evento.getHeaderColor());
        setContentColor(evento.getContentColor());
        setContent(evento.getContent());
        setTitle(evento.getTitle());
        setBeginDate(evento.getBeginDate());
        setEndDate(evento.getEndDate());
        setLocked(evento.isLocked());
    }
    
    
    
    public DemoCalendarEvent(Date beginDate, Date endDate, String headerColor, String contentColor, String content) {
        setHeaderColor(headerColor);
        setContentColor(contentColor);
        setContent(content);
        setBeginDate(beginDate);
        setEndDate(endDate);
    }
 
    
    
    public DemoCalendarEvent(Date beginDate, Date endDate, String headerColor, String contentColor, String content,
            String title) {
        setHeaderColor(headerColor);
        setContentColor(contentColor);
        setContent(content);
        setTitle(title);
        setBeginDate(beginDate);
        setEndDate(endDate);
    }
 
    public DemoCalendarEvent(Date beginDate, Date endDate, String headerColor, String contentColor, String content,
            String title, boolean locked) {
        setHeaderColor(headerColor);
        setContentColor(contentColor);
        setContent(content);
        setTitle(title);
        setBeginDate(beginDate);
        setEndDate(endDate);
        setLocked(locked);
    }
     
    public DemoCalendarEvent() {
        this.evento = new Evento();
        setHeaderColor("#FFFFFF");
        setContentColor("#000000");
    }
}
