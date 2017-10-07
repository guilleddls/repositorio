package com.clinica.veterinaria.clientes;

import com.clinica.veterinaria.citas.Cita;
import com.clinica.veterinaria.citas.CitaDAO;
import com.clinica.veterinaria.poblaciones.Poblacion;
import com.clinica.veterinaria.poblaciones.PoblacionDAO;
import com.clinica.veterinaria.provincias.Provincia;
import com.clinica.veterinaria.provincias.ProvinciaDAO;
import com.clinica.veterinaria.util.GeneradorPass;
import com.clinica.veterinaria.util.Mail;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.Property;
import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.Validator;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.bind.validator.AbstractValidator;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

//import org.zkoss.zul.ListModelList;

public class ClienteViewModel {
    
    Session s = Sessions.getCurrent();
    private final ClienteDAO eventDao = new ClienteDAO();
    private final CitaDAO citaDao = new CitaDAO();
    private final ProvinciaDAO provDao = new ProvinciaDAO();
    private final PoblacionDAO poblDao = new PoblacionDAO();
    
   
    
    private Cliente selectedEvent, newEvent = new Cliente();
    private Provincia selectedProvincia = new Provincia();
    private Cita newCita = new Cita();
    
    private List<Poblacion> allPoblaciones = new ArrayList<>();

    private String filterNombre = "",
                   filterApellidos = "",
                   filterDni = "",
                   filterDireccion = "",
                   filterCiudad = "",
                   filterProvincia =  "",
                   filterFechaalta = "", 
                   filterTelefono = "", 
                   filterTelefono2="",
                   filterCodigopostal ="",
                   filterEmail = "",
                   filterSaldoDeudor = "",
                   filterBoletasImpagas = "";
    
    private ListModelList<Cliente> clientes;

    @Init
    public void initSetup(@ContextParam(ContextType.VIEW) Component view, 
            @ExecutionArgParam("selectedCliente") Cliente selectedCliente) 
    {
        Selectors.wireComponents(view, this, false);
        if(selectedCliente != null)
            this.selectedEvent = selectedCliente;
    }
    

    @Command
    public void showDeudor(){
        final HashMap<String, Object> map = new HashMap<>();
        map.put("selectedCliente", selectedEvent );
        Executions.createComponents("/deudores/deudor-ventas.zul",null,map);

    }
    
    
    
    /******* Parte Movil*************/
   
    /**
     * Parte Movil
     * @param w
     */
    @Command
    @NotifyChange("selectedEvent")
    public void ok(@BindingParam("cmp") Window w){
        
        selectedEvent.setPassword(GeneradorPass.getNext());
        
        if(eventDao.updateMovil(selectedEvent)){  
            Clients.showNotification("Se ha enviado un e-mail con la contraseña al usuario", Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 3500);
            Mail.mailPassword(selectedEvent);
            w.detach();
        }
    }
    
    @Command
    @NotifyChange("selectedEvent")
    public void cancel(@BindingParam("cmp") Window w){
       w.detach();
        
    }
    
    @Command("showMovil")
    
    public void showMovil(){
        if(selectedEvent != null){
            final HashMap<String, Object> map = new HashMap<>();
            map.put("selectedCliente", selectedEvent );
        
            Executions.createComponents("/clientes/cliente-movil.zul", null, map);
      
        }
    }
    
    public Validator getValidarMovil(){
        return new AbstractValidator(){
            @Override
            public void validate(ValidationContext ctx) {
                Map<String,Property> formData = ctx.getProperties(ctx.getProperty().getValue());
                String usuario = String.valueOf(formData.get("usuario").getValue());
                String email = String.valueOf(formData.get("email").getValue());
                
                if(usuario == null){
                    addInvalidMessage(ctx, "usuario","Ingrese un usuario");
                } else if(usuario.isEmpty() ){
                    addInvalidMessage(ctx, "usuario","Ingrese un usuario");
                } else if(eventDao.existeUsuario(usuario) && !selectedEvent.getUsuario().equals(usuario)){
                    addInvalidMessage(ctx, "usuario","El usuario ya existe");
                }
                
                if(email == null){
                    addInvalidMessage(ctx, "email","Ingrese un e-mail");
                } else if(email.isEmpty()){
                    addInvalidMessage(ctx, "email","Ingrese un e-mail");
                } else if (eventDao.existeEmail(email) && !selectedEvent.getEmail().equals(email)){
                    addInvalidMessage(ctx, "email","El email ya existe");
                } else if (!email.matches(".+@.+\\.[a-z]+")){
                    addInvalidMessage(ctx, "email","El e-mail es invalido");
                }
            }
        };
    }
    
    /***********************************/
    
    /**
     *
     * @return
     */
    public Cita getNewCita() {
        return newCita;
    }

    public void setNewCita(Cita newCita) {
        this.newCita = newCita;
    }

        
    public Cliente getSelectedEvent() {
            return selectedEvent;
    }

    public void setSelectedEvent(Cliente selectedEvent) {
            this.selectedEvent = selectedEvent;
    }

    public Cliente getNewEvent() {
            return newEvent;
    }

    public void setNewEvent(Cliente newEvent) {
            this.newEvent = newEvent;
    }
    
    public List<Cliente> getEvents() {
            return eventDao.findAll();
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
    
    
    
    

    @GlobalCommand
    @NotifyChange({"provincias","allPoblaciones"})
    public void refreshProvincias() {}
    
    @GlobalCommand
    @NotifyChange("allPoblaciones")
    public void cambiarPoblaciones(){
        if(selectedProvincia != null) 
            onSelectProvincia();
        else if(this.selectedEvent != null){
            if(this.selectedEvent.getProvincia() != null)
                allPoblaciones = new PoblacionDAO().findAll("select * from zk_poblacion where provincia = " + selectedEvent.getProvincia().getId() );
        }
            
        
    }
    
    @Command
    @NotifyChange({"events", "clientes"})
    public void add(@BindingParam("cmp")  final Window x) {
//            this.newEvent.setId(UUID.randomUUID().variant());
            if(eventDao.insert(this.newEvent)){
                BindUtils.postGlobalCommand(null, null, "doSearch", null);
                
                this.newEvent = new Cliente();
                Messagebox.show("El cliente se ha guardado", "Mensaje", Messagebox.OK, Messagebox.QUESTION,
                    new org.zkoss.zk.ui.event.EventListener(){
                        @Override
                        public void onEvent(Event e){
                            if(Messagebox.ON_OK.equals(e.getName())){
                                x.detach();
                                
                            }
                        }
                    }
                );
                
                //Executions.sendRedirect("cliente-template.zul");
            }
            else{
                Messagebox.show("Cliente no añadido", "Error", Messagebox.OK, Messagebox.ERROR);
                this.newEvent = new Cliente();
            }
            
    }

    @Command("update")
    @NotifyChange({"events", "clientes","selectedEvent"})
    public void update() {
            if(!eventDao.update(this.selectedEvent)){
                Messagebox.show("Cliente no actualizado", "Error", Messagebox.OK, Messagebox.ERROR);
            }
    }

    @Command("delete")
    @NotifyChange({"events", "clientes", "selectedEvent"})
    public void delete() {
        if(this.selectedEvent != null) {
            
            Messagebox.show("¿Desea eliminar " + selectedEvent.getFullname() + "?", "Advertencia", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION,
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
            Messagebox.show("Seleccione una fila", "Información",Messagebox.OK, null,null);
    }

    @Command
    public void showMascotas()
    {
        
        final HashMap<String, Object> map = new HashMap<>();
        map.put("selectedCliente", selectedEvent );
        //Window win = (Window) page.getFellow("win");
        //Borderlayout bl = (Borderlayout) Path.getComponent("/main");
        //Center center = bl.getCenter();
        //center.getChildren().clear();
        Executions.createComponents("cliente-mascota.zul", null, map);
    }
    
    @Command
    public void showCliente()
    {
        if(selectedEvent == null) return;
        if(selectedEvent.getFullname().toLowerCase().contains("consumidor")){
            Clients.showNotification("No se puede modificar CF", Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 3500);
            return;
        }
        final HashMap<String, Object> map = new HashMap<>();
        map.put("selectedCliente", selectedEvent );
        //Window win = (Window) page.getFellow("win");
        //Borderlayout bl = (Borderlayout) Path.getComponent("/main");
        //Div asd = (Div)Path.getComponent("/bloque");
        //asd.getChildren().clear();
        //Center center = bl.getCenter();
        //center.getChildren().clear();
        
        Executions.createComponents("/clientes/cliente.zul", null, map);
    }
    
    @Command
    public void showCitas()
    {
        final HashMap<String, Object> map = new HashMap<>();
        map.put("selectedCliente", selectedEvent );
        //Window win = (Window) page.getFellow("win");
        //Borderlayout bl = (Borderlayout) Path.getComponent("/main");
       
        
        //Center center = bl.getCenter();
        //center.getChildren().clear();
        Executions.createComponents("cliente-cita.zul", null, map);
    }

    @Command
    public void asignarMascota()
    {
        final HashMap<String, Object> map = new HashMap<>();
        map.put("selectedCliente", selectedEvent );
        //Window win = (Window) page.getFellow("win");
        //Borderlayout bl = (Borderlayout) Path.getComponent("/main");
        //Center center = bl.getCenter();
        //center.getChildren().clear();
        Executions.createComponents("/mascotas/mascota-asignar.zul", null, map);
        //Executions.createComponents("/clientes/cliente-nueva-mascota.zul", null, map);
    }

    @Command
    public void asignarCita()
    {
        final HashMap<String, Object> map = new HashMap<>();
        map.put("selectedCliente", selectedEvent );
        //Window win = (Window) page.getFellow("win");
        //Borderlayout bl = (Borderlayout) Path.getComponent("/main");
        //Center center = bl.getCenter();
        //center.getChildren().clear();
        Executions.createComponents("/clientes/cliente-nueva-cita.zul", null, map);
    }
    
        /* Se manda desde mascota-vacuna-nueva.zul */
    @GlobalCommand
    @NotifyChange({"vacunas", "citas"})
    public void refreshCitas(@BindingParam("returnCita") Cita item)
    {
        /*
            this.newCita = item;
            this.newCita.setCliente(selectedEvent);
            this.newCita.setEmpleado(UserCredentialManager.getIntance(s).getUser() );
            if(!citaDao.insert(this.newCita)){
                Messagebox.show("Cita no añadida", "Error", Messagebox.OK, Messagebox.ERROR);
            }
        */
            this.newCita = new Cita();
        
    }

    @Command("addCita")
    @NotifyChange({"clientes", "events"})
    public void addCita() {
//            this.newCita.setId(UUID.randomUUID().variant());
//		this.newCita.setCliente(selectedEvent);  //Relacion Mascota-Cliente
//                selectedEvent.asignarMascota(newEvent);   //Relacion Cliente-Mascota
            if(!citaDao.insert(this.newCita)){
                Messagebox.show("Cita no añadida", "Error", Messagebox.OK, Messagebox.ERROR);
            }
            this.newCita = new Cita();
    }

    /* FILTRADO */

    public String getFilterNombre() {
        return filterNombre;
    }

    public String getFilterApellidos() {
        return filterApellidos;
    }
    
    public String getFilterDni() {
        return filterDni;
    }

    public String getFilterSaldoDeudor() {
        return filterSaldoDeudor;
    }

    public String getFilterBoletasImpagas() {
        return filterBoletasImpagas;
    }

    @NotifyChange
    public void setFilterBoletasImpagas(String filterBoletasImpagas) {
        this.filterBoletasImpagas = filterBoletasImpagas;
    }
    
    @NotifyChange
    public void setFilterSaldoDeudor(String filterSaldoDeudor) {
        this.filterSaldoDeudor = filterSaldoDeudor;
    }

    @NotifyChange
    public void setFilterNombre(String filterNombre) {
        this.filterNombre = filterNombre;
    }

    @NotifyChange
    public void setFilterApellidos(String filterApellidos) {
        this.filterApellidos = filterApellidos;
    }

    @NotifyChange
    public void setFilterDni(String filterDni) {
        this.filterDni = filterDni;
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

    public String getFilterFechaalta() {
        return filterFechaalta;
    }
    
    @NotifyChange
    public void setFilterFechaalta(String filterFechaalta) {
        this.filterFechaalta = filterFechaalta;
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

    public String getFilterEmail() {
        return filterEmail;
    }

    @NotifyChange
    public void setFilterEmail(String filterEmail) {
        this.filterEmail = filterEmail;
    }

    public String getFilterCodigopostal() {
        return filterCodigopostal;
    }

    @NotifyChange
    public void setFilterCodigopostal(String filterCodigopostal) {
        this.filterCodigopostal = filterCodigopostal;
    }
    
    
    // Buscador para hacer el filtrado
    @GlobalCommand
    @Command 
    @NotifyChange("clientes")
    public void doSearch() {      
        String[] listaFiltro = {filterTelefono, filterTelefono2, filterEmail, filterApellidos,filterNombre, filterDni, filterDireccion, filterCiudad, filterProvincia, filterCodigopostal, filterFechaalta}; 
        clientes = new ListModelList<>();
        if(areNullOrEmpty(listaFiltro)) {
              clientes.addAll(eventDao.findAll());
        }
        else {
            for (Cliente c : eventDao.findAll()) {
                Object[] cliente = {c.getTelefono(), c.getTelefono2(), c.getEmail(), c.getApellidos(), c.getNombre(), c.getNif(), c.getDireccion(), c.getCiudad(), c.getProvincia(), c.getCodigopostal(), c.getFechaalta()};          
                if (filtrar(cliente, listaFiltro))  {
                    clientes.add(c);
                }
            }
        }
    }
    
    @GlobalCommand
    @Command 
    @NotifyChange("clientes")
    public void doSearchDeudores() {      
        String[] listaFiltro = {filterApellidos,filterNombre, filterDireccion, filterCiudad , filterBoletasImpagas, filterSaldoDeudor}; 
        clientes = new ListModelList<>();
        if(areNullOrEmpty(listaFiltro)) {
              clientes.addAll(eventDao.findDeudores());
        }
        else {
            for (Cliente c : eventDao.findDeudores()) {
                Object[] cliente = {c.getApellidos(), c.getNombre(), c.getDireccion(), c.getCiudad(), c.getBoletasImpagas(), c.getSaldoDeudor()};          
                if (filtrar(cliente, listaFiltro))  {
                    clientes.add(c);
                }
            }
        }
        
    }
    
    
    
    // Metodos para el doSearch
    private boolean filtrar(Object[] obj, String[] filtros){
        String ivastring;
        for (int i =0; i < obj.length ; i++){ 
            ivastring = String.valueOf(obj[i]).toLowerCase();      
            if (!ivastring.startsWith(filtros[i].toLowerCase())) return false;
        }
        return true;
    }
    
    // True: si son todos nulos o todos vacios
    private boolean areNullOrEmpty(String[] text){
        for (String txt : text) {
            if (txt != null ) { //Si es nulo  no puede ser vacio
                 if ( !txt.isEmpty() ) return false; //Sino es nulo puede ser vacio
            }
           
        }
        return true;
    }
    

    /* FIN FILTER*/
    
    public ListModelList<Cliente> getClientes() 
    {
        if(clientes == null) {
            clientes = new ListModelList<>();//new ListModelList<Cliente>(getEvents());
        }

        return clientes;
    }

    @GlobalCommand
    @NotifyChange("clientes")
    public void refreshClientes(){
        
    }
    
    //Seleccionador de Provincia, para filtrar las poblaciones de dicha provincia
    @Command
    @NotifyChange("allPoblaciones")
    public void onSelectProvincia()
    {
            allPoblaciones = new PoblacionDAO().findAll("select * from zk_poblacion where provincia = " + selectedProvincia.getId() );
    }
    
    public void refreshPage() {
//        final HashMap<String, Object> map = new HashMap<String, Object>();
//            map.put("selectedPedido", selectedPedido );
            //Window win = (Window) page.getFellow("win");
            //Borderlayout bl = (Borderlayout) Path.getComponent("/main");
            //Center center = bl.getCenter();
            //center.getChildren().clear();
            Executions.createComponents("cliente-lista.zul", null, null);
    }
    
    
    @Command
    public void sendRapido() {
        Map args = new HashMap();
        eventDao.insert(newEvent);
        args.put("returnCliente", this.newEvent);
        BindUtils.postGlobalCommand(null, null, "refreshCliente", args);
    }
    
    @Command
    public void send() {
        Map args = new HashMap();
        args.put("returnCliente", this.selectedEvent);
        BindUtils.postGlobalCommand(null, null, "refreshCliente", args);
    }
    
    @Command
    public void modificarPropietario() {
        Map args = new HashMap();
        args.put("returnCliente", this.selectedEvent);
        BindUtils.postGlobalCommand(null, null, "refreshModificarPropietario", args);
    }
    
    
}
