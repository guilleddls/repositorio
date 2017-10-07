package com.clinica.veterinaria.proveedores;

import com.clinica.veterinaria.poblaciones.Poblacion;
import com.clinica.veterinaria.poblaciones.PoblacionDAO;
import com.clinica.veterinaria.provincias.Provincia;
import com.clinica.veterinaria.provincias.ProvinciaDAO;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

public class ProveedorViewModel {
	
    private final ProveedorDAO eventDao = new ProveedorDAO();
    private final ProvinciaDAO provDao = new ProvinciaDAO();
    private final PoblacionDAO poblDao = new PoblacionDAO();
    private Proveedor selectedEvent, newEvent = new Proveedor();
    private Provincia selectedProvincia = new Provincia();
    private List<Poblacion> allPoblaciones = new ArrayList<>();
    private List<Proveedor> events;
    
    private String filterCif = "",
                   filterNombre = "",
                   filterDireccion = "",
                   filterCiudad = "",
                   filterProvincia = "",
                   filterTelefono = "",
                   filterTelefono2 = "",
                   filterFax = "",
                   filterEmail = "",
                   filterContacto = "";

  
    @Init
    public void initSetup(@ContextParam(ContextType.VIEW) Component view, 
                          @ExecutionArgParam("selectedProveedor") Proveedor selectedEvent) 
    { 
   
        Selectors.wireComponents(view, this, false);
        if(selectedEvent != null) {
            this.selectedEvent = selectedEvent;
            this.selectedProvincia = this.selectedEvent.getProvincia();
            this.onSelectProvincia();
        }

    }
        
    public Proveedor getSelectedEvent() {
        
        return selectedEvent;
    }

    public void setSelectedEvent(Proveedor selectedEvent) {
        this.selectedEvent = selectedEvent;
    }

    public Proveedor getNewEvent() {
        return newEvent;
    }

    public void setNewEvent(Proveedor newEvent) {
        this.newEvent = newEvent;
    }

    public List<Proveedor> getEvents() {
        if(events == null) {
                events = new ListModelList<>();//new ListModelList<Producto>(eventDao.findAll());
            }
            return events;
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

    public String getFilterCif() {
        return filterCif;
    }

    @NotifyChange
    public void setFilterCif(String filterCif) {
        this.filterCif = filterCif;
    }

    public String getFilterNombre() {
        return filterNombre;
    }

    @NotifyChange
    public void setFilterNombre(String filterNombre) {
        this.filterNombre = filterNombre;
    }

    public String getFilterDireccion() {
        return filterDireccion;
    }

    @NotifyChange
    public void setFilterDireccion(String filterDireccion) {
        this.filterDireccion = filterDireccion;
    }

    public String getFilterCiudad() {
        return filterCiudad;
    }

    @NotifyChange
    public void setFilterCiudad(String filterCiudad) {
        this.filterCiudad = filterCiudad;
    }

    public String getFilterProvincia() {
        return filterProvincia;
    }

    @NotifyChange
    public void setFilterProvincia(String filterProvincia) {
        this.filterProvincia = filterProvincia;
    }

    public String getFilterTelefono() {
        return filterTelefono;
    }

    @NotifyChange
    public void setFilterTelefono(String filterTelefono) {
        this.filterTelefono = filterTelefono;
    }

    public String getFilterTelefono2() {
        return filterTelefono2;
    }

    @NotifyChange
    public void setFilterTelefono2(String filterTelefono2) {
        this.filterTelefono2 = filterTelefono2;
    }

    public String getFilterFax() {
        return filterFax;
    }

    @NotifyChange
    public void setFilterFax(String filterFax) {
        this.filterFax = filterFax;
    }

    public String getFilterEmail() {
        return filterEmail;
    }

    @NotifyChange
    public void setFilterEmail(String filterEmail) {
        this.filterEmail = filterEmail;
    }

    public String getFilterContacto() {
        return filterContacto;
    }

    @NotifyChange
    public void setFilterContacto(String filterContacto) {
        this.filterContacto = filterContacto;
    }
    

    @Command("add")
    @NotifyChange("events")
    public void add(@BindingParam("cmp")  final Window x) {
//        this.newEvent.setId(UUID.randomUUID().variant());
        
        if(ProveedorValidator.validar(newEvent)){
            eventDao.insert(this.newEvent);
            this.newEvent = new Proveedor();    
            BindUtils.postGlobalCommand(null, null, "doSearch", null);
                Messagebox.show("Se ha guardado el Proveedor", "Mensaje", Messagebox.OK, Messagebox.QUESTION,
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

 
    
    
    @Command("update")
    @NotifyChange({"events", "selectedEvent"})
    public void update(@BindingParam("cmp")  final Window x) {
        if(ProveedorValidator.validar(selectedEvent)){
            eventDao.update(this.selectedEvent);    
            
                Messagebox.show("Se ha actualizado el Proveedor", "Mensaje", Messagebox.OK, Messagebox.QUESTION,
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
            
//            Executions.createComponents("proveedor-lista.zul", null, null);
    }

    @Command("delete")
    @NotifyChange({"events", "selectedEvent"})
    public void delete() {
      if(this.selectedEvent != null) {
            Messagebox.show("¿Desea elimimar " + selectedEvent.getNombre() + " ?", "Advertencia", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION,
                new org.zkoss.zk.ui.event.EventListener(){
                @Override
                public void onEvent(Event e){
                    if(Messagebox.ON_OK.equals(e.getName())){
                        eventDao.delete(selectedEvent);
                        selectedEvent = null;
                        BindUtils.postGlobalCommand(null, null, "doSearch", null);
                    }
                }
            }
            );
        }else
             Messagebox.show("Seleccione un producto", "Advertencia", Messagebox.OK, Messagebox.INFORMATION);
    }

    //Seleccionador de Provincia, para filtrar las poblaciones de dicha provincia
    @Command
    @NotifyChange("allPoblaciones")
    public void onSelectProvincia(){
        allPoblaciones = new PoblacionDAO().findAll("select * from zk_poblacion where provincia = " + selectedProvincia.getId() );
    }

    @Command
    public void send() {
        Map args = new HashMap();
        args.put("returnProveedor", this.selectedEvent);
        BindUtils.postGlobalCommand(null, null, "refreshProveedor", args);
    }
    
    @Command("modificarProveedor")
    @NotifyChange("events")
    public void modificarServicio()
    {
        final HashMap<String, Object> map = new HashMap<>();
        map.put("selectedProveedor", selectedEvent );
        Executions.createComponents("/proveedores/proveedor-modificar.zul", null, map);
    }
    
    @GlobalCommand
    @Command
    @NotifyChange({"doSearch", "events"})
    public void doSearch() 
    {
        events = new ListModelList<>();
        List<Proveedor> allEvents;// = eventDao.findAll(); //Para mostrar todo

        if((filterCif       == null || "".equals(filterCif))        && 
           (filterNombre    == null || "".equals(filterNombre))     &&
           (filterDireccion == null || "".equals(filterDireccion))  &&
           (filterCiudad    == null || "".equals(filterCiudad))     && 
           (filterProvincia == null || "".equals(filterProvincia))  &&
           (filterTelefono  == null || "".equals(filterTelefono))   && 
           (filterTelefono2 == null || "".equals(filterTelefono2))  &&
           (filterFax       == null || "".equals(filterFax))        &&
           (filterEmail     == null || "".equals(filterEmail))      && 
           (filterContacto  == null || "".equals(filterContacto))) {
                events.addAll(eventDao.findAll()); //Para mostrar todo
        }
        else 
        {
            allEvents = eventDao.findAll();//Quitar para mostrar todo
            for (Proveedor prod : allEvents) {
                String telefono2, fax, email, contacto;
                if(prod.getTelefono2() != null) {
                    telefono2 = prod.getTelefono2();
                }
                else {
                    telefono2 = "";
                }
                if(prod.getFax() != null) {
                    fax = prod.getFax();
                }
                else {
                    fax = "";
                }
                if(prod.getEmail() != null) {
                    email = prod.getEmail();
                }
                else {
                    email = "";
                }
                if(prod.getContacto() != null) {
                    contacto = prod.getContacto();
                }
                else {
                    contacto = "";
                }
                if ((prod.getNif().toLowerCase().startsWith(filterCif.toLowerCase())   ) &&
                        (prod.getNombre().toLowerCase().startsWith(filterNombre.toLowerCase())  )  &&
                        (prod.getDireccion().toLowerCase().startsWith(filterDireccion.toLowerCase())  ) &&
                        (prod.getPoblacion().getPoblacion().toLowerCase().startsWith(filterCiudad.toLowerCase())   ) &&
                        (prod.getProvincia().getProvincia().toLowerCase().startsWith(filterProvincia.toLowerCase())  )  &&
                        (String.valueOf(prod.getTelefono()).toLowerCase().startsWith(filterTelefono.toLowerCase())  )  &&
                        (telefono2.toLowerCase().startsWith(filterTelefono2.toLowerCase())  ) &&
                        (fax.toLowerCase().startsWith(filterFax.toLowerCase())   ) &&
                        (email.toLowerCase().startsWith(filterEmail.toLowerCase())  )  &&
                        (contacto.toLowerCase().startsWith(filterContacto.toLowerCase())  )) {
                    events.add(prod);
                }
            } //for(Mascota masc : allEvents)
        }
    }
}
