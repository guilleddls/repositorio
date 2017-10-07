package com.clinica.veterinaria.clientes;

import app.pagos.Pago;
import app.pagos.PagoDAO;
import com.clinica.veterinaria.citas.Cita;
import com.clinica.veterinaria.citas.CitaDAO;
import com.clinica.veterinaria.especies.Especie;
import com.clinica.veterinaria.especies.EspecieDAO;
import com.clinica.veterinaria.mascotas.Mascota;
import com.clinica.veterinaria.mascotas.MascotaDAO;
import com.clinica.veterinaria.poblaciones.Poblacion;
import com.clinica.veterinaria.poblaciones.PoblacionDAO;
import com.clinica.veterinaria.provincias.Provincia;
import com.clinica.veterinaria.provincias.ProvinciaDAO;
import com.clinica.veterinaria.razas.Raza;
import com.clinica.veterinaria.razas.RazaDAO;
import com.clinica.veterinaria.user.UserCredentialManager;
import com.clinica.veterinaria.ventas.Venta;
import com.clinica.veterinaria.ventas.VentaDAO;
import com.clinica.veterinaria.ventas.VentaViewModel;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.Validator;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.bind.annotation.SelectorParam;
import org.zkoss.bind.validator.AbstractValidator;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Menuitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Selectbox;
import org.zkoss.zul.Textbox;


/**
 *
 * 
 */
public class ClienteDatosVM {
        

    Session s = Sessions.getCurrent();
    private List<Cita>    citas    = new ArrayList<>();
    private List<Mascota> mascotas = new ArrayList<>();
    private List<Venta>   ventas   = new ArrayList<>();
    private List<Raza>    allRazas = new ArrayList<>();
    private List<Poblacion> allPoblaciones = new ArrayList<>();
    
    private final ClienteDAO eventDao = new ClienteDAO();
    private final MascotaDAO mascoDao = new MascotaDAO();
    private final EspecieDAO especieDao = new EspecieDAO();
//    private RazaDAO razaDao = new RazaDAO();
    private final CitaDAO citaDao = new CitaDAO();
    private final VentaDAO ventaDao = new VentaDAO();
    private final ProvinciaDAO provDao = new ProvinciaDAO();
    private final PoblacionDAO poblDao = new PoblacionDAO();

    private Cliente newEvent = new Cliente();
    private Mascota newMasc  = new Mascota();
    private Cita newCita = new Cita();
    private Venta newVenta = new Venta();
    
    private Cliente selectedEvent = new Cliente();
    private Cita selectedCita = new Cita();
    private Mascota selectedMascota = new Mascota();
    private Especie selectedEspecie = new Especie();
    private Venta selectedVenta = new Venta();
    private Provincia selectedProvincia = new Provincia();
    private float saldoDeudor = 0;
    
    
    private Cliente auxCliente = new Cliente();
    
    private Menuitem updateDatos;
    private Menuitem modificarDatos;
    private Menuitem cancelarDatos;
    private Textbox txtEmail;
    private Intbox intTelefono;
    private Intbox intTelefono2;
  
    private Selectbox poblaciones;
    private Grid direccion;
    private Grid direccionMod;
    private Label labelApellidos;
    private Label labelCUIT;
    private Label labelCiudad;
    private Label labelProvincia;
    private Label labelNombre;
  
    private Label labelDireccion;
    private Textbox txtCUIT;
    private Textbox txtNombre;
    private Textbox txtApellidos;
    private Textbox txtDireccion;
    private Selectbox provincias;
    
    
    
    /*
     * Inicio de la carga 
     */
    @Init
    public void initSetup(@ContextParam(ContextType.VIEW) Component view, 
            @ExecutionArgParam("selectedCliente") Cliente selectedCliente
            ) 
    {
        Selectors.wireComponents(view, this, false);
        this.selectedEvent = selectedCliente;
        this.auxCliente = selectedCliente;
        saldoDeudor = selectedCliente.getSaldoDeudor();

        
    }
    

    @AfterCompose
    public void afterCompose(@SelectorParam("#updateDatos") Menuitem updateDatos,
                             @SelectorParam("#modificarDatos") Menuitem modificarDatos,
                             @SelectorParam("#cancelarDatos") Menuitem cancelarDatos,
                             @SelectorParam("#txtEmail") Textbox txtEmail,
                            
                             @SelectorParam("#intTelefono") Intbox intTelefono,
                             @SelectorParam("#intTelefono2") Intbox intTelefono2,
                             @SelectorParam("#poblaciones") Selectbox poblaciones,
                             @SelectorParam("#Direccion") Grid direccion,
                             @SelectorParam("#DireccionMod") Grid direccionMod,
                             @SelectorParam("#labelCUIT") Label labelCUIT,
                         
                            @SelectorParam("#labelNombre") Label labelNombre,

                            @SelectorParam("#labelApellidos") Label labelApellidos,

                            @SelectorParam("#labelDireccion") Label labelDireccion,

                            @SelectorParam("#labelCiudad") Label labelCiudad,

                            @SelectorParam("#labelProvincia") Label labelProvincia,
                          
                             @SelectorParam("#txtCUIT") Textbox txtCUIT,                          
                           @SelectorParam("#txtNombre") Textbox txtNombre,                         
                           @SelectorParam("#txtApellidos") Textbox txtApellidos,                         
                           @SelectorParam("#txtDireccion") Textbox txtDireccion,                       
                           @SelectorParam("#provincias") Selectbox provincias
 
            ){
        
        
        //onCreate="@command('onChangeProvincia')"
        onChangeProvincia();
        this.updateDatos = updateDatos;
        this.modificarDatos = modificarDatos;
        this.cancelarDatos = cancelarDatos;
        this.txtEmail = txtEmail;
     
        this.intTelefono = intTelefono;
        this.intTelefono2 = intTelefono2;
        this.poblaciones = poblaciones;
        this.direccion = direccion;
        this.direccionMod = direccionMod;
        this.labelApellidos = labelApellidos;
        this.labelCUIT = labelCUIT;
        this.labelCiudad = labelCiudad;
        this.labelProvincia = labelProvincia;
        this.labelNombre = labelNombre;
        
        this.labelDireccion = labelDireccion;
        this.txtCUIT = txtCUIT;
        this.txtNombre = txtNombre;
        this.txtApellidos = txtApellidos;
        this.txtDireccion = txtDireccion;
        this.provincias = provincias;
        
        
    }


    
    public Cliente getSelectedEvent() {
            return selectedEvent;
    }

    public void setSelectedEvent(Cliente selectedEvent) {
            this.selectedEvent = selectedEvent;
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
    public Mascota getSelectedMascota() {
            return selectedMascota;
    }

    public void setSelectedMascota(Mascota selectedMascota) {
            this.selectedMascota = selectedMascota;
    }
    
    public Venta getSelectedVenta() {
            return selectedVenta;
    }

    public void setSelectedVenta(Venta selectedVenta) {
            this.selectedVenta = selectedVenta;
            System.out.println("SelectedVenta: " + this.selectedVenta.getCliente().getFullname());
    }
    
    public Cita getSelectedCita() {
            return selectedCita;
    }

    public void setSelectedCita(Cita selectedCita) {
            this.selectedCita = selectedCita;
            System.out.println("SelectedCita: " + this.selectedCita.getCliente().getFullname());
    }

    public Cliente getNewEvent() {
            return newEvent;
    }

    public void setNewEvent(Cliente newEvent) {
            this.newEvent = newEvent;
    }

    public Especie getSelectedEspecie() {
            return selectedEspecie;
    }

    public void setSelectedEspecie(Especie selectedEspecie) {
            this.selectedEspecie = selectedEspecie;
    }

    public List<Cliente> getEvents() {
            return eventDao.findAll();
    }

    public List<Especie> getEspecies() {
            return especieDao.findAll();
    }

    public void setAllRazas(List<Raza> allRazas) {
            this.allRazas = allRazas;
    }

    public List<Raza> getAllRazas() {
            return allRazas;
    }

    public List<Mascota> getMasc() {
            return mascoDao.getClienteMascotas(this.selectedEvent);
    }
    
    public List<Mascota> getMascotas() {
        return mascoDao.findAll("select * from zk_mascota where id_cliente=" + selectedEvent.getId());
    }
        
    public List<Cita> getCitas() {
        return citaDao.findAll("select * from zk_cita where id_cliente=" + selectedEvent.getId());
    }
    
    
    @Command
    public void showPago(){
        if(selectedVenta== null) return;
        final HashMap<String, Object> map = new HashMap<>();
        map.put("selectedVenta", selectedVenta );
        Executions.createComponents("/deudores/pago.zul",null,map);
    }
    
    @Command
    public void showPagosHistorial(){
        if(selectedVenta== null) return;
        final HashMap<String, Object> map = new HashMap<>();
        map.put("selectedVenta", selectedVenta );
        Executions.createComponents("/deudores/pagos-historial.zul",null,map);
    }
    
    
    public List<Venta> getVentas() {
        return ventaDao.findByCliente(selectedEvent.getId());
    }

    
    public List<Venta> getBoletas(){
        List<Venta> boletas = new ArrayList<>();
        saldoDeudor = 0;
        for(Venta v : getVentas()){
            if(v.getMonto_deudor() > 0){
                boletas.add(v);
                saldoDeudor += v.getMonto_deudor();
            }
        }
        return boletas;
    }
    
    public float getSaldoDeudor(){
        return saldoDeudor;
    }
    
    public void setSaldoDeudor(float saldoDeudor){
        this.saldoDeudor = saldoDeudor;
    }
    
    
    @GlobalCommand
    @NotifyChange({"boletas", "saldoDeudor"})
    public void refreshBoletas(){
        BindUtils.postGlobalCommand(null, null, "doSearchDeudores", null);
        //Refresca getBoletas();
    }
    
    
    @Command("showBoleta")
    public void showBoleta(){
        final HashMap<String, Object> map = new HashMap<>();
        map.put("selectedVenta", selectedVenta );
        Executions.createComponents("/deudores/venta-linea.zul",null,map);

    }
    
    // Deudores
    @Command("newBoleta")
    @NotifyChange({"boletas", "selectedVenta"})
    public void newBoleta() { //Añadir nuevo cliente
        final HashMap<String, Object> map = new HashMap<>();
        map.put("selectedCliente", selectedEvent );
        Executions.createComponents("/deudores/venta-linea.zul",null,map);
      
    }
    
    @Command("deleteBoleta")
    @NotifyChange({"boletas", "selectedVenta"})
    public void deleteBoleta() {
        if(this.selectedVenta != null) { 
            if(!ventaDao.delete(this.selectedVenta)){
                Messagebox.show("Factura no eliminada", "Error", Messagebox.OK, Messagebox.ERROR);
            }
            this.selectedVenta = null;
        }
    }
    
   
    
    
    @Command("add")
    @NotifyChange({"events","clientes"})
    public void add() { //Añadir nuevo cliente       
        if(!eventDao.insert(this.newEvent)){
            Messagebox.show("Datos del cliente no insertados", "Error", Messagebox.OK, Messagebox.ERROR);
        }
        this.newEvent = new Cliente();
    }

    @Command("update")
    @NotifyChange({"events", "selectedEvent","clientes"})
    public void update() {
        if(!eventDao.update(this.selectedEvent)){
            Messagebox.show("Datos del cliente no actualizados", "Error", Messagebox.OK, Messagebox.ERROR);
            this.selectedEvent = this.auxCliente;
        }
    }

    @Command("delete")
    @NotifyChange({"events", "selectedEvent","clientes"})
    public void delete() {
        //shouldn't be able to delete with selectedEvent being null anyway
        //unless trying to hack the system, so just ignore the request
        if(this.selectedEvent != null) {
            if(!eventDao.delete(this.selectedEvent)){
                Messagebox.show("Cliente no eliminado", "Error", Messagebox.OK, Messagebox.ERROR);
            }
            this.selectedEvent = null;
        }
    }

    /*
     * Mascotas
     */
    
    @Command("addmasc")
    @NotifyChange("mascotas")
    public void addmmasc() {
        this.newMasc.setCliente(selectedEvent);  //Relacion Mascota-Cliente
        selectedEvent.asignarMascota(newMasc);   //Relacion Cliente-Mascota
        if(!mascoDao.insert(this.newMasc)){
            Messagebox.show("Mascota no añadida", "Error", Messagebox.OK, Messagebox.ERROR);
        }
        this.newMasc = new Mascota();
    }

    @Command("updatemasc")
    @NotifyChange("eventsmasc")
    public void updatemasc() {
            if(!mascoDao.update(this.selectedMascota)){
                Messagebox.show("Mascota no actualizada", "Error", Messagebox.OK, Messagebox.ERROR);
            }
    }

    @Command("deletemasc")
    @NotifyChange({"eventsmasc", "selectedMascota","selectedEvent", "mascotas"})
    public void deletemasc() {
            //shouldn't be able to delete with selectedEvent being null anyway
            //unless trying to hack the system, so just ignore the request
            if(this.selectedMascota != null) {
                if(!mascoDao.delete(this.selectedMascota)){
                    Messagebox.show("Mascota no eliminada", "Error", Messagebox.OK, Messagebox.ERROR);
                }
                this.selectedMascota = null;
            }
    }
    
    @Command
    @NotifyChange("mascotas")
    public void asignarMascota()
    {
        final HashMap<String, Object> map = new HashMap<>();
        map.put("selectedCliente", selectedEvent );
        Executions.createComponents("/clientes/cliente-nueva-mascota.zul", null, map);
    }

    /* Se manda desde cliente-nueva-mascota.zul */
    @GlobalCommand
    @NotifyChange("mascotas")
    public void refreshMascotas(@BindingParam("returnMascota") Mascota item)
    {
        this.newMasc = item;
        this.newMasc.setCliente(selectedEvent);
       
        if(!mascoDao.insert(this.newMasc)){
            Messagebox.show("Mascota no añadida", "Error", Messagebox.OK, Messagebox.ERROR);
        }
        else{
            selectedEvent.asignarMascota(newMasc);   //Relacion Cliente-Mascota
        }
        this.newMasc = new Mascota();
    }
  
    @GlobalCommand
    @NotifyChange({"provincias","allPoblaciones"})
    public void refreshProvincias(){}
    
    @GlobalCommand
    @NotifyChange("allPoblaciones")
    public void cambiarPoblacion(){
//        if(selectedProvincia != null){ 
//            if(selectedProvincia.getId() != 0)
//                onSelectProvincia();
//        }
//        else if(selectedEvent != null)
//            if(selectedEvent.getProvincia() != null)
//                onChangeProvincia();
//        else
            onCambiarProvincia();
        //BindUtils.postNotifyChange(null, null, this, "allPoblaciones");
    }
    
    public void onCambiarProvincia(){
        if(provincias == null) return;
        int indice = provincias.getSelectedIndex();
        if(indice > -1){
        Provincia prov = (Provincia)provincias.getModel().getElementAt(provincias.getSelectedIndex());
        
        allPoblaciones = new PoblacionDAO().findAll("select * from zk_poblacion where provincia = '" + prov.getId()+"'" );
        }
    
    }
    
    
    @GlobalCommand
    @NotifyChange({"citas", "selectedEvent"})
    public void updatesCitas(@BindingParam("returnCita") Cita item)
    {
        if(this.selectedCita != null) {
        selectedCita= item;
        
        if(!citaDao.update(selectedCita)){
            Messagebox.show("Mascota no añadida", "Error", Messagebox.OK, Messagebox.ERROR);
        }
        else{
               //Relacion Cliente-Mascota
        }
        }
        
    }
    
    @Command
    @NotifyChange("allRazas")
    public void onSelectEspecie()
    {
            allRazas = new RazaDAO().findAll("select * from zk_raza where especie = " + selectedEspecie.getId() );
    }
        
    @Command
    public void showMascota()
    {
        if(selectedMascota.getNombre() == null || selectedMascota.getNombre().isEmpty()){
            Messagebox.show("Seleccione una Mascota", "Error", Messagebox.OK, Messagebox.ERROR);
            return;
        }
        final HashMap<String, Object> map = new HashMap<>();
        map.put("selectedMascota", selectedMascota );
        //Window win = (Window) page.getFellow("win");
        //Borderlayout bl = (Borderlayout) Path.getComponent("/main");
        //Center center = bl.getCenter();
        //center.getChildren().clear();
        Executions.createComponents("/mascotas/mascota.zul", null, map);
    }
    
    //Seleccionador de Provincia, para filtrar las poblaciones de dicha provincia
    @Command
    @NotifyChange("allPoblaciones")
    public void onSelectProvincia()
    {
        allPoblaciones = new PoblacionDAO().findAll("select * from zk_poblacion where provincia = '" + selectedProvincia.getId() +"'");
    }
    
    
    
    @Command
    @NotifyChange("allPoblaciones")
    public void onChangeProvincia() 
    {
        allPoblaciones = new PoblacionDAO().findAll("select * from zk_poblacion where provincia = '" + selectedEvent.getProvincia().getId()+"'" );
    }
  
    
    @Command
    
    public void modificar(){
        onChangeProvincia();
        if(selectedEvent.getProvincia() != null){
            for(int i =0; i< provincias.getModel().getSize(); i++){
                if(selectedEvent.getProvincia().getProvincia().equals(provincias.getModel().getElementAt(i).toString())){
                    provincias.setSelectedIndex(i);
                    break;
                }

            }
        }
        //BindUtils.postNotifyChange(null, null, this, "allPoblaciones");
        if(poblaciones.getModel().getSize()>=0){
            for(int i =0; i< poblaciones.getModel().getSize(); i++){
                if(selectedEvent.getCiudad().getPoblacion().equals(poblaciones.getModel().getElementAt(i).toString())){
                    poblaciones.setSelectedIndex(i);
                    break;
                }

            }
        }
        //boolean mod = true;
        setComponentes(true);
        
    }
    
    @Command
    @NotifyChange({"selectedEvent","allPoblaciones"})
    public void cancelar() {
//        Messagebox.show("Modificación cancelada", "Aviso", Messagebox.CANCEL, Messagebox.ON_CANCEL);
        onChangeProvincia();
        if(selectedEvent.getProvincia() != null){
            for(int i =0; i< provincias.getModel().getSize(); i++){
                if(selectedEvent.getProvincia().getProvincia().equals(provincias.getModel().getElementAt(i).toString())){
                    provincias.setSelectedIndex(i);
                    break;
                }

            }
        }
        labelApellidos.setValue("");
        labelCUIT.setValue("");
        labelCiudad.setValue("");
        labelDireccion.setValue("");
        labelProvincia.setValue("");
        
        labelNombre.setValue("");
        this.selectedEvent = this.auxCliente;
        setComponentes(false);
    }
    
    @Command
    @NotifyChange({"events", "selectedEvent","clientes"})
    public void actualizar(){
        
        
        labelApellidos.setValue("");
        labelCUIT.setValue("");
        labelCiudad.setValue("");
        labelDireccion.setValue("");
        labelProvincia.setValue("");
     
        labelNombre.setValue("");
        
        this.eventDao.update(this.selectedEvent);
        //Messagebox.show("Datos del cliente no actualizados", "Error", Messagebox.OK, Messagebox.ERROR);
        setComponentes(false);
        
            
    }
    
    private void setComponentes(boolean mod){
        this.txtEmail.setReadonly(!mod);
        this.intTelefono.setReadonly(!mod);
        this.intTelefono2.setReadonly(!mod);
        
        this.modificarDatos.setVisible(!mod);
        this.cancelarDatos.setVisible(mod);
        this.updateDatos.setDisabled(!mod);
        
     
       
                                    
        this.direccion.setVisible(!mod);
        this.direccionMod.setVisible(mod);
    }
    
    
    public Validator getDateValidator() {
        
        return new AbstractValidator(){
            @Override
            public void validate(ValidationContext ctx) {
               
                
                String nif = String.valueOf(ctx.getProperties("nif")[0].getValue());
                String nombre = (String)ctx.getProperties("nombre")[0].getValue();
		String email = (String)ctx.getProperties("email")[0].getValue();
		String apellidos = (String)ctx.getProperties("apellidos")[0].getValue();
                String direccion = (String)ctx.getProperties("direccion")[0].getValue();
                Object provincia = ctx.getProperties("provincia")[0].getValue();
                Object poblacion = ctx.getProperties("ciudad")[0].getValue();
//                Integer telefono = (Integer)ctx.getProperties("telefono")[0].getValue();
		
                if(nif == null || nif.isEmpty() || nif.equals("__-________-_")) {
                    this.addInvalidMessage(ctx, "nif", "Debes introducir un CUIT");
                }else{
                    if(txtCUIT.getValue() == null) 
                    {labelCUIT.setValue("Ingrese un CUIT"); this.addInvalidMessage(ctx, "nif", "Debes introducir un CUIT");}
                    else labelCUIT.setValue("");
                }
                
                if(nombre == null || nombre.isEmpty()) {
                    this.addInvalidMessage(ctx, "nombre", "Debes introducir un nombre");
                }
                else{
                    if(txtNombre.getValue().isEmpty() ) {labelNombre.setValue("Ingrese un nombre"); this.addInvalidMessage(ctx, "nombre", "Debes introducir un nombre");}
            
                    else labelNombre.setValue("");
                }
                if(apellidos == null || apellidos.isEmpty()) {
                    this.addInvalidMessage(ctx, "apellidos", "Debes introducir los apellidos");
                }
                else{
                    if(txtApellidos.getValue().isEmpty()) {labelApellidos.setValue("Ingrese un Apellido");this.addInvalidMessage(ctx, "apellidos", "Debes introducir los apellidos");}
                    else labelApellidos.setValue("");
                }
                
                if(direccion == null || direccion.isEmpty()) {
                    this.addInvalidMessage(ctx, "direccion", "Debes introducir la dirección");
                }
                else{
                    if(txtDireccion.getValue().isEmpty()) {labelDireccion.setValue("Ingrese una Dirección");this.addInvalidMessage(ctx, "direccion", "Debes introducir la dirección");}
                    else labelDireccion.setValue("");
                }
                
                if(!(email == null || email.isEmpty()) && !email.matches(".+@.+\\.[a-z]+")) {
                    this.addInvalidMessage(ctx, "email", "Email invalido ");
                }
                
                if(provincia == null) {
                    this.addInvalidMessage(ctx, "provincia", "Seleccione una provincia");
                }else{
                    if(provincias.getSelectedIndex() <0 ) {labelProvincia.setValue("Seleccione una provincia");this.addInvalidMessage(ctx, "provincia", "Seleccione una provincia");}
                    else labelProvincia.setValue("");
                }
                
                if(poblacion == null) {
                    this.addInvalidMessage(ctx, "poblacion", "Seleccione una ciudad");
                }
                else{
                    

                    int num = poblaciones.getSelectedIndex();
                    if(num < 0 ){
                        labelCiudad.setValue("Seleccione una ciudad");
                        this.addInvalidMessage(ctx, "poblacion", "Seleccione una ciudad");

                    }else labelCiudad.setValue("");
                }
            }
        };
    }
    
    
    /*
     * Citas
     */
    
    @Command
    public void asignarCita()
    {
        final HashMap<String, Object> map = new HashMap<>();
        map.put("selectedCliente", selectedEvent );
        Executions.createComponents("/clientes/cliente-nueva-cita.zul", null, map);
    }
    
    /* Se manda desde cliente-nueva-cita.zul */
    @GlobalCommand
    @NotifyChange("citas")
    public void refreshCitas(@BindingParam("returnCita") Cita item)
    {
        
        /*
            this.newCita = item;
            this.newCita.setCliente(selectedEvent);
            this.newCita.setEmpleado(UserCredentialManager.getIntance(s).getUser() );
            if(!citaDao.insert(this.newCita)){
                Messagebox.show("Cita no añadida", "Error", Messagebox.OK, Messagebox.ERROR);
            }
            this.newCita = new Cita();
        */
        
    }
    
    /*
     * Ventas
     */
    
    @Command("asignarVenta")
    @NotifyChange("ventas")
    public void asignarVenta() {
        
            this.newVenta.setVendedor(UserCredentialManager.getIntance(s).getUser() );
            this.newVenta.setFecha(new Date());
            int id = 1 ; //ventaDao.insert(this.newVenta);
            Venta venta = this.newVenta;
            this.newVenta = new Venta();
            if(id != 0) {
                venta.setId(id);
                new VentaViewModel().showVentaLinea(venta);
            }else{
                Messagebox.show("Venta no creada", "Error", Messagebox.OK, Messagebox.ERROR);
            }
    }
    
    @Command
    public void showVentaLinea() {
        new VentaViewModel().showVentaLinea(this.selectedVenta);
    }
    
    
    
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
    
    @Command("modificarCita")
    @NotifyChange("citas")
    public void modificarCita()
    {
        final HashMap<String, Object> map = new HashMap<>();
        map.put("selectedCita", selectedCita );
        Executions.createComponents("/citas/cita-modificar.zul", null, map);
    }
    
    @Command("acudidoCita")
	@NotifyChange({"citas","avisoCita"})
	public void acudidoCita() {
            this.selectedCita.setEstado(1);
            citaDao.update(this.selectedCita);
	}
        
        @Command("canceladoCita")
	@NotifyChange({"citas","avisoCita"})
	public void canceladoCita() {
            this.selectedCita.setEstado(0);
            citaDao.update(this.selectedCita);
	}
        
        @Command("pendienteCita")
	@NotifyChange({"citas","avisoCita"})
	public void pendienteCita() {
            this.selectedCita.setEstado(2);
            citaDao.update(this.selectedCita);
	}
        
        @Command("avisadoCita")
        @NotifyChange({"citas","avisoCita"})
	public void avisadoCita() {
            this.selectedCita.setEstado(3);
            citaDao.update(this.selectedCita);
	}
        
        
        
    
    
}
