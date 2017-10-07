package com.clinica.veterinaria.ventas;

import com.clinica.veterinaria.albaranes.BoletaPDF;
import com.clinica.veterinaria.clientes.Cliente;
import com.clinica.veterinaria.user.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.*;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;

/**
 *
 * 
 */
public class VentaViewModel {
    private final VentaDAO ventaDAO = new VentaDAO();
    private Venta selectedEvent, newEvent = new Venta();
    private Cliente selectedCliente;
    private String filterCliente = "", filterEmpleado =  "", filterFecha = "", filterFacturado = "", filterVeterinario = "", filterHora = "";
    private ListModelList<Venta> ventas;
    
    Session s = Sessions.getCurrent();
    //private Media albaran;
    
    public Venta getSelectedEvent() {
            return selectedEvent;
    }

    public void setSelectedEvent(Venta selectedEvent) {
            this.selectedEvent = selectedEvent;
    }

    public Cliente getSelectedCliente() {
        return selectedCliente;
    }

    public void setSelectedCliente(Cliente selectedCliente) {
        this.selectedCliente = selectedCliente;
    }

    public Venta getNewEvent() {
            return newEvent;
    }

    public void setNewEvent(Venta newEvent) {
            this.newEvent = newEvent;
    }

    public List<Venta> getEvents() {
            return ventaDAO.findAll();
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

    public String getFilterHora() {
        return filterHora;
    }

    @NotifyChange
    public void setFilterHora(String filterHora) {
        this.filterHora = filterHora;
    }
    
    public String getFilterFacturado() {
        return filterFacturado;
    }
    
    @NotifyChange
    public void setFilterFacturado(String filterFacturado) {
        this.filterFacturado = filterFacturado;
    }

    public String getFilterVeterinario() {
        return filterVeterinario;
    }

    @NotifyChange
    public void setFilterVeterinario(String filterVeterinario) {
        this.filterVeterinario = filterVeterinario;
    }
    
    
    @NotifyChange("ventas")
    public ListModelList<Venta> getVentas() {
        if(ventas == null) {
            ventas = new ListModelList<>();//new ListModelList<Venta>(eventDao.findAll());
        }
        return ventas;
    }
    
    @Command("add")
    @NotifyChange("ventas")
    public void add() {
            this.newEvent.setVendedor(UserCredentialManager.getIntance(s).getUser() );
            //this.newEvent.setFecha(new Date());
            //int id = ventaDAO.insert(this.newEvent);
        int id =0;            
        //Venta venta = this.newEvent;
            this.newEvent = new Venta();
            if(id > 0) {
                newEvent.setId(id);
                this.showVentaLinea(newEvent);
            }
    }
        
    @Command("update")
    @NotifyChange("ventas")
    public void update() {
          ventaDAO.update(this.selectedEvent);
    }

    @Command("delete")
    @NotifyChange({"ventas", "selectedEvent"})
    public void delete() {
       if(this.selectedEvent != null) {
            Messagebox.show("¿Desea elimimar la venta de " + selectedEvent.getCliente().getFullname() + " ?", "Advertencia", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION,
                new org.zkoss.zk.ui.event.EventListener(){
                    @Override
                    public void onEvent(Event e){
                        if(Messagebox.ON_OK.equals(e.getName())){
                            ventaDAO.delete(selectedEvent);
                            selectedEvent = null;
                            BindUtils.postGlobalCommand(null, null, "doSearch", null);

                        }
                    }
                }
            );
            
        }else 
             Messagebox.show("Seleccione una venta", "Mensaje", Messagebox.OK, Messagebox.EXCLAMATION);
           
    }
    
    @Command
    public void showVentaLinea()
    {
        final HashMap<String, Object> map = new HashMap<>();
        if(selectedEvent != null) {
            map.put("selectedVenta", selectedEvent );
        }
        else {
            map.put("selectedVenta", newEvent);
        }
        //Window win = (Window) page.getFellow("win");
        
        Executions.createComponents("/ventas/venta-lineas.zul", null, map);
    }
    
    public void showVentaLinea(Venta venta)
    {
        final HashMap<String, Object> map = new HashMap<>();
        map.put("selectedVenta", venta );
        //Window win = (Window) page.getFellow("win");
       
        Executions.createComponents("/ventas/venta-lineas.zul", null, map);
    }
    
    @GlobalCommand
    @NotifyChange("selectedCliente")
    public void refreshCliente(@BindingParam("returnCliente") Cliente cli)
    {
        this.newEvent.setCliente(cli);
        this.newEvent.setFecha(new Date());
//        venta.setVenta(selectedVenta);
        this.selectedCliente = cli;
//        this.add();  //Que al elegir el cliente vaya directamente a linea de venta
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
    
    
    
    
    
    // Buscador para hacer el filtrado
    @GlobalCommand
    @Command
    @NotifyChange("ventas")
    public void doSearch()
    {
        String[] listaFiltro = {filterHora, filterFacturado ,filterVeterinario, filterFecha, filterCliente, filterEmpleado};
        ventas = new ListModelList<>();
        List<Venta> allEvents = ventaDAO.findAll();
        if(areNullOrEmpty(listaFiltro)) {
            ventas.addAll(allEvents);
        }
        else {
            for(Venta venta : allEvents){
                String
                    facturado   = (venta.isDeudor())? "Si": "No",               
                    veterinario = (venta.getVeterinario() == null)? "":venta.getVeterinario().getNombre(),
                    cliente     = (venta.getCliente().getFullname() == null)? venta.getCliente().getNombre() :venta.getCliente().getFullname();
                Object[] objetoVta = {venta.getHora(), facturado, veterinario, venta.getFecha(), cliente, venta.getVendedor().getNombre()};

                if (filtrar(objetoVta, listaFiltro)) {
                    ventas.add(venta);
                }
            }
        }
    }
    /* FIN FILTER*/
    
    @Command
    @NotifyChange("ventas")
    public void crearAlbaran() throws FileNotFoundException, IOException{
        if(selectedEvent != null){
        BoletaPDF albaran = new BoletaPDF(this.selectedEvent);
        //albaran.setVenta(this.selectedEvent);
        albaran.createPdf();
        this.selectedEvent.descargarAlbaran();
//        FirstPdf.main();
//        MyFirstTable.main();
        }else
        Messagebox.show("Seleccione una venta", "Mensaje", Messagebox.OK, Messagebox.EXCLAMATION);
    }
    
    @Command
    public void descargarAlbaran() throws FileNotFoundException, IOException {
        if(selectedEvent != null){
        try{
            this.selectedEvent.descargarAlbaran();
        }catch (FileNotFoundException e){
            Messagebox.show("Factura no encontrado", "Aviso", Messagebox.OK, Messagebox.EXCLAMATION);
        }catch (IOException e) {
            Messagebox.show("Factura no encontrado", "Aviso", Messagebox.OK, Messagebox.EXCLAMATION);
        }
        }
        else Messagebox.show("Seleccione una venta", "Mensaje", Messagebox.OK, Messagebox.EXCLAMATION);
//        try{
//            if(this.selectedEvent.getAlbaran() == null){
//                Messagebox.show("Factura no encontrado", "Aviso", Messagebox.OK, Messagebox.EXCLAMATION);
//            }
//            else{
//                File f = new File(this.selectedEvent.getAlbaran() );
//                byte[] buffer = new byte[ (int) f.length() ];
//                FileInputStream fs = new FileInputStream(f);
//                fs.read( buffer ); 
//                fs.close();
//
//                ByteArrayInputStream is = new ByteArrayInputStream(buffer);
//                AMedia amedia =new AMedia("albaran-" + this.selectedEvent.getFecha() + "-" + this.selectedEvent.getId(), "pdf", "application/pdf", is);
//
//                if (is != null){
//                    Filedownload.save(amedia);
//                }
//                else{
//                    Messagebox.show("Factura no encontrado", "Aviso", Messagebox.OK, Messagebox.EXCLAMATION);
//                }
//            }
//        }catch (FileNotFoundException e){
//            Messagebox.show("Factura no encontrado", "Aviso", Messagebox.OK, Messagebox.EXCLAMATION);
//        }catch (IOException e) {
//            Messagebox.show("Factura no encontrado", "Aviso", Messagebox.OK, Messagebox.EXCLAMATION);
//        }
//        
    }
}
