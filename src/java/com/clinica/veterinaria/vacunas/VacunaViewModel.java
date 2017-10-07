package com.clinica.veterinaria.vacunas;

import com.clinica.veterinaria.especies.Especie;
import com.clinica.veterinaria.especies.EspecieDAO;
import java.util.List;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.bind.annotation.SelectorParam;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Selectbox;

/**
 *
 * 
 */
public class VacunaViewModel {
    
  
    private final VacunaDAO eventDao = new VacunaDAO();
    private final EspecieDAO especieDao = new EspecieDAO();
    private Vacuna selectedEvent, auxEvent, newEvent = new Vacuna();

    
    
    public Vacuna getSelectedEvent() {
            return selectedEvent;
    }

    public void setSelectedEvent(Vacuna selectedEvent) {
            this.auxEvent = selectedEvent;
            this.selectedEvent = selectedEvent;
    }

    public Vacuna getNewEvent() {
            return newEvent;
    }

    public void setNewEvent(Vacuna newEvent) {
            this.newEvent = newEvent;
    }

    public Vacuna getAuxEvent() {
        return auxEvent;
    }

    public void setAuxEvent(Vacuna auxEvent) {
        this.auxEvent = auxEvent;
    }


    public List<Vacuna> getEvents() {
            return eventDao.findAll();
    }

    public List<Especie> getEspecies() {
            return especieDao.findAll();
    }
    
    @Command
    public void selecBox(@SelectorParam("#cmbEspecie") Selectbox cmbEspecie){
        
        ListModel<Especie> lista = cmbEspecie.getModel();            
        if(this.selectedEvent!= null && lista.getSize() > 0){
            for(int i = 0; i<lista.getSize() ; i++){
                if(lista.getElementAt(i).getId() == this.selectedEvent.getEspecie().getId()){
                    cmbEspecie.setSelectedIndex(i);
                    break;
                }
            }
        }
    }

    @Command("add")
    @NotifyChange("events")
    public void add() {
        boolean encontrado = false;
        for(Vacuna vacuna : getEvents()) {
            if(vacuna.getNombre().equals(this.newEvent.getNombre()) && (vacuna.getEspecie().getId() == this.newEvent.getEspecie().getId())){
                encontrado = true;
            }
        }
        if(!encontrado){
            eventDao.insert(this.newEvent);
        }
        else{
            Messagebox.show("La vacuna "+this.newEvent.getNombre()+" para la especie "+ this.newEvent.getEspecie().getEspecie()+" ya se encuentra en el sistema.", "Aviso", Messagebox.OK, Messagebox.EXCLAMATION);
        }

        this.newEvent = new Vacuna();
    }

    @Command("update")
    @NotifyChange({"events", "selectedEvent"})
    public void update() {
        if(selectedEvent ==null) return;
        if(!eventDao.update(this.selectedEvent)) {
            Messagebox.show("La vacuna no se ha modificado", "Aviso", Messagebox.OK, Messagebox.EXCLAMATION);
        }
        this.selectedEvent = null;
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

