package app.calendario;

import com.clinica.veterinaria.citas.Cita;
import com.clinica.veterinaria.citas.CitaDAO;
import com.clinica.veterinaria.util.Conversion;
import java.util.Calendar;
import java.util.TimeZone;
 
import org.zkoss.calendar.Calendars;
import org.zkoss.calendar.event.CalendarsEvent;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zkmax.ui.select.annotation.Subscribe;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;
 
 
public class CalendarController extends SelectorComposer<Component> {

    @Wire
    private Label fecha;
    
    @Wire
    private Calendars calendars;
    @Wire
    private Textbox filter;
     
    private DemoCalendarModel calendarModel;
     
    //the in editing calendar ui event
    private CalendarsEvent calendarsEvent = null;
    
    private void setLabel(){
        Calendar now = Calendar.getInstance();
        now.setTime(calendars.getBeginDate());
        int year = now.get(Calendar.YEAR);
        int month = now.get(Calendar.MONTH); // Note: zero based!
        int day = now.get(Calendar.DAY_OF_MONTH);
        String mes = Conversion.getMes(month+2);
        fecha.setValue(mes+" de "+year);
        
       
    }
 
    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        calendarModel = new DemoCalendarModel(new DemoCalendarData().getCalendarEvents());
        calendars.setModel(this.calendarModel);
        setLabel();
    }
     
    //control the calendar position
    @Listen("onClick = #today")
    public void gotoToday(){
        TimeZone timeZone = calendars.getDefaultTimeZone();
        calendars.setCurrentDate(Calendar.getInstance(timeZone).getTime());
        setLabel();
        
    }
    @Listen("onClick = #next")
    public void gotoNext(){
        calendars.nextPage();
        setLabel();
    }
    @Listen("onClick = #prev")
    public void gotoPrev(){
        calendars.previousPage();
        setLabel();
    }
     
    //control page display
    @Listen("onClick = #pageDay")
    public void changeToDay(){
        calendars.setMold("default");
        calendars.setDays(1);
        setLabel();
    }
    @Listen("onClick = #pageWeek")
    public void changeToWeek(){
        calendars.setMold("default");
        calendars.setDays(7);
        setLabel();
    }
    @Listen("onClick = #pageMonth")
    public void changeToYear(){
        calendars.setMold("month");
        setLabel();
    }
     
    //control the filter
    @Listen("onClick = #applyFilter")
    public void applyFilter(){
        calendarModel.setFilterText(filter.getValue());
        calendars.setModel(calendarModel);
        setLabel();
    }
    @Listen("onClick = #resetFilter")
    public void resetFilter(){
        filter.setText("");
        calendarModel.setFilterText("");
        calendars.setModel(calendarModel);
        setLabel();
    }
 
    //listen to the calendar-create and edit of a event data
    @Listen("onEventCreate = #calendars; onEventEdit = #calendars")
    public void createEvent(CalendarsEvent event) {
        calendarsEvent = event;
         
        //to display a shadow when editing
        calendarsEvent.stopClearGhost();
         
        DemoCalendarEvent data = (DemoCalendarEvent)event.getCalendarEvent();
         
        if(data == null) {
            data = new DemoCalendarEvent();
            
          
            data.setHeaderColor("#3366ff");
            data.setContentColor("#6699ff");
            data.setBeginDate(event.getBeginDate());
            data.setEndDate(event.getEndDate());
            
        } else {
            data = (DemoCalendarEvent) event.getCalendarEvent();
        }
        //notify the editor
        QueueUtil.lookupQueue().publish(new QueueMessage(QueueMessage.Type.EDIT,data));
    }
     
    //listen to the calendar-update of event data, usually send when user drag the event data 
    // CUANDO SE ARRASTRA ALGUN EVENTO!!
    @Listen("onEventUpdate = #calendars")
    public void updateEvent(CalendarsEvent event) {
        
        DemoCalendarEvent data = (DemoCalendarEvent) event.getCalendarEvent();
        data.setBeginDate(event.getBeginDate());
        data.setEndDate(event.getEndDate());
        
        //ACA HACEMOS UPDATE BD
        if(data.getCita()!=null){
            Cita cita = data.getCita();
            cita.setFecha(data.getBeginDate());
            new CitaDAO().update(cita);
        }else if (data.getEvento()!=null){
            Evento evento = new Evento(data);
            
            new EventoDAO().update(evento);
        }
        
        
        calendarModel.update(data);
    }
     
    //listen to queue message from other controller
    @Subscribe(value = QueueUtil.QUEUE_NAME)
    public void handleQueueMessage(Event event) {
        if(!(event instanceof QueueMessage)) {
            return;
        } 
        QueueMessage message = (QueueMessage)event;
        DemoCalendarEvent data = (DemoCalendarEvent)message.getData();
        // MENSAJE DE LA VENTANA DE CREAR/ EVENTO
        switch(message.getType()){
            
            case DELETE:
                calendarModel.remove(data);
                //clear the shadow of the event after editing
                calendarsEvent.clearGhost(); 
                calendarsEvent = null;
                if(data.getCita()!=null){
                        Cita cita = data.getCita();
                        cita.setFecha(data.getBeginDate());
                        new CitaDAO().delete(cita);
                    }else{
                        Evento evento = new Evento(data);

                        new EventoDAO().delete(evento);
                    }
                break;
            
            case OK:
                // SI TIENE INDICE ACTUALIZA 
                if(calendarModel.indexOf(data) >= 0){
                    calendarModel.update(data);
                    
                    if(data.getCita()!=null){
                        Cita cita = data.getCita();
                        cita.setFecha(data.getBeginDate());
                        new CitaDAO().update(cita);
                    }else {
                        Evento evento = new Evento(data);

                        new EventoDAO().update(evento);
                    }
                }
                 else {
                    calendarModel.add(data);
                    Evento evento = new Evento(data);

                        new EventoDAO().insert(evento);
                }
                
            case CANCEL:
                //clear the shadow of the event after editing
                calendarsEvent.clearGhost();
                calendarsEvent = null;
                break;
            }
    }   
}