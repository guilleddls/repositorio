package com.clinica.veterinaria.provincias;

import java.util.List;
import java.util.UUID;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;

public class ProvinciaViewModel {
	
	private final ProvinciaDAO eventDao = new ProvinciaDAO();
	private Provincia selectedEvent, newEvent = new Provincia();

        
	public Provincia getSelectedEvent() {
		return selectedEvent;
	}

	public void setSelectedEvent(Provincia selectedEvent) {
		this.selectedEvent = selectedEvent;
	}
	
	public Provincia getNewEvent() {
		return newEvent;
	}

	public void setNewEvent(Provincia newEvent) {
		this.newEvent = newEvent;
	}

	public List<Provincia> getEvents() {
		return eventDao.findAll();
	}
        
        public String[] getProvincias() {
            
            List<Provincia> allEvents = eventDao.findAll();
            int i=0;
            String pv[] = new String[allEvents.size()];
            
            for (Provincia prov : allEvents) {
                pv[i] = prov.getProvincia();
                i++;
            }
//            ListModel dictModel= new SimpleListModel(pv);
//            return dictModel;
            return pv;
        }
	
	@Command("add")
	@NotifyChange({"events", "newEvent"})
	public void add() {
		this.newEvent.setId(UUID.randomUUID().variant());
		eventDao.insert(this.newEvent);
		this.newEvent = new Provincia();
                BindUtils.postGlobalCommand(null, null, "refreshProvincias", null);
                BindUtils.postNotifyChange(null, null, com.clinica.veterinaria.clientes.ClienteDatosVM.class, "provincias");
	}
	
	@Command("update")
	@NotifyChange({"events", "selectedEvent"})
	public void update() {
            if(this.selectedEvent == null) return;
            eventDao.update(this.selectedEvent);
            BindUtils.postGlobalCommand(null, null, "refreshProvincias", null);
            BindUtils.postNotifyChange(null, null, com.clinica.veterinaria.clientes.ClienteDatosVM.class, "provincias");
	}
	
	@Command("delete")
	@NotifyChange({"events", "selectedEvent"})
	public void delete() {
            //shouldn't be able to delete with selectedEvent being null anyway
            //unless trying to hack the system, so just ignore the request
            if(this.selectedEvent != null) {
                    eventDao.delete(this.selectedEvent);
                    BindUtils.postGlobalCommand(null, null, "refreshProvincias", null);
                    this.selectedEvent = null;
            }
	}
}
