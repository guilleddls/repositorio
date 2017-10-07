/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.clinica.veterinaria.user;

import com.clinica.veterinaria.poblaciones.Poblacion;
import com.clinica.veterinaria.poblaciones.PoblacionDAO;
import com.clinica.veterinaria.provincias.Provincia;
import com.clinica.veterinaria.provincias.ProvinciaDAO;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.bind.annotation.SelectorParam;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Selectbox;

/**
 *
 * 
 */
public class UserEmergenteVM {
    //private final UserDAO eventDao = new UserDAO();
    User user = new User();
    private User selectedEvent, newEvent = new User();
    
    private final ProvinciaDAO provDao = new ProvinciaDAO();
    private final PoblacionDAO poblDao = new PoblacionDAO();
    private Provincia selectedProvincia = new Provincia();
    private List<Poblacion> allPoblaciones = new ArrayList<>();
    private int auxTipo;
    
    
    @Init
    public void initSetup(@ContextParam(ContextType.VIEW) Component view, 
                          @ExecutionArgParam("selectedUsuario") User selectedEvent)
                           {    
        Selectors.wireComponents(view, this, false);
        this.selectedEvent = selectedEvent;
        
       
    }
   
    @AfterCompose
    public void afterCompose(@SelectorParam("#lstbTipo") Listbox lstbTipo,
                             @SelectorParam("#provincias") Selectbox provincias,
                             @SelectorParam("#poblaciones") Selectbox poblaciones){
        if(selectedEvent == null ) return;
        if(selectedEvent.getId() != 0){
            
            for(int i =0; i< lstbTipo.getItems().size();i++){
                if(String.valueOf(selectedEvent.getTipo()).equals(lstbTipo.getItems().get(i).getValue().toString())){
                    lstbTipo.setSelectedIndex(i);
                    break;
                }             
            }
            /*
            for(int i =0; i< provincias.getModel().getSize(); i++){
                if(selectedEvent.getProvincia().getProvincia().equals(provincias.getModel().getElementAt(i).toString())){
                    provincias.setSelectedIndex(i);
                    break;
                }

            }
            
            for(int i =0; i< poblaciones.getModel().getSize(); i++){
                if(selectedEvent.getPoblacion().getPoblacion().equals(provincias.getModel().getElementAt(i).toString())){
                    provincias.setSelectedIndex(i);
                    break;
                }

            }
                    */
        }
    }
    
    @Command
    public void cargarCombo( ){
        
    }
    
    public User getSelectedEvent() {
        return selectedEvent;
    }

    public void setSelectedEvent(User selectedEvent) {
        this.selectedEvent = selectedEvent;
    }

    public User getNewEvent() {
        return newEvent;
    }

    public void setNewEvent(User newEvent) {
        this.newEvent = newEvent;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    
    public Provincia getSelectedProvincia() {
        return selectedProvincia;
    }

    public void setSelectedProvincia(Provincia selectedProvincia) {
        this.selectedProvincia = selectedProvincia;
    }

    public List<Provincia> getProvincias() {
        return provDao.findAll();
    }

    public List<Poblacion> getPoblaciones() {
        return poblDao.findAll();
    }

    public List<Poblacion> getAllPoblaciones() {
        return allPoblaciones;
    }
    
    //Seleccionador de Provincia, para filtrar las poblaciones de dicha provincia
    @Command
    @NotifyChange("allPoblaciones")
    public void onSelectProvincia(){
        allPoblaciones = new PoblacionDAO().findAll("select * from zk_poblacion where provincia = " + selectedProvincia.getId() );
    }
    
    @Command
    public void sendNew() {
        
        Map args = new HashMap();
        if(this.newEvent.getTipo() != 0 && 
           this.newEvent.getPoblacion() != null && 
           this.newEvent.getProvincia() != null){
            args.put("returnNewUsuario", this.newEvent);
            BindUtils.postGlobalCommand(null, null, "refreshNewUsuario", args);
        }
        else{
            Messagebox.show("Existen campos obligatorios vacíos", "Error", Messagebox.OK, Messagebox.ERROR);
        }
    }
    
    @Command
    public void sendUpdate() {
        
        Map args = new HashMap();
        if(this.selectedEvent.getTipo() != 0 &&
           this.selectedEvent.getPoblacion().getId() != 0 &&
           this.selectedEvent.getProvincia().getId() != 0){
            args.put("returnUpdateUsuario", this.selectedEvent);
            BindUtils.postGlobalCommand(null, null, "refreshUpdateUsuario", args);
        }else{
            Messagebox.show("Existen campos obligatorios vacíos", "Error", Messagebox.OK, Messagebox.ERROR);
        }
    }
    
    
    
}
