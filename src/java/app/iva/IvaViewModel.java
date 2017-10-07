package app.iva;

import com.clinica.veterinaria.clientes.Cliente;
import com.clinica.veterinaria.proveedores.Proveedor;
import com.clinica.veterinaria.util.Conversion;
import com.clinica.veterinaria.util.Fecha;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.zkoss.bind.BindUtils;
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
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zul.Doublebox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

/**
 *
 * 
 */
public class IvaViewModel {
    public final static int VENTA = 0, COMPRA = 1;
    private boolean venta;
    private String filterCodigo = "", filterTipo = "", filterFecha = "", filterCUIT = "",filterPersona = "", filterIva21 = "", filterIva27 = "", filterIva10 = "", filterGravado= "", filterNoGravado= "", filterTotal = "";
    
    private String titulo;
    
    private int  filterYear = Calendar.getInstance().get(Calendar.YEAR) -1;
    private ListModelList<Iva> listEvent;

    private final IvaDAO ivaDAO = new IvaDAO();
    private boolean verIva = true;
    private boolean facturaZ = true;
    private boolean tipoA = true;
    private Iva newEvent;
    private Iva selectedEvent;
    private Iva event;
    
    IvaViewModel este;
    private Doublebox neto;
    private Doublebox iva21 ;
    private Doublebox iva27 ;
    private Doublebox iva10 ;
    private Doublebox retencion ;
    private Doublebox excenta ;
    private Doublebox nogravado ;
    private Doublebox total ;
            
    
    @Init
    public void initSetup(@ContextParam(ContextType.VIEW) Component view, 
                          @ExecutionArgParam("vista") IvaViewModel ivavista,
                          @ExecutionArgParam("venta") int venta // Si o si le manda un parametro 1 (VENTA) o 0 (COMPRA) 
    ) 
    {
        Selectors.wireComponents(view, this, false);
        if(ivavista != null){
            este = ivavista; // Ventana Modal
            this.venta = este.isVenta();
            
            
        }
        else {
            this.venta = venta == VENTA;
            titulo = venta == VENTA? "IVA VENTA" : "IVA COMPRA";
        }
    }
    
    @AfterCompose
    public void afterCompose(@SelectorParam("#netogravado") Doublebox neto,
            @SelectorParam("#iva21") Doublebox iva21,
            @SelectorParam("#iva27") Doublebox iva27,
            @SelectorParam("#iva10") Doublebox iva10,
            @SelectorParam("#nogravado") Doublebox nogravado,
            @SelectorParam("#excentas") Doublebox excenta,
            @SelectorParam("#retenciones") Doublebox retencion,
            @SelectorParam("#total") Doublebox total)
    {
        this.neto = neto;
        this.iva21 = iva21;
        this.iva27 = iva27;
        this.iva10 = iva10;
        this.retencion = retencion;
        this.excenta = excenta;
        this.nogravado = nogravado;
        this.total = total;
        if(este == null) return;
        if(!este.modificacion){
                vistaNuevo();
            }
            else if(este.modificacion){
                vistaModificar();
            }
    }
    
    private void vistaNuevo() {
        event = este.getNewEvent();
        event.setTipo("A");
        event = ivaDAO.getOne(event);
        event.setNro_factura(event.getNro_factura() + 1);
        neto.setValue(0.0);
        iva10.setValue(0.0);
        iva21.setValue(0.0);
        iva27.setValue(0.0);
        retencion.setValue(0.0);
        nogravado.setValue(0.0);
        excenta.setValue(0.0);
        total.setValue(0.0);
        if(este.getNewEvent() instanceof IvaCompra){
            
            titulo = "Nueva Compra";
            
            //BindUtils.postNotifyChange(null,null,this,"event");
            /*
            ** Dejar todos los campos en blanco
            ** El campo buscar Persona (Cliente/Proveedor) tendra que estar oculto o visto segun un isVenta
            */
        }
        else
            titulo = "Nueva Venta";
    }
    
    private void vistaModificar() {
        event = este.getSelectedEvent();
        verIva = !(event.getTipo().equals("B") || event.getTipo().equals("Z"));
        facturaZ = !event.getTipo().equals("Z");
        this.neto.setValue(event.getNetogravado());
        this.iva21.setValue(event.getIva21());
        this.iva27.setValue(event.getIva27());
        this.iva10.setValue(event.getIva10());
        this.nogravado.setValue(event.getNogravado());
        this.excenta.setValue(event.getOpexcentas());
        this.retencion.setValue(event.getRetencion());
        this.total.setValue(event.getTotal());
        if(este.getSelectedEvent() instanceof IvaCompra){
            titulo = "Modificar Compra";
            
            //BindUtils.postNotifyChange(null,null,this,"verIva");
            /*
            ** Dejar todos los campos sea Compra o Venta
            ** El campo buscar Persona tendra que estar oculto o visto segun un filtro
            */
        }
        else
            titulo = "Modificar Venta";
    }
    

    public ListModelList<Iva> getListEvent() {
        return listEvent;
        //return ivaDAO.findAllVenta(Calendar.getInstance().get(Calendar.YEAR)-1);
    }

    @GlobalCommand
    @Command 
    @NotifyChange("listEvent")
    public void doSearch()  {
        ListModelList<Iva> allEvents  = new ListModelList<>(venta?ivaDAO.findAllVenta(filterYear):ivaDAO.findAllCompra(filterYear));
        
        String[] listaFiltro = {filterCodigo, filterTipo, filterFecha, filterCUIT,filterPersona, filterIva21, filterIva27, filterIva10, filterGravado, filterNoGravado, filterTotal};
        listEvent = new ListModelList<>();
        if(areNullOrEmpty(listaFiltro)) {
            listEvent.addAll(allEvents);
        }
        else  {
            for (Iva iva : allEvents) {
                Object[] objetoIva = {iva.getFacturaCodigo(), iva.getTipo(), iva.getFecha(), iva.getCuit(), iva.getPersona(), iva.getIva21(), iva.getIva27(), iva.getIva10(), iva.getNetogravado(), iva.getNogravado(), iva.getTotal()};               
                if (filtrar(objetoIva, listaFiltro)) {
                    listEvent.add(iva);
                }
            } 
        }
    }
    
    @Command
    //@NotifyChange({"listEvent"})
    public void addOrUpdate(@BindingParam("cmp")  final Window x){
        event.setNetogravado(neto.getValue());
        event.setTotal(total.getValue());
        event.setIva10(iva10.getValue());
        event.setIva27(iva27.getValue());
        event.setIva21(iva21.getValue());
        event.setOpexcentas(excenta.getValue());
        event.setRetencion(retencion.getValue());
        event.setNogravado(nogravado.getValue());
        if(!este.modificacion){
            add(x);
        }
        else{
            update(x);
        }    
    }
     
    public void add(final Window x) {
        if(ivaDAO.insert(este.getNewEvent())){    
            listEvent = new ListModelList<>((este.getNewEvent() instanceof IvaCompra)?ivaDAO.findAllCompra(filterYear):ivaDAO.findAllVenta(filterYear));
        }  
            BindUtils.postGlobalCommand(null, null, "doSearch", null);
            Messagebox.show("Se ha guardado", "Mensaje", Messagebox.OK, Messagebox.QUESTION,
                new org.zkoss.zk.ui.event.EventListener(){
                    @Override
                    public void onEvent(Event e){
                        if(Messagebox.ON_OK.equals(e.getName())){
                            x.detach();
                        }
                    }
                }
            );
        este.setNewEvent(null);
        
    }
    
    public void update(final Window x) {
        ivaDAO.update(este.getSelectedEvent());
        BindUtils.postGlobalCommand(null, null, "doSearch", null);
        Messagebox.show("Se ha actualizado", "Mensaje", Messagebox.OK, Messagebox.QUESTION,
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

    public boolean modificacion = false;
    //Nueva ventana de IVA para modificar
    @Command
    @NotifyChange("listEvent")
    public void modificarIva() {
        final HashMap<String, Object> map = new HashMap<>();
        map.put("vista", this );
        newEvent = null;
        map.put("venta", venta?1:0 );
        modificacion = true;
        Executions.createComponents("/ivas/iva-formulario.zul", null, map);
    }
    
    //Nueva venta IVA para agregar
    @Command
    @NotifyChange({"listEvent", "newEvent"})
    public void nuevoIva(){//@BindingParam("venta") boolean venta) {
        final HashMap<String, Object> map = new HashMap<>();
        newEvent = this.venta? new IvaVenta() : new IvaCompra();
        map.put("vista", this );
        map.put("venta", venta?1:0 );
        selectedEvent = null;
        modificacion = false;
        Executions.createComponents("/ivas/iva-formulario.zul", null, map);
    }

    @Command
    @NotifyChange({"listEvent", "selectedEvent"})
    public void deleteIva() {
        if(this.selectedEvent != null) {
            
            Messagebox.show("¿Desea eliminar " + selectedEvent.getFacturaCodigo() + "?", "Advertencia", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION,
              new org.zkoss.zk.ui.event.EventListener(){
                 @Override
                 public void onEvent(Event e){
                    if(Messagebox.ON_OK.equals(e.getName())){
                        ivaDAO.delete(selectedEvent); 
                        selectedEvent = null;
                        BindUtils.postGlobalCommand(null, null, "doSearch", null);
                    }
                 }
              }
            );
            
            
        }else
            Messagebox.show("Seleccione una factura", "Información",Messagebox.OK, null,null);
    }
    
    
    @Command
    public void buscarEntidad() //Cliente & Proveedor
    {  
        if(isVenta())
        {
            Executions.createComponents("/citas/cita-buscarcliente.zul", null, null);
        }
        else
        {
            Executions.createComponents("/pedidos/pedido-buscarproveedor.zul", null, null);
        }
    }
    
    private String personaString ="";
    public String getPersonaString(){
        return personaString;
    }
    @NotifyChange
    public void setPersonaString(String x){
        this.personaString = x;
    }
    
    @GlobalCommand
    @NotifyChange("personaString")
    public void refreshProveedor(@BindingParam("returnProveedor") Proveedor prov)
    {
        if(event == null) return;
       
        ((IvaCompra)this.event).setProveedor(prov);
        personaString = event.getPersona();
    }
    
    @GlobalCommand
    @NotifyChange("personaString")
    public void refreshCliente(@BindingParam("returnCliente") Cliente cli) 
    {
        if(event == null ) return;
        ((IvaVenta)this.event).setCliente(cli);
        personaString = event.getPersona();
    }
    
    @Command
    @NotifyChange({"verIva","facturaZ"}) // ,"neto", "iva10", "iva21", "iva27", "retencion", "nogravado", "excenta", "total"
    public void cambiarTipo(@BindingParam("valor") String tipo){
        if(event == null) return;
        verIva = !(tipo.equals("B") || tipo.equals("Z"));
        facturaZ = !tipo.equals("Z");
        event.setTotal(0.0); 
        event.setIva10(0.0); 
        event.setIva27(0.0); 
        event.setIva21(0.0); 
        event.setOpexcentas(0.0); 
        event.setRetencion(0.0);
        event.setNogravado(0.0); 
        neto.setValue(0.0);
        iva10.setValue(0.0);
        iva21.setValue(0.0);
        iva27.setValue(0.0);
        retencion.setValue(0.0);
        nogravado.setValue(0.0);
        excenta.setValue(0.0);
        total.setValue(0.0);
    }
    
    @Command
    @NotifyChange("event")
    public void botonFecha(@BindingParam("valor") Date fecha){
        event.setFechastring(Fecha.toDateFormat(fecha, "dd-MM-yyyy"));
    }
    
    @Command
    //@NotifyChange({"neto", "iva10", "iva21", "iva27", "retencion", "nogravado", "excenta", "total"})
    public void calcularIva(){
        if(neto.getValue() == null) return;
        iva21.setValue(neto.getValue() * 0.21);
        iva10.setValue(0.0);
        iva27.setValue(0.0);
        total.setValue(neto.getValue() + iva10.getValue() + iva21.getValue() + iva27.getValue() + retencion.getValue() + nogravado.getValue() + excenta.getValue());
    }
    /*
        Cambio 11-07-16
        La factura B tambien puede tener 10,5 de iva
    */
    @Command
    public void calcularPorTotal(){
        double factor = (tipoA)?0.21:0.105;
        if(total.getValue()  == null) return;
        
        
        neto.setValue(total.getValue() / (1 + factor)); 
        iva21.setValue(neto.getValue() * ((tipoA)? factor :0.0));
        iva10.setValue(neto.getValue() * ((!tipoA)? factor:0.0));
        iva27.setValue(0.0);
        event.setNetogravado(neto.getValue());
        event.setIva21(iva21.getValue());
        //total.setValue(neto.getValue() + iva10.getValue() + iva21.getValue() + iva27.getValue() + retencion.getValue() + nogravado.getValue() + excenta.getValue());
    }
    
    @Command
    //@NotifyChange({"neto", "iva10", "iva21", "iva27", "retencion", "nogravado", "excenta", "total"})
    public void calcularIva10(){
        if(neto.getValue()  == null) return;
        iva10.setValue(neto.getValue() * 0.105);
        iva21.setValue(0.0);
        iva27.setValue(0.0);
        total.setValue(neto.getValue() + iva10.getValue() + iva21.getValue() + iva27.getValue() + retencion.getValue() + nogravado.getValue() + excenta.getValue());
    }
    
    @Command
    //@NotifyChange({"neto", "iva10", "iva21", "iva27", "retencion", "nogravado", "excenta", "total"})
    public void calcularIva27(){
        if(neto.getValue()  == null) return;
        iva27.setValue(neto.getValue() * 0.27);
        iva21.setValue(0.0);
        iva10.setValue(0.0);
        total.setValue(neto.getValue() + iva10.getValue() + iva21.getValue() + iva27.getValue() + retencion.getValue() + nogravado.getValue() + excenta.getValue());
    }
    
    @Command
    //@NotifyChange({"neto", "iva10", "iva21", "iva27", "retencion", "nogravado", "excenta", "total"})
    public void sumarTodo(){
        if(neto.getValue()  == null) neto.setValue(0.0);
        if(iva21.getValue()  == null) iva21.setValue(0.0);
        if(iva27.getValue()  == null) iva27.setValue(0.0);
        if(iva10.getValue()  == null) iva10.setValue(0.0);
        if(retencion.getValue()  == null) retencion.setValue(0.0);
        if(nogravado.getValue()  == null) nogravado.setValue(0.0);
        if(excenta.getValue()  == null) excenta.setValue(0.0);
        total.setValue(neto.getValue() + iva10.getValue() + iva21.getValue() + iva27.getValue() + retencion.getValue() + nogravado.getValue() + excenta.getValue());
    }
    
    
    public Validator getIvaValidator(){
        return new AbstractValidator(){
            @Override
            public void validate(ValidationContext ctx) {
                Map<String,Property> formData = ctx.getProperties(ctx.getProperty().getValue());
                String fecha = (String)formData.get("fechastring").getValue();
                Integer puesto = Integer.parseInt(String.valueOf(formData.get("nro_puesto").getValue()));
                Integer factura = Integer.parseInt(String.valueOf(formData.get("nro_factura").getValue()));
                String tipo = (String)formData.get("tipo").getValue();
                //Double total = Double.parseDouble(String.valueOf(formData.get("total").getValue()));
                //Double iva21 = Double.parseDouble(String.valueOf(formData.get("iva21").getValue()));
                //Double iva10 = Double.parseDouble(String.valueOf(formData.get("iva10").getValue()));
                //Double iva27 = Double.parseDouble(String.valueOf(formData.get("iva27").getValue()));
                //Date beginDate = (Date)formData.get("beginDate").getValue();
                //Date endDate = (Date)formData.get("endDate").getValue();
                //String fecha = (String)ctx.getProperties("fechastring")[0].getValue();
                if(isNullOrEmpty(fecha)){
                    addInvalidMessage(ctx, "fechaString","Ingrese una fecha");
                }
                else if(!Conversion.esDate(fecha, "dd-MM-yyyy")){
                    addInvalidMessage(ctx, "fechaString","Ingrese una fecha valida");
                }
                /*else if(Integer.parseInt(fecha.substring(0, 2)) > 31 || Integer.parseInt(fecha.substring(3, 5)) > 12){
                    addInvalidMessage(ctx, "fechaString","Ingrese una fecha valida");
                }*/
                if(isNullOrEmpty(tipo)) {
                    addInvalidMessage(ctx, "tipo","Seleccione un tipo");
                }
                
                
                if((puesto < 1 && !tipo.equals("Z")) 
                        || factura < 1){
                    addInvalidMessage(ctx, "factura","Ingrese un número mayor a cero");
                }
                
                if(!isNullOrEmpty(tipo) && event.getId() < 0) {
                    if(new IvaDAO().existeCodigo(event,tipo,puesto,factura)){
                        addInvalidMessage(ctx, "factura","El número ya existe.");
                    }//event.getTipo(), event.getNro_puesto(), event.getNro_factura()
                }
                if( isNullOrEmpty(event.getPersona())){ //isNullOrEmpty(personaString) &&
                    addInvalidMessage(ctx, "persona","Seleccione un "+ (isVenta()? "cliente" : "proveedor"));
                }else if(!isNullOrEmpty(tipo)){
                    
                    if(event.getPersona().equalsIgnoreCase("consumidor final") 
                            && tipo.equals("A")){
                        addInvalidMessage(ctx, "persona","Factura A no permite Consumidor Final");
                    }
                }
                
                System.out.println(total + " e "+event.getTotal() );
                if(total.getValue() == 0.0){
                    addInvalidMessage(ctx, "total","El total debe ser mayor a cero");
                }
                
                      
            }
        };
    }
    
    private boolean isNullOrEmpty(Object o){
        if(o == null) return true;
        else return (String.valueOf(o).isEmpty());
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


    public Iva getNewEvent() {
        return newEvent;
    }

    public void setNewEvent(Iva newEvent) {
        this.newEvent = newEvent;
    }

    public Iva getSelectedEvent() {
        return selectedEvent;
    }

    public void setSelectedEvent(Iva selectedEvent) {
        this.selectedEvent = selectedEvent;
    }

    public String getFilterCUIT() {
        return filterCUIT;
    }
    @NotifyChange
    public void setFilterCUIT(String filterCUIT) {
        this.filterCUIT = filterCUIT;
    }
    
    public String getFilterCodigo() {
        return filterCodigo;
    }
    
    @NotifyChange
    public void setFilterCodigo(String filterCodigo) {
        this.filterCodigo = filterCodigo;
    }

    public String getFilterTipo() {
        return filterTipo;
    }
    @NotifyChange
    public void setFilterTipo(String filterTipo) {
        this.filterTipo = filterTipo;
    }

    public String getFilterFecha() {
        return filterFecha;
    }
    @NotifyChange
    public void setFilterFecha(String filterFecha) {
        this.filterFecha = filterFecha;
    }

    public String getFilterPersona() {
        return filterPersona;
    }
    @NotifyChange
    public void setFilterPersona(String filterPersona) {
        this.filterPersona = filterPersona;
    }

    public String getFilterIva21() {
        return filterIva21;
    }
    @NotifyChange
    public void setFilterIva21(String filterIva21) {
        this.filterIva21 = filterIva21;
    }

    public String getFilterIva27() {
        return filterIva27;
    }
    @NotifyChange
    public void setFilterIva27(String filterIva27) {
        this.filterIva27 = filterIva27;
    }

    public String getFilterIva10() {
        return filterIva10;
    }
    @NotifyChange
    public void setFilterIva10(String filterIva10) {
        this.filterIva10 = filterIva10;
    }

    public String getFilterGravado() {
        return filterGravado;
    }
    
    @NotifyChange
    public void setFilterGravado(String filterGravado) {
        this.filterGravado = filterGravado;
    }

    public String getFilterNoGravado() {
        return filterNoGravado;
    }

    @NotifyChange
    public void setFilterNoGravado(String filterNoGravado) {
        this.filterNoGravado = filterNoGravado;
    }

    public String getFilterTotal() {
        return filterTotal;
    }

    @NotifyChange
    public void setFilterTotal(String filterTotal) {
        this.filterTotal = filterTotal;
    }
    
    public int getFilterYear(){
        return filterYear;
    }
    
    @NotifyChange
    public void setFilterYear(int filterYear) {
        this.filterYear = filterYear;
    }

    public Iva getEvent() {
        return event;
    }

    
    public void setEvent(Iva event) {
        this.event = event;
    }

    public String getTitulo() {
        return titulo;
    }
    
    @NotifyChange
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public boolean isVenta() {
        return venta;
    }

    @NotifyChange
    public void setVenta(boolean venta) {
        this.venta = venta;
    }

    public boolean isVerIva() {
//        if(event == null) return true;
//        return !(event.getTipo().equals("B") || event.getTipo().equals("Z"));
        return verIva;
    }

    
    
    @NotifyChange
    public void setVerIva(boolean verIva){
        this.verIva = verIva;
    }



    public boolean isFacturaZ() {
        return facturaZ;
    }

    public void setFacturaZ(boolean facturaZ) {
        this.facturaZ = facturaZ;
    }

    boolean bandera = true;
    public boolean isTipoA() {
        if(bandera){
            bandera = false;
            return (este.modificacion)? event.getIva10() == 0.0: tipoA;
        }
        else{
            
             return tipoA;
        }
           
    }

   
    
    @NotifyChange
    public void setTipoA(boolean tipoA) {
        this.tipoA = tipoA;
    }

    
    
    
}
