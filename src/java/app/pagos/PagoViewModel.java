package app.pagos;

import com.clinica.veterinaria.clientes.Cliente;
import com.clinica.veterinaria.ventas.Venta;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

public class PagoViewModel {
    private Venta selectedVenta;
    private Pago newPago = new Pago();
    private Pago selectedEvent = new Pago();

    public Venta getSelectedVenta() {
        return selectedVenta;
    }

    @NotifyChange
    public void setSelectedVenta(Venta selectedVenta) {
        this.selectedVenta = selectedVenta;
    }

    public Pago getNewPago() {
        return newPago;
    }

    @NotifyChange
    public void setNewPago(Pago newPago) {
        this.newPago = newPago;
    }

    public Pago getSelectedEvent() {
        return selectedEvent;
    }

    @NotifyChange
    public void setSelectedEvent(Pago selectedEvent) {
        this.selectedEvent = selectedEvent;
    }
    
    
    
    @Init
    public void initSetup(@ContextParam(ContextType.VIEW) Component view, 
                          @ExecutionArgParam("selectedVenta") Venta selectedVenta) {
        
        Selectors.wireComponents(view, this, false);
        
        this.selectedVenta = selectedVenta;
        newPago.setIdVenta(selectedVenta.getId());
        newPago.setFecha(new Date());
        newPago.setMonto(0.0);
            
        
    }
    
    public List<Pago> getPagos(){
        return selectedVenta== null? new PagoDAO().findAll() : new PagoDAO().findByVenta(selectedVenta.getId());
    }
    
  
  
   
    @Command
    @NotifyChange("pagos")
    public void add(@BindingParam("cmp")  final Window x){
        new PagoDAO().insert(newPago);
        BindUtils.postGlobalCommand(null, null, "refreshBoletas", null);
        Messagebox.show("El pago se ha efectuado", "Mensaje", Messagebox.OK, Messagebox.QUESTION,
            new org.zkoss.zk.ui.event.EventListener(){
                @Override
                public void onEvent(Event e){
                    if(Messagebox.ON_OK.equals(e.getName())){
                        x.detach();
                    }
                }
        });
    } 
    
    @Command
    @NotifyChange("pagos")
    public void delete(){
        new PagoDAO().delete(selectedEvent);
        BindUtils.postGlobalCommand(null, null, "refreshBoletas", null);
    } 
    
    @Command
    public void showPago(){
        final HashMap<String, Object> map = new HashMap<>();
        map.put("selectedVenta", selectedVenta );
        Executions.createComponents("/deudores/pago.zul",null,map);
    }
}
