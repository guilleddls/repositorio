package app.calendario;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
 
import org.zkoss.calendar.api.CalendarEvent;
import org.zkoss.calendar.api.RenderContext;
import org.zkoss.calendar.impl.SimpleCalendarModel;
 
public class DemoCalendarModel extends SimpleCalendarModel {
    
     
    private String filterText = "";
 
    public DemoCalendarModel(List<CalendarEvent> calendarEvents) {
        super(calendarEvents);
    }
 
    public void setFilterText(String filterText) {
        this.filterText = filterText;
    }
 
    @Override
    public List<CalendarEvent> get(Date beginDate, Date endDate, RenderContext rc) {
        List<CalendarEvent> list = new LinkedList<>();
        long begin = beginDate.getTime();
        long end = endDate.getTime();
                 
        for (Object obj : _list) {
            CalendarEvent calEvent = obj instanceof CalendarEvent ? (CalendarEvent)obj : null;
             
            if(calEvent == null) break;
             
            long ini = calEvent.getBeginDate().getTime();
            long fin = calEvent.getEndDate().getTime();
            if (fin >= begin && 
                    ini < end && 
                    calEvent.getContent().toLowerCase().contains(filterText.toLowerCase()))
                list.add(calEvent);
        }
        return list;
    }
 
}