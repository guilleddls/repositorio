package com.clinica.veterinaria.ventas;

import com.clinica.veterinaria.clientes.Cliente;
import com.clinica.veterinaria.facturas.Factura;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.*;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zul.ListModelList;

/**
 *
 * 
 */
public class VentaFacturaVM {

    private final VentaDAO eventDao = new VentaDAO();
    private Cliente selectedCliente;
    private String filterCliente = "";
    private String filterEmpleado =  "";
    private String filterFecha = "";
    private ListModelList<Venta> ventas;
    private Set <Venta> selectedItems;
    
    
    @Init
    public void initSetup(@ContextParam(ContextType.VIEW) Component view, 
                          @ExecutionArgParam("selectedFactura") Factura selectedFactura) 
    {
        Selectors.wireComponents(view, this, false);
        this.selectedCliente = selectedFactura.getCliente();
    }
    
    public Set<Venta> getSelectedItems() {
        return selectedItems;
    }

    public void setSelectedItems(Set<Venta> selectedItems) {
        this.selectedItems = selectedItems;
    }
    
    public List<Venta> getEvents() {
            return eventDao.findAll();
    }

    public String getFilterCliente() {
        return filterCliente;
    }

    @NotifyChange
    public void setFilterCliente(String filterCliente) {
        this.filterCliente = filterCliente;
    }

    public String getFilterEmpleado() {
        return filterEmpleado;
    }

    @NotifyChange
    public void setFilterEmpleado(String filterEmpleado) {
        this.filterEmpleado = filterEmpleado;
    }

    public String getFilterFecha() {
        return filterFecha;
    }

    @NotifyChange
    public void setFilterFecha(String filterFecha) {
        this.filterFecha = filterFecha;
    }
    
    @NotifyChange("ventas")
    public ListModelList<Venta> getVentas() {
        if(ventas == null) {
            ventas = new ListModelList<>(eventDao.findAll("SELECT * FROM zk_venta WHERE id_cliente=" + selectedCliente.getId() + " AND facturado=0"));
        }
        ventas.setMultiple(true);
        return ventas;
    }
    
    
    // Buscador para hacer el filtrado
    @Command
    @NotifyChange("ventas")
    public void doSearch() {
        ventas.clear();
        List<Venta> allEvents = eventDao.findAll("SELECT * FROM zk_venta WHERE id_cliente=" + selectedCliente.getId() );

        if((filterFecha    == null || filterFecha.equals(""))      &&
           (filterCliente  == null || filterCliente.equals(""))    &&
           (filterEmpleado == null || filterEmpleado.equals(""))) 
        {
                ventas.addAll(allEvents);
        }
        else {
            for (Venta clie : allEvents) {
                if((clie.getCliente() .getFullname().toLowerCase().startsWith(filterCliente.toLowerCase())   )    &&
                   (clie.getVendedor().getNombre().toLowerCase()  .startsWith(filterEmpleado.toLowerCase())  )    &&
                   (clie.getFecha()   .toString().toLowerCase()   .startsWith(filterFecha.toLowerCase())  )) {
                    
                    ventas.add(clie);
                }
            } 
        }
    }
    /* FIN FILTER*/
    
    
    //Se llama desde factura-buscarventa.zul y lo recibe factura.zul en FacturaDetalladoVM.java
    @Command
    public void sendVentas() {
        Map args = new HashMap();
        args.put("returnVenta", this.selectedItems);
        BindUtils.postGlobalCommand(null, null, "refreshVentas", args);
    }
    
}
