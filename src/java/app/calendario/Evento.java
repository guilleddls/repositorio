package app.calendario;

import org.zkoss.calendar.impl.SimpleCalendarEvent;

public class Evento extends SimpleCalendarEvent {
	
	private int _news_item;

	public int getNews_item() {
		return _news_item;
	}
	
	public void setNews_item(int id) {
		_news_item = id;
	}
	
	public Evento() {
		
	}
        
        public Evento(DemoCalendarEvent evento){
            this._news_item = (evento.getEvento().getNews_item());
            setHeaderColor(evento.getHeaderColor());
            setContentColor(evento.getContentColor());
            setContent(evento.getContent());
            setTitle(evento.getTitle());
            setBeginDate(evento.getBeginDate());
            setEndDate(evento.getEndDate());
            setLocked(evento.isLocked());
            
            //if(!com.clinica.veterinaria.user.UserCredentialManager.getIntance(org.zkoss.zk.ui.Sessions.getCurrent()).isAuthenticated())
            //org.zkoss.zk.ui.Executions.sendRedirect("index.zul");
        }
        
        
        
}
