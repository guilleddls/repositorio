package com.clinica.veterinaria.clientes;

import com.clinica.veterinaria.citas.Cita;
import com.clinica.veterinaria.citas.CitaDAO;
import com.clinica.veterinaria.especies.Especie;
import com.clinica.veterinaria.especies.EspecieDAO;
import com.clinica.veterinaria.mascotas.Mascota;
import com.clinica.veterinaria.mascotas.MascotaDAO;
import com.clinica.veterinaria.mascotas.MascotaValidator;
import com.clinica.veterinaria.razas.Raza;
import com.clinica.veterinaria.razas.RazaDAO;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

public class ClienteMascotaVM {
    

    private Cita selectedCita;
    private final CitaDAO citaDao = new CitaDAO();
    private List<Mascota> mascotas = new ArrayList<>();
    private List<Raza> allRazas = new ArrayList<>();
    private final MascotaDAO eventDao = new MascotaDAO();
    private final EspecieDAO especieDao = new EspecieDAO();
    private final RazaDAO razaDao = new RazaDAO();

    private Cliente selectedEvent = new Cliente();
    private Mascota newEvent = new Mascota();
    private Mascota selectedMascota = new Mascota();
    private Especie selectedEspecie = new Especie();

    public Cliente getSelectedEvent() {
            return selectedEvent;
    }

    public void setSelectedEvent(Cliente selectedEvent) {
            this.selectedEvent = selectedEvent;
    }

    public Mascota getSelectedMascota() {
            return selectedMascota;
    }

    public void setSelectedMascota(Mascota selectedMascota) {
            this.selectedMascota = selectedMascota;
    }

    public Mascota getNewEvent() {
            return newEvent;
    }

    public void setNewEvent(Mascota newEvent) {
            this.newEvent = newEvent;
    }

    public Especie getSelectedEspecie() {
            return selectedEspecie;
    }

    public void setSelectedEspecie(Especie selectedEspecie) {
            this.selectedEspecie = selectedEspecie;
    }

//        public void selectEspecie() {
//            this.selectedEspecie = cmbEspecie.getSelectedItem().getValue();
//        }

    public List<Mascota> getEvents() {
            return eventDao.findAll();
    }

    public List<Especie> getEspecies() {
            return especieDao.findAll();
    }

    public void setAllRazas(List<Raza> allRazas) {
            this.allRazas = allRazas;
    }

    public List<Raza> getAllRazas() {
//                selectedEspecie = cmbEspecie.getSelectedItem().getValue();
            //return razaDao.findAll();
        //return razaDao.findAll("select * from zk_raza where especie = " + selectedEspecie.getId() );
            return allRazas;
    }

//        @NotifyChange("razas")
//        public List<Raza> getRazas(String especie) {
//                return razaDao.findAll("select * from zk_raza where especie= " + especie);
//        }
    /*
    public Mascota getNewMasc() {
            return newMasc;
    }

    public void setNewEvent(Mascota newMasc) {
            this.newMasc = newMasc;
    }
    */


    @GlobalCommand
    @NotifyChange({"newEvent","selectedEvent", "especies"})
    public void cambiarEspecies(){

    }

    @GlobalCommand
    @NotifyChange({"newEvent","selectedEvent", "allRazas"})
    public void cambiarRazas(){
        if(selectedEspecie != null) onSelectEspecie();

    }


    public List<Mascota> getMasc() {
            return eventDao.getClienteMascotas(this.selectedEvent);
    }


    @Init
    public void initSetup(@ContextParam(ContextType.VIEW) Component view, 
            @ExecutionArgParam("selectedCliente") Cliente selectedCliente) 
    {
        Selectors.wireComponents(view, this, false);
        this.selectedEvent = selectedCliente;
        mascotas = getMasc();
    }

    @Command("add")
    @NotifyChange("events")
    public void add(@BindingParam("cmp")  final Window x) {

        this.newEvent.setCliente(selectedEvent);  //Relacion Mascota-Cliente
        selectedEvent.asignarMascota(newEvent);   //Relacion Cliente-Mascota
        eventDao.insert(this.newEvent);

        if(MascotaValidator.validar(newEvent)){
        Messagebox.show("Se ha guardado la mascota", "Mensaje", Messagebox.OK, Messagebox.QUESTION,
            new org.zkoss.zk.ui.event.EventListener(){
                @Override
                public void onEvent(Event e){
                    if(Messagebox.ON_OK.equals(e.getName())){
                        x.detach();
                    }
                }
            }
            );
        }
        this.newEvent = new Mascota();
    }

    @Command("update")
    @NotifyChange("events")
    public void update() {
            eventDao.update(this.selectedMascota);
    }

    @Command("delete")
    @NotifyChange({"events", "selectedEvent"})
    public void delete() {
            //shouldn't be able to delete with selectedEvent being null anyway
            //unless trying to hack the system, so just ignore the request
            if(this.selectedEvent != null) {
                    eventDao.delete(this.selectedMascota);
                    this.selectedEvent = null;
            }
    }

    @Command
    @NotifyChange("allRazas")
    public void onSelectEspecie() {
            allRazas = new RazaDAO().findAll("select * from zk_raza where especie = " + selectedEspecie.getId() );
    }
        
        /* CITAS */
    @Command("deletecita")
    @NotifyChange({"citas", "selectedEvent"})
    public void deletecita() {
            //shouldn't be able to delete with selectedEvent being null anyway
            //unless trying to hack the system, so just ignore the request
            if(this.selectedCita != null) {
                    citaDao.delete(this.selectedCita);
                    this.selectedCita = null;
            }
    }
        
    @Command
    public void sendmascota(@BindingParam("cmp")  final Window x) {
        Map args = new HashMap();

            
        args.put("returnMascota", this.newEvent);
        if(MascotaValidator.validar(newEvent)){
            
            
            BindUtils.postGlobalCommand(null, null, "refreshMascotas", args);
            Messagebox.show("Se ha guardado la mascota", "Mensaje", Messagebox.OK, Messagebox.QUESTION,
                    new org.zkoss.zk.ui.event.EventListener(){
                        @Override
                        public void onEvent(Event e){
                            if(Messagebox.ON_OK.equals(e.getName())){
                                x.detach();
                            }
                        }
                    }
            );
        }
        
        
    }
        
}
