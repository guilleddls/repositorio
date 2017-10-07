package com.clinica.veterinaria.pedido_linea;

import com.clinica.veterinaria.pedidos.Pedido;
import com.clinica.veterinaria.pedidos.PedidoDAO;
import com.clinica.veterinaria.productos.Producto;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.Property;
import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.Validator;
import org.zkoss.bind.annotation.*;
import org.zkoss.bind.validator.AbstractValidator;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zul.Borderlayout;
import org.zkoss.zul.Center;
import org.zkoss.zul.Messagebox;

/**
 *
 * 
 */
public class PedidoLineaViewModel {

    private List<PedidoLinea> pedidolineas = new ArrayList<>();
    private final PedidoLineaDAO eventDao = new PedidoLineaDAO();
    private final PedidoDAO pedidoDao = new PedidoDAO();

    private Pedido selectedPedido = new Pedido();
    private PedidoLinea newEvent = new PedidoLinea();
    private PedidoLinea selectedEvent = new PedidoLinea();


    public PedidoLinea getSelectedEvent() {
            return selectedEvent;
    }

    public void setSelectedEvent(PedidoLinea selectedEvent) {
            this.selectedEvent = selectedEvent;
    }

    public Pedido getSelectedPedido() {
            return selectedPedido;
    }

    public void setSelectedPedido(Pedido selectedPedido) {
            this.selectedPedido = selectedPedido;
    }

    public PedidoLinea getNewEvent() {
            return newEvent;
    }

    public void setNewEvent(PedidoLinea newEvent) {
            this.newEvent = newEvent;
    }

    public List<PedidoLinea> getEvents() {
            return eventDao.findAll();
    }

    public List<PedidoLinea> getPedidolineas() {
        
        if(this.selectedPedido != null) {
            return eventDao.findAll("select * from zk_pedido_linea where id_pedido = " + this.selectedPedido.getId());
        }
       else {
            return new ArrayList<>();
        }
    }

    public void setPedidolineas(List<PedidoLinea> pedidolineas) {
        this.pedidolineas = pedidolineas;
    }

    
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
                    int cant = (int) cantidad;
                    if(cant <= 0){
                        addInvalidMessage(ctx, "cantidad","Ingrese una cantidad mayor a cero");
                    }
                    
                }
                
                
            }
            
        };
        
    }
    
    
    @Init
    public void initSetup(@ContextParam(ContextType.VIEW) Component view, @ExecutionArgParam("selectedPedido") Pedido selectedPedido) 
    {
        Selectors.wireComponents(view, this, false);
        this.selectedPedido = selectedPedido;
        pedidolineas = getPedidolineas();
    }

    @Command("add")
    @NotifyChange({"events", "pedidolineas"})
    public void add() {
            this.newEvent.setPedido(selectedPedido);                //Relacion PedidoLinea-Pedido
            this.newEvent.setProducto(selectedEvent.getProducto()); //Relacion PedidoLinea-Producto
            selectedPedido.asignarPedidoLinea(newEvent);            //Relacion Pedido-PedidoLinea
            eventDao.insert(this.newEvent);
            this.newEvent = new PedidoLinea();
            
            refreshPage();
            
    }

    @Command("update")
    @NotifyChange({"events", "pedidolineas", "selectedEvent"})
    public void update() {
        if(this.selectedEvent != null) {
            if(this.selectedEvent.getId() != 0){
            if(!eventDao.update(this.selectedEvent)){
                Messagebox.show("Modificación no realizada", "Aviso", Messagebox.OK, Messagebox.ERROR);
            }}
        }

    }
    
    @Command("updatepedido")
    @NotifyChange("selectedPedido")
    public void updatepedido(){
        if(!pedidoDao.update(this.selectedPedido)){
            Messagebox.show("Modificación no realizada", "Aviso", Messagebox.OK, Messagebox.ERROR);
        }
    }

    @Command("delete")
    @NotifyChange({"events", "pedidolineas","selectedEvent"})
    public void delete() {
            //shouldn't be able to delete with selectedEvent being null anyway
            //unless trying to hack the system, so just ignore the request
            if(this.selectedEvent != null) {
                selectedPedido.removerLinea(selectedEvent);
                    eventDao.delete(this.selectedEvent);
                    this.selectedEvent = null;

            }
            else{
                this.selectedEvent = null;
                Messagebox.show("Seleccione un producto para eliminar", "Aviso", Messagebox.OK, Messagebox.ERROR);
            }
            
    }
    

    
    @GlobalCommand
    @NotifyChange({"selectedEvent", "pedidolineas"})
    public void refreshvalues(@BindingParam("returnProducto") Set <Producto> items)
    {
        Iterator <Producto> it = items.iterator();
        while(it.hasNext()){
            Producto item = it.next();
            PedidoLinea ped = new PedidoLinea();  
            ped.setProducto(item);                //Relacion PedidoLinea-Producto
            ped.setPedido(selectedPedido);        //Relacion PedidoLinea-Pedido
            ped.setCantidad(1);
            if(selectedPedido.agregarLinea(ped)) {
                eventDao.insert(ped);
            }
        }
    }
    
        public void refreshPage() {
        final HashMap<String, Object> map = new HashMap<>();
        map.put("selectedPedido", selectedPedido );
        //Window win = (Window) page.getFellow("win");
        Borderlayout bl = (Borderlayout) Path.getComponent("/main");
        Center center = bl.getCenter();
        center.getChildren().clear();
        Executions.createComponents("pedido-lineas.zul", center, map);
    }

    
    @Command
    public void send() {
        Map args = new HashMap();
        args.put("returnProveedor", this.selectedPedido.getProveedor());
        BindUtils.postGlobalCommand(null, null, "refreshProveedor", args);
    }
}
