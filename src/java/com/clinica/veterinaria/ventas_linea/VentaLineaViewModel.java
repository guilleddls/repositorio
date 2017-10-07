package com.clinica.veterinaria.ventas_linea;

import com.clinica.veterinaria.albaranes.BoletaPDF;
import com.clinica.veterinaria.clientes.Cliente;
import com.clinica.veterinaria.items.Item;
import com.clinica.veterinaria.productos.Producto;
import com.clinica.veterinaria.productos.ProductoDAO;
import com.clinica.veterinaria.servicios.Servicio;
import com.clinica.veterinaria.user.User;
import com.clinica.veterinaria.user.UserCredentialManager;
import com.clinica.veterinaria.user.UserDAO;
import com.clinica.veterinaria.ventas.Venta;
import com.clinica.veterinaria.ventas.VentaDAO;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Bandbox;
import org.zkoss.zul.Borderlayout;
import org.zkoss.zul.Center;
import org.zkoss.zul.Messagebox;

/**
 *
 * 
 */
public class VentaLineaViewModel {
    
    

    private final VentaLineaDAO lineaDao = new VentaLineaDAO();
    private final VentaDAO ventaDao = new VentaDAO();
    private final UserDAO userDao = new UserDAO();
    
    private VentaLinea newEvent = new VentaLinea();
    private VentaLinea selectedEvent;
    private Venta selectedVenta;
    private User selectedVet = new User();
    Session s = Sessions.getCurrent();
    private float costeventalineas;
    
    private boolean modificar = true;
    private boolean readyToPrint = false;
    
   /* private Textbox nombreArticulo;
    private Doublebox cantidadArticulo;
    private Doublebox pvpArticulo;
    private Doublebox stockArticulo;
    
*/
    
    @Init
    public void initSetup(@ContextParam(ContextType.VIEW) Component view, 
            @ExecutionArgParam("selectedVenta") Venta selectedVenta,
            @ExecutionArgParam("selectedCliente") Cliente cliente)
    {
        Selectors.wireComponents(view, this, false);
        if(selectedVenta == null){ //Si la venta es nula,  // Venta Nueva
            modificar = false;
            Venta newVenta = new Venta();
            newVenta.setCliente(cliente!=null? cliente : new Cliente().getConsumidorFinal()); //entonces es Consumidor Final
            newVenta.setVendedor(UserCredentialManager.getIntance(s).getUser());
            newVenta.setFecha(new Date());
            newVenta.setDeudor(true);
            //int id = ventaDao.insert(newVenta); 
            newVenta.setId(ventaDao.getNewID());
            selectedVenta = newVenta;
        }else{
            readyToPrint = true;
            selectedVenta.setVenta_lineas(lineaDao.findByVenta(selectedVenta));
        }
        
        this.selectedEvent = null;
        this.selectedVenta = selectedVenta;
        
    }
    
    /*
    @AfterCompose
    public void afterCompose(@SelectorParam("#nombreArticulo") Textbox nombreArticulo,                          
                           @SelectorParam("#cantidadArticulo") Doublebox cantidadArticulo,                         
                           @SelectorParam("#pvpArticulo") Doublebox pvpArticulo,                         
                           @SelectorParam("#stockArticulo") Doublebox stockArticulo){
        this.nombreArticulo = nombreArticulo;
        this.cantidadArticulo = cantidadArticulo;
        this.pvpArticulo = pvpArticulo;
        this.stockArticulo = stockArticulo;
        
    }*/
    
    public String getTitulo(){
        return "Venta Nº: " + String.format("%04d" , selectedVenta.getId());
    }
    
    public VentaLinea getSelectedEvent() {
        return selectedEvent;
    }

    public void setSelectedEvent(VentaLinea selectedEvent) {
        this.selectedEvent = selectedEvent;
    }

    public Venta getSelectedVenta() {
        return selectedVenta;
    }

    public void setSelectedVenta(Venta selectedVenta) {
        this.selectedVenta = selectedVenta;
    }

    public User getSelectedVet() {
        return selectedVet;
    }

    public void setSelectedVet(User selectedVet) {
        this.selectedVet = selectedVet;
    }
    
    public VentaLinea getNewEvent() {
        return newEvent;
    }

    public void setNewEvent(VentaLinea newEvent) {
        this.newEvent = newEvent;
    }

    
    // No deberia devolver todas las lineas ??
    public List<VentaLinea> getEvents() {
        return lineaDao.findAll();
    }

    public List<VentaLinea> getVentalineas() {
        return selectedVenta.getVenta_lineas();//lineaDao.findByVenta(this.selectedVenta);
    }

    public boolean isReadyToPrint() {
        return readyToPrint;
    }

    @NotifyChange
    public void setReadyToPrint(boolean readyToPrint) {
        this.readyToPrint = readyToPrint;
    }

   

    /*
    public List<User> getVeterinarios() {
        return userDao.findAll("select * from zk_usuario where tipo = 2");
    }

    public void setVeterinarios(List<User> veterinarios) {
        this.veterinarios = veterinarios;
    }*/
    
    
    public Validator getValidarCantidad(){
        return new AbstractValidator(){
            @Override
            public void validate(ValidationContext ctx) {
                Map<String,Property> formData = ctx.getProperties(ctx.getProperty().getValue());
                Object cantidad = formData.get("cantidad").getValue();
                if(selectedEvent == null) return;
                if(cantidad==null){
                    addInvalidMessage(ctx, "cantidad","Ingrese una cantidad");
                }
                else{
                    float cant = (float) cantidad;
                    if(cant <= 0){
                        addInvalidMessage(ctx, "cantidad","Ingrese una cantidad mayor a cero");
                    }
                    else if(cant > selectedEvent.getProducto().getStock()){
                        addInvalidMessage(ctx, "cantidad","La cantidad no puede ser mayor al stock");
                    }
                }
            }
        };
    }
    
    public String getValidarVenta(){
        StringBuilder rta = new StringBuilder();
        rta
        .append((selectedVenta.getCliente().getId() == 0 && selectedVenta.isDeudor())? "El consumidor final no puede ser deudor.\n":"")
        .append( selectedVenta.getVenta_lineas().isEmpty()? "Debe ingresar al menos un producto.\n":"")
        .append( selectedVenta.getFecha() == null? "Debe ingresar una fecha.\n":"");
                
         
        return rta.toString();
    }
    
    @NotifyChange("readyToPrint")
    @Command("guardar")
    public void guardar(){
        //Validacion
        String rta = getValidarVenta();
        if(!rta.isEmpty()){
            Messagebox.show(rta);
            
        }
        else {
        //Venta --> Insert + Lineas
        //Verifica si es insert o update
        if(selectedVenta.isDeudor()){
            selectedVenta.setMonto_deudor(selectedVenta.getCostesinIva());
        }else{
            selectedVenta.setMonto_deudor(0.0);
        }
        if(!modificar){
            ventaDao.insert(selectedVenta);
            lineaDao.insert(selectedVenta.getVenta_lineas());
        }else{
            ventaDao.update(selectedVenta);
            lineaDao.update(selectedVenta.getVenta_lineas());
        }
        modificar = true;
        readyToPrint = true;
        BindUtils.postGlobalCommand(null, null, "doSearchDeudores", null);
        BindUtils.postGlobalCommand(null, null, "refreshBoletas", null);
        Messagebox.show("Se ha guardardo la venta " + selectedVenta.getId());
        }
        
    }
    
    @Command("add")
    @NotifyChange({"events", "ventalineas", "costeventalineas"})
    public void add() {
        // cantidad, id_venta, id_producto, tipo, pvp, iva
        this.newEvent.setVenta(selectedVenta);
        this.newEvent.setProducto(selectedEvent.getProducto()); 
        
        selectedVenta.addVentaLinea(newEvent); 
        
        //lineaDao.insert(this.newEvent);
        //this.newEvent = new VentaLinea();
    }

    @Command("update")
    @NotifyChange({"selectedVenta", "ventalineas", "costeventalineas","selectedEvent"})
    public void update() {
        if(this.selectedEvent == null || selectedVenta == null ) return;
        selectedVenta.updateVentaLinea(selectedEvent);
        
        selectedEvent = null;
        //if(lineaDao.update(this.selectedEvent)) {
        //this.stockArticulo.setValue(this.stockArticulo.getValue() - cantidadArticulo.getValue());
        //}  else{            Messagebox.show("Cambio no realizado", "Error", Messagebox.OK, Messagebox.ERROR);       }
    }
    
    

    @Command("delete")
    @NotifyChange({"events", "ventalineas", "costeventalineas", "selectedEvent"})
    public void delete() {
        if(this.selectedEvent != null) {
            selectedVenta.removerVentaLinea(selectedEvent);
            //lineaDao.delete(this.selectedEvent);
            
            this.selectedEvent = null;
            /*
            this.nombreArticulo.setValue("");
            this.cantidadArticulo.setValue(0);
            this.pvpArticulo.setValue(0);
            this.stockArticulo.setValue(0);*/
        }
    }
    
    
    
    @Command
    public void seleccionar(@BindingParam("cmp")  final String x){
        selectedVenta.setDeudor(!x.equals("contado"));

    }
          
    
    
    // Actualiza El Cliente y el veterinario, pero una vez que ya se guardo la venta
    
    @GlobalCommand
    @NotifyChange("selectedVenta")
    public void refreshCliente(@BindingParam("returnCliente") Cliente cli)
    {
        this.selectedVenta.setCliente(cli);
        //if(!ventaDao.updateCliente(this.selectedVenta))  {            Messagebox.show("Cambio no realizado", "Error", Messagebox.OK, Messagebox.ERROR);        }
    }
    /*
    @Command("updateVet")
    @NotifyChange({"events", "ventalineas"})
    public void updateVet() {
        
        this.selectedVenta.setVeterinario(this.selectedVet);
        if(!ventaDao.updateVeterinario(this.selectedVenta)) {
            Messagebox.show("Cambio no realizado", "Error", Messagebox.OK, Messagebox.ERROR);
        }
    }
    */
    // ????????????????
    /*
    @Command
    public void addProducto()
    {
        final HashMap<String, Object> map = new HashMap<>();
        map.put("selectedCliente", selectedVenta );
        Executions.createComponents("/mascotas/mascota-asignar.zul", null, map);
    }*/
    
    @Command
    @NotifyChange({"selectedEvent", "ventalineas", "costeventalineas"})
    public void agregarProducto(@BindingParam("band") Bandbox band){
        String codigo = band.getValue();
        Producto producto = (new ProductoDAO().findByCodigo(codigo));
        if(producto != null){
            band.setValue("");
            List<Item> lista = new ArrayList<>();
            lista.add(producto);
            asignarItemToVenta(lista);
            /*
            VentaLinea vlinea = new VentaLinea();
            vlinea.setProducto(producto);                   
            vlinea.setVenta(selectedVenta);            
            vlinea.setCantidad(1);
            vlinea.setTipo(1);
            band.setValue("");
            if(!selectedVenta.contiene(vlinea)) {
                selectedVenta.asignarVentaLinea(vlinea);
                lineaDao.insert(vlinea);
                    
                
            }*/
            //else
               // Clients.showNotification("El producto ya se encuentra", Clients.NOTIFICATION_TYPE_INFO, band, "end_center", 3000);
        }
        else 
            Clients.showNotification("No se encuentra el producto", Clients.NOTIFICATION_TYPE_INFO, band, "end_center", 3000);
    }
    
    
    
    
    private void asignarItemToVenta(List<Item> items){
        for(Item x : items){
            VentaLinea vlinea = new VentaLinea();
            if(x instanceof Producto){
                vlinea.setProducto((Producto)x);
                vlinea.setTipo(1);
            }else{
                vlinea.setServicio((Servicio)x);
                vlinea.setTipo(2);
            }
            vlinea.setVenta(selectedVenta);             
            vlinea.setCantidad(1);
            vlinea.setPvp(vlinea.getItem().getPrecio());
            vlinea.setIva(vlinea.getItem().getIva().getValor());
            
            if(!selectedVenta.addVentaLinea(vlinea)) {
                Clients.showNotification("El producto ya se encuentra", Clients.NOTIFICATION_TYPE_INFO, null, null, 3000);
            }
            
        }
    }
    
    //Recoge los productos seleccionados
    @GlobalCommand
    @NotifyChange({"selectedEvent", "ventalineas", "costeventalineas"})
    public void refreshvalues(@BindingParam("returnProducto") Set<Producto> items)
    {
        asignarItemToVenta(new ArrayList<Item>(items));
    }
    
    @GlobalCommand
    @NotifyChange({"selectedEvent", "ventalineas", "costeventalineas"})
    public void refreshServicios(@BindingParam("returnServicio") Set<Servicio> items)
    {
        asignarItemToVenta(new ArrayList<Item>(items));
    }
    
    public void refreshPage() {
        final HashMap<String, Object> map = new HashMap<>();
        map.put("selectedVenta", selectedVenta );
        //Window win = (Window) page.getFellow("win");
        Borderlayout bl = (Borderlayout) Path.getComponent("/main");
        Center center = bl.getCenter();
        center.getChildren().clear();
        Executions.createComponents("venta-lineas.zul", center, map);
    }
    
    
    
    
    @Command
    public void imprimir() throws FileNotFoundException, IOException{
        BoletaPDF albaran = new BoletaPDF(this.selectedVenta);
        //albaran.setVenta(this.selectedVenta);
        albaran.createPdf();
        
        this.selectedVenta.descargarAlbaran();
    }


    public float getCosteventalineas() {
        costeventalineas = selectedVenta.getCostesinIva();
        /*Iterator <VentaLinea> itLinea = this.getVentalineas().iterator();
        while(itLinea.hasNext()){
            costeventalineas += itLinea.next().getMonto();
        }*/
        return costeventalineas;
    }
    

    public void setCosteventalineas(float costeventalineas) {
        this.costeventalineas = costeventalineas;
    }
    
    public int indexDeudor(){
        
        return selectedVenta.isDeudor()? 0 : 1;
    }
    
}
