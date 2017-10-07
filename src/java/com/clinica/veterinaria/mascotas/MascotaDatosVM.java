package com.clinica.veterinaria.mascotas;

import com.clinica.veterinaria.citas.Cita;
import com.clinica.veterinaria.citas.CitaDAO;
import com.clinica.veterinaria.clientes.Cliente;
import com.clinica.veterinaria.especies.Especie;
import com.clinica.veterinaria.especies.EspecieDAO;
import com.clinica.veterinaria.ficheros.Fichero;
import com.clinica.veterinaria.ficheros.FicheroDAO;
import com.clinica.veterinaria.historial.Historial;
import com.clinica.veterinaria.historial.HistorialDAO;
import com.clinica.veterinaria.mascota_vacuna.MascotaVacuna;
import com.clinica.veterinaria.mascota_vacuna.MascotaVacunaDAO;
import com.clinica.veterinaria.poblaciones.Poblacion;
import com.clinica.veterinaria.poblaciones.PoblacionDAO;
import com.clinica.veterinaria.provincias.Provincia;
import com.clinica.veterinaria.provincias.ProvinciaDAO;
import com.clinica.veterinaria.razas.Raza;
import com.clinica.veterinaria.razas.RazaDAO;
import com.clinica.veterinaria.servicio_familia.ServicioFamilia;
import com.clinica.veterinaria.user.UserCredentialManager;
import com.clinica.veterinaria.util.UploadFile;
import com.clinica.veterinaria.vacunas.Vacuna;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.zkoss.bind.Property;
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
import org.zkoss.io.Files;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zkmax.zul.Dropupload;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Image;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Menuitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Radio;
import org.zkoss.zul.Selectbox;
import org.zkoss.zul.Textbox;

/**
 * 
 */
public class MascotaDatosVM {
            
    String activo;
    Session s = Sessions.getCurrent();
    
    //private List<Cita>    citas    = new ArrayList<>();
    //private List<Mascota> mascotas = new ArrayList<>();

    //private List<Historial> historiales = new ArrayList<>();
    //private List<MascotaVacuna> vacunas = new ArrayList<>();
    private List<Raza>    allRazas = new ArrayList<>();
    private List<Poblacion> allPoblaciones = new ArrayList<>();
    
    private final MascotaDAO eventDao = new MascotaDAO();
    private final MascotaDAO mascoDao = new MascotaDAO();
    private final EspecieDAO especieDao = new EspecieDAO();
    //private final RazaDAO razaDao = new RazaDAO();
    private final CitaDAO citaDao = new CitaDAO();
    private final MascotaVacunaDAO mascvacDao = new MascotaVacunaDAO();

    private final HistorialDAO histDao = new HistorialDAO();
    private final ProvinciaDAO provDao = new ProvinciaDAO();
    private final PoblacionDAO poblDao = new PoblacionDAO();

    private Mascota newEvent = new Mascota();
    private Mascota newMasc  = new Mascota();
    private Cita newCita = new Cita();
    private Historial newHistorial = new Historial();
    private MascotaVacuna newVacuna = new MascotaVacuna();
    
    private Mascota selectedEvent;
    private Especie auxEspecie = new Especie();
    private Raza auxRaza = new Raza();
    private Mascota selectedMascota = new Mascota();
    private Cita selectedCita;
    private Especie selectedEspecie = new Especie();

    private Provincia selectedProvincia = new Provincia();
    private Historial selectedHistorial;
    private MascotaVacuna selectedVacuna;
    
    private Mascota auxMascota = new Mascota();
    private Menuitem updateDatos;
    private Menuitem modificarDatos;
    private Menuitem cancelarDatos;
   // private Textbox txtObserv;
    private Textbox txtChip;
    private Textbox txtNombre;
    private Listbox lsbSexo;
    private Datebox dateNac;
    private Datebox dateDef;
    private Radio ra1;
    private Radio ra2;
    private Grid Caract;
    private Grid Caractmodif;
    private Grid Contacto;
    private Grid ContactoModif;
    private Selectbox cmbRaza;
    private Selectbox cmbEspecie;
    
    private Image imagen;
    
    private String nombre;
    /*
     * Inicio de la carga 
     */
    @Init
    public void initSetup(@ContextParam(ContextType.VIEW) Component view, 
                          @ExecutionArgParam("selectedMascota") Mascota selectedMascota) 
    {
        Selectors.wireComponents(view, this, false);
        this.selectedEvent = selectedMascota;
        auxEspecie = selectedMascota.getEspecie();
        auxRaza = selectedMascota.getRaza();
        auxMascota = selectedMascota;
        
      
//        System.out.println("Cliente: " + selectedMascota.getCliente().getFullname() );
//        citas = getCitas();
//        ventas = getVentas();
//        historiales = getHistoriales();
    }
    
    
    @AfterCompose
    public void afterCompose(@SelectorParam("#updateDatos") Menuitem updateDatos,
                             @SelectorParam("#modificarDatos") Menuitem modificarDatos,
                             @SelectorParam("#cancelarDatos") Menuitem cancelarDatos,
                         //    @SelectorParam("#txtObserv") Textbox txtObserv,
                             @SelectorParam("#txtChip") Textbox txtChip,
                             @SelectorParam("#txtNombre") Textbox txtNombre,
                             @SelectorParam("#lsbSexo") Listbox lsbSexo,
                             @SelectorParam("#dateNac") Datebox dateNac,
                             @SelectorParam("#dateDef") Datebox dateDef,
                             @SelectorParam("#ra1") Radio ra1,
                             @SelectorParam("#ra2") Radio ra2,
                             @SelectorParam("#Caract") Grid Caract,
                             @SelectorParam("#Caractmodif") Grid Caractmodif,
                             @SelectorParam("#Contacto") Grid Contacto,
                             @SelectorParam("#ContactoModif") Grid ContactoModif,
                             @SelectorParam("#cmbRaza") Selectbox cmbRaza,
                             @SelectorParam("#cmbEspecie") Selectbox cmbEspecie,
                             @SelectorParam("#imagen") Image imagen,
                             @SelectorParam("arrastrar") Dropupload arrastrar
                             ){
        
        
       // onCreate="@command('onSelectEvent')"
        onSelectEvent();
        this.updateDatos = updateDatos;
        this.modificarDatos = modificarDatos;
        this.cancelarDatos = cancelarDatos; //menuitem       
        
    //    this.txtObserv = txtObserv;
        this.txtChip = txtChip;
        this.txtNombre = txtNombre;
        this.lsbSexo = lsbSexo; //listbox
        this.dateNac = dateNac; //datebox
        this.dateDef = dateDef;
        this.ra1 = ra1;
        this.ra2 = ra2; //radio
        
        this.Caract = Caract; //grid
        this.Caractmodif = Caractmodif; //grid
        this.Contacto = Contacto; //grid
        this.ContactoModif = ContactoModif; //grid
        this.cmbRaza = cmbRaza;
        this.cmbEspecie = cmbEspecie;
        this.imagen = imagen;
        
        
        if(this.selectedEvent != null){
            if(this.selectedEvent.getFoto() != null){
                imagen.setSrc(this.selectedEvent.getFoto());
            }
        }
    }
    
    public Mascota getSelectedEvent() {
            return selectedEvent;
    }

    public void setSelectedEvent(Mascota selectedEvent) {
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

    public Historial getSelectedHistorial() {
        return selectedHistorial;
    }

    public void setSelectedHistorial(Historial selectedHistorial) {
        this.selectedHistorial = selectedHistorial;
    }
    
//    public Venta getSelectedVenta() {
//            return selectedVenta;
//    }
//
//    public void setSelectedVenta(Venta selectedVenta) {
//            this.selectedVenta = selectedVenta;
//    }
    
  
    
    @Command
    public void onUploadFoto(@BindingParam("upload") UploadEvent event) throws FileNotFoundException, IOException {
        if(updateDatos.isDisabled()) return;
        Media media = event.getMedia();
        
        if (!(media instanceof org.zkoss.image.Image)) return;
        nombre = this.selectedEvent.getId() + media.getName();
        String path = "/uploads/mascotas/";
        String ruta = Executions.getCurrent().getDesktop().getWebApp().getRealPath("/");
        
        ruta = ruta + path ;
        File baseDir = new File(ruta);
        if (!baseDir.exists()) {
            baseDir.mkdirs();
        }
        
        ruta = ruta + nombre ;
        
        
        Files.copy(new File(ruta), media.getStreamData());
        new UploadFile().imagenMascota(ruta);
        //copiarFile(ruta, media);
        //Messagebox.show("File Sucessfully uploaded in the path [ ." + filePath + " ]");
        //this.selectedEvent.setFoto(path+nombre);
        this.imagen.setSrc("/uploads/mascotas/"+nombre);
      
    }
    
//    private void copiarFile(String ruta, Media media) throws FileNotFoundException, IOException{
//        BufferedInputStream in;
//        BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(new File(ruta)));
//        byte[] fi = media.getByteData();
//        InputStream fin = media.getStreamData();
//        byte buffer[] = fi;// 
//        in = new BufferedInputStream(fin);
//        int ch = in.read(buffer);
//        while (ch != -1) {
//            out.write(buffer, 0, ch);
//            ch = in.read(buffer);
//        }
//    }
    
  
    
    public Cita getSelectedCita() {
            return selectedCita;
    }

    public void setSelectedCita(Cita selectedCita) {
            this.selectedCita = selectedCita;
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

    public List<Mascota> getEvents() {
            return eventDao.findAll();
    }

    public List<Especie> getEspecies() {
            return especieDao.findAll();
    }

    

    public List<Raza> getAllRazas() {
        
        return allRazas;
    }

    
    public List<Cita> getCitas() {
            return citaDao.findAll("select * from zk_cita where id_mascota=" + selectedEvent.getId());
    }
    
//    public List<Venta> getVentas() {
//            return ventaDao.findAll("select * from zk_venta where id_cliente=" + selectedEvent.getId());
//    }

    public List<Historial> getHistoriales() {
            return histDao.findAll("select * from zk_historial where id_mascota=" + selectedEvent.getId() + " ORDER BY fecha DESC");
    }
    
    public List<MascotaVacuna> getVacunas() {
            return mascvacDao.findAll("select * from zk_mascota_vacuna where mascota=" + selectedEvent.getId() + " ORDER BY fecha DESC");
    }
    
    
    @GlobalCommand
        @NotifyChange({"selectedEvent", "especies"})
        public void cambiarEspecies(){
            
        }
        
        @GlobalCommand
        @NotifyChange({"selectedEvent", "allRazas"})
        public void cambiarRazas(){
            if(selectedEspecie != null) onSelectEspecie();
            
        }
    
    
    @Command("add")
    @NotifyChange("events")
    public void add() { //Añadir nuevo cliente
//            this.newEvent.setId(UUID.randomUUID().variant());
//            this.newEvent.setCliente(selectedEvent);  //Relacion Mascota-Cliente
            if(!eventDao.insert(this.newEvent)){
                Messagebox.show("Mascota no añadida", "Error", Messagebox.OK, Messagebox.ERROR);
            }
            this.newEvent = new Mascota();
    }

    

    @Command("delete")
    @NotifyChange({"events", "selectedEvent"})
    public void delete() {
            //shouldn't be able to delete with selectedEvent being null anyway
            //unless trying to hack the system, so just ignore the request
            if(this.selectedEvent != null) {
                if(!eventDao.delete(this.selectedEvent)){
                    Messagebox.show("Mascota no eliminada", "Error", Messagebox.OK, Messagebox.ERROR);
                }
                this.selectedEvent = null;
            }
    }

    /*
     * Mascotas
     */
    
    @Command("addmasc")
    @NotifyChange("eventsmasc")
    public void addmmasc() {
//            this.newMasc.setId(UUID.randomUUID().variant());
//            this.newMasc.setCliente(selectedEvent);  //Relacion Mascota-Cliente
//            selectedEvent.asignarMascota(newMasc);   //Relacion Cliente-Mascota
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
    @NotifyChange({"eventsmasc", "selectedMascota"})
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
    public void asignarMascota()
    {
        final HashMap<String, Object> map = new HashMap<>();
        map.put("selectedCliente", selectedEvent );
        Executions.createComponents("/mascotas/mascota-asignar.zul", null, map);
    }
    
    @Command
    @NotifyChange("allRazas")
    public void onSelectEspecie()
    {
            allRazas = new RazaDAO().findAll("select * from zk_raza where especie = " + selectedEspecie.getId() );
    }
    
    @Command
    @NotifyChange({"selectedEvent","allRazas","selectedEspecie"})
    public void onSelectEvent()
    {
        
            allRazas = new RazaDAO().findAll("select * from zk_raza where especie = " + selectedEvent.getEspecie().getId() );
    }
        
            //Seleccionador de Provincia, para filtrar las poblaciones de dicha provincia
    @Command
    @NotifyChange("allPoblaciones")
    public void onSelectProvincia()
    {
            
            allPoblaciones = new PoblacionDAO().findAll("select * from zk_poblacion where provincia = " + selectedProvincia.getId() );
    }
    
    
    @Command  
    public void modificar(){
        nombre = null;
        onSelectEvent();
        if(selectedEvent.getEspecie() != null){
            for(int i =0; i< cmbEspecie.getModel().getSize(); i++){
                if(selectedEvent.getEspecie().getEspecie().equals(cmbEspecie.getModel().getElementAt(i).toString())){
                    cmbEspecie.setSelectedIndex(i);
                    break;
                }

            }


            if(cmbRaza.getModel().getSize()>=0){
            for(int i =0; i< cmbRaza.getModel().getSize(); i++){
                if(selectedEvent.getRaza().getRaza().equals(cmbRaza.getModel().getElementAt(i).toString())){
                    cmbRaza.setSelectedIndex(i);
                    break;
                }
            }
            }
        }
        
        setComponentes(true);
        
    }
    
    @Command
    @NotifyChange({"selectedEvent","allRazas","activo"})
    public void cancelar() {
        onSelectEvent();
        setComponentes(false);
        nombre = null;
        this.selectedEvent = this.auxMascota;
        this.imagen.setSrc(selectedEvent.getFoto());
      
    }
    
    
    @Command
    @NotifyChange({"events", "selectedEvent"})
    public void update() {
        setComponentes(false);
        if(this.nombre != null){
            this.selectedEvent.setFoto(nombre); //imagen.getSrc());
        }
        if((this.selectedEvent.getEspecie() == null) ) {
            this.selectedEvent.setEspecie(this.auxEspecie);
        }

        if((this.selectedEvent.getRaza() == null)) {
            this.selectedEvent.setRaza(auxRaza);
        }

        if(!eventDao.update(this.selectedEvent)){
            Messagebox.show("Mascota no actualizada", "Error", Messagebox.OK, Messagebox.ERROR);
            this.selectedEvent = this.auxMascota;
        }
    }
    
    
    public void setComponentes(boolean mod){
        updateDatos.setDisabled(!mod);
        modificarDatos.setVisible(!mod); //menuitem
        cancelarDatos.setVisible(mod); //menuitem        
        
    
        txtChip.setReadonly(!mod);
        txtNombre.setReadonly(!mod);
  //      txtObserv.setReadonly(!mod);
        lsbSexo.setDisabled(!mod); //listbox
        dateNac.setDisabled(!mod); //datebox
        dateDef.setDisabled(!mod);
        ra1.setDisabled(!mod);
        ra2.setDisabled(!mod); //radio
        
        
        
        Caract.setVisible(!mod); //grid
        Caractmodif.setVisible(mod); //grid
        Contacto.setVisible(!mod); //grid
        ContactoModif.setVisible(mod); //grid
    }
    
    
    public Validator getValidarMascota() {
        
        return new AbstractValidator(){
            @Override
            public void validate(ValidationContext ctx) {
                Map<String,Property> beanProps = ctx.getProperties(ctx.getProperty().getBase());
                String chip = (String)beanProps.get("chip").getValue();
                String nombre = (String)beanProps.get("nombre").getValue();
                Date nacimiento = (Date)(beanProps.get("nacimiento").getValue());
                String sexo = (String)beanProps.get("sexo").getValue();
                Object especie = beanProps.get("especie").getValue();
                Object raza = beanProps.get("raza").getValue();
                
                if(chip == null || chip.isEmpty()){
                   // addInvalidMessage(ctx, "chip", "Ingrese un Código de Mascota");
                }else if (new MascotaDAO().existChip(chip) && !chip.equals(txtChip.getValue())){
                   addInvalidMessage(ctx, "chip", "El código ya existe");
                }
                
                if(nacimiento == null ){
                    addInvalidMessage(ctx, "nacimiento", "Ingrese un Código de Mascota");
                }else if(dateNac.getValue() == null){
                    addInvalidMessage(ctx, "nacimiento", "Ingrese un Código de Mascota");
                }
                
                
                 if(nombre == null || nombre.isEmpty()){
                    addInvalidMessage(ctx, "nombre", "Ingrese un Nombre");
                }else if(txtNombre.getValue().isEmpty()){
                    addInvalidMessage(ctx, "nombre", "Ingrese un Nombre");
                }
                
                 if(sexo == null || sexo.isEmpty()){
                    addInvalidMessage(ctx, "sexo", "Ingrese un Nombre");
                }
                 
               
                    if(cmbEspecie.getSelectedIndex() <0){
                    addInvalidMessage(ctx, "especie", "Seleccione una especie");
                }
                 
                 
                    if(cmbRaza.getSelectedIndex() < 0){
                    addInvalidMessage(ctx, "raza", "Seleccione una raza");
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
    
    /*
     * Historiales
     */
    
    @Command
    @NotifyChange("events")
    public void addhistorial() {
        this.newHistorial.setMascota(this.getSelectedEvent());
        this.newHistorial.setId_veterinario(UserCredentialManager.getIntance(s).getUser());
        if(histDao.insert(this.newHistorial)<0){
            Messagebox.show("Historial no añadido", "Error", Messagebox.OK, Messagebox.ERROR);
        }
        this.newHistorial = new Historial();
    }

    @Command
    @NotifyChange({"events","historiales"})
    public void updatehistorial() {
        if(!histDao.update(this.selectedHistorial)){
            Messagebox.show("Historial no actualizado", "Error", Messagebox.OK, Messagebox.ERROR);
        }
    }

    @Command
    @NotifyChange({"historiales", "events", "selectedEvent"})
    public void deletehistorial() {
        //shouldn't be able to delete with selectedEvent being null anyway
        //unless trying to hack the system, so just ignore the request
        if(this.selectedHistorial != null) {
            if(!histDao.delete(this.selectedHistorial)){
                Messagebox.show("Historial no eliminado", "Error", Messagebox.OK, Messagebox.ERROR);
            }
            this.selectedHistorial = null;
        }
    }
    
    @GlobalCommand
    @NotifyChange("historiales")
    public void refreshHistorial(@BindingParam("returnHistorial") Historial hist)
    {
        hist.setFecha(new Date());
        int id = histDao.insert(hist);
        
        if(!hist.getFicheros().isEmpty() && id > 0){
            Iterator <Fichero> it = hist.getFicheros().iterator();
            while(it.hasNext()){
                Fichero file = it.next();
                file.setId_externo(id);
                if(new FicheroDAO().insert(file) > 0){
                    System.out.println("Nuevo>>Fichero insertado!!");
                }
                
            }
        }
            
        this.newHistorial = new Historial();
    }
    
    @GlobalCommand
    @NotifyChange("historiales")
    public void refreshHistorialModificado(@BindingParam("returnHistorial") Historial hist) {
        if(!histDao.update(hist)){
            Messagebox.show("Historial no actualizado", "Error", Messagebox.OK, Messagebox.ERROR);
        }
    }
    
    @Command
    public void abrirHistorial(){
        Map map = new HashMap();
        map.put("selectedMascota", this.selectedEvent);
        map.put("selectedHistorial", this.selectedHistorial);
        Executions.createComponents("/mascotas/mascota-historial-modificar.zul", null, map);
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
        
    @Command("deletevacuna")
    @NotifyChange("vacunas")
    public void deletevacuna() {
        //shouldn't be able to delete with selectedEvent being null anyway
        //unless trying to hack the system, so just ignore the request
        if(this.selectedVacuna != null) {
            if(!mascvacDao.delete(this.selectedVacuna)){
                Messagebox.show("Vacuna no eliminada", "Error", Messagebox.OK, Messagebox.ERROR);
            }
            this.selectedVacuna = null;
        }
    }
    
    public MascotaVacuna getSelectedVacuna() {
        return selectedVacuna;
    }

    public void setSelectedVacuna(MascotaVacuna selectedVacuna) {
        this.selectedVacuna = selectedVacuna;
    }
    
    
    /* Se manda desde mascota-vacuna-nueva.zul */
    @GlobalCommand
    @NotifyChange({"vacunas", "citas"})
    public void refreshVacunas(@BindingParam("returnVacuna") Set<Vacuna> items)
    {
//        System.out.println("Tamaño: " + items.size());
        Iterator <Vacuna> it = items.iterator();
        while(it.hasNext()){
            Vacuna item = it.next();
//            System.out.println("Vacuna: " + item.getNombre());
            this.newVacuna.setMascota(selectedEvent);
            this.newVacuna.setVacuna(item);
            this.newVacuna.setVeterinario(UserCredentialManager.getIntance(s).getUser());
            if(!mascvacDao.insert(this.newVacuna)){
                Messagebox.show("Vacuna no añadida", "Error", Messagebox.OK, Messagebox.ERROR);
            }
            
            if(item.getDias() > 0 ){
                this.newCita.setMascota(selectedEvent);
                this.newCita.setCliente(selectedEvent.getCliente());
                this.newCita.setEmpleado(UserCredentialManager.getIntance(s).getUser());
                this.newCita.setEstado(2); //Pendiente
                this.newCita.setInforme(item.getNombre());
                
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.DAY_OF_YEAR, item.getDias());

                this.newCita.setFecha(cal.getTime());
                this.newCita.setHora("09:00:00");
                ServicioFamilia serv = new ServicioFamilia();
                serv.setId(1);
                this.newCita.setServicio(serv);
                
                if(!citaDao.insert(newCita)){
                    Messagebox.show("Cita no añadida", "Error", Messagebox.OK, Messagebox.ERROR);
                }
                this.newCita = new Cita();
            }
            
            this.newVacuna = new MascotaVacuna();
        }
    }
    
    /* Se manda desde mascota-vacuna-nueva.zul */
    @GlobalCommand
    @NotifyChange({"vacunas", "citas"})
    public void refreshCitas(@BindingParam("returnCita") Cita item)
    {
        /*
            this.newCita = item;
            this.newCita.setCliente(selectedEvent.getCliente());
            this.newCita.setEmpleado(UserCredentialManager.getIntance(s).getUser() );
            if(!citaDao.insert(this.newCita)){
                Messagebox.show("Cita no añadida", "Error", Messagebox.OK, Messagebox.ERROR);
            }
            
                */
                this.newCita = new Cita();
            
                
    }

    @GlobalCommand
    @NotifyChange("selectedEvent")
    public void refreshModificarPropietario(@BindingParam("returnCliente") Cliente cli) {
        this.selectedEvent.setCliente(cli);
    }
    
    @NotifyChange
    public void setActivo(String activo) {
        this.activo = activo;
        if("false".equals(activo)) {
            this.selectedEvent.setFechabaja(new Date());
        }
        else {
            this.selectedEvent.setFechabaja(null);
        }
    }

    @NotifyChange
    public String getActivo() {
//        System.out.println("isActivo1: " + activo);
        if(this.selectedEvent.getFechabaja() == null) {
            activo = "true";
        }
        else {//if(this.selectedEvent.getFechabaja() == null){
            activo = "false";
        }
        
        return activo;
    }
    
    
    
    @Command("mostrarPeso")
    public void mostrarPeso()
    {
        final HashMap<String, Object> map = new HashMap<>();
        map.put("selectedMascota", selectedEvent );
        Executions.createComponents("/mascotas/mascota-peso.zul", null, map);
    }
}
