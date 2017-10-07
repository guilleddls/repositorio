package com.clinica.veterinaria.poblaciones;

import com.clinica.veterinaria.provincias.Provincia;
import com.clinica.veterinaria.provincias.ProvinciaDAO;
import java.util.ArrayList;
import java.util.List;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zul.Selectbox;

public class PoblacionViewModel {
        private List<Poblacion> allPoblaciones = new ArrayList<>();
	private final Provincia selectedProvincia = new Provincia();
	private final PoblacionDAO eventDao = new PoblacionDAO();
	private Poblacion selectedEvent, newEvent = new Poblacion();
        private final ProvinciaDAO provDao = new ProvinciaDAO();
        
	public Poblacion getSelectedEvent() {
		return selectedEvent;
	}

	public void setSelectedEvent(Poblacion selectedEvent) {
		this.selectedEvent = selectedEvent;
	}
	
	public Poblacion getNewEvent() {
		return newEvent;
	}

	public void setNewEvent(Poblacion newEvent) {
		this.newEvent = newEvent;
	}

	public List<Poblacion> getEvents() {
		return eventDao.findAll();
	}
        
        public List<Provincia> getProvincias(){
            return provDao.findAll();
        }
        
       
    //Seleccionador de Provincia, para filtrar las poblaciones de dicha provincia
    @Command
    @NotifyChange("allPoblaciones")
    public void onSelectProvincia()
    {
            allPoblaciones = new PoblacionDAO().findAll("select * from zk_poblacion where provincia = " + selectedProvincia.getId() );
    }
        
        
    @Command("add")
    @NotifyChange({"events", "newEvent"})
    public void add(@BindingParam("cmp")  final Selectbox selboxProvincia) {
        int indice = selboxProvincia.getSelectedIndex() + 1;
        if(this.newEvent.getPoblacion() != null && 
                indice > -1)
        {
            if(this.newEvent.getProv() == null) {
                Provincia prov = (Provincia) selboxProvincia.getModel().getElementAt(selboxProvincia.getSelectedIndex());
                this.newEvent.setProv(prov);
            }
            eventDao.insert(this.newEvent);
            this.newEvent = new Poblacion();
           
            BindUtils.postGlobalCommand(null, null, "cambiarPoblaciones", null);
            BindUtils.postGlobalCommand(null, null, "cambiarPoblacion", null);
        }
    }
	
    @Command("update")
    @NotifyChange({"events", "selectedEvent"})
    public void update(@BindingParam("cmp")  final Selectbox selboxProvincia) {
        if(this.selectedEvent.getPoblacion() != null && 
                selboxProvincia.getSelectedIndex() > -1)
        {
            if(this.selectedEvent.getProv() == null) {
                Provincia prov = (Provincia) selboxProvincia.getModel().getElementAt(selboxProvincia.getSelectedIndex());
                this.selectedEvent.setProv(prov);
            }
            eventDao.update(this.selectedEvent);
            this.selectedEvent = new Poblacion();
            selboxProvincia.setSelectedIndex(-1);
            BindUtils.postGlobalCommand(null, null, "cambiarPoblaciones", null);
            BindUtils.postGlobalCommand(null, null, "cambiarPoblacion", null);
        }
    }

    @Command("delete")
    @NotifyChange({"events", "selectedEvent"})
    public void delete() {
            //shouldn't be able to delete with selectedEvent being null anyway
            //unless trying to hack the system, so just ignore the request
            if(this.selectedEvent != null) {
                    eventDao.delete(this.selectedEvent);
                    this.selectedEvent = null;
            }
    }
}
