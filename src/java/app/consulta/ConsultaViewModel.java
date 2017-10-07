package app.consulta;

import java.util.ArrayList;
import java.util.List;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Executions;

public class ConsultaViewModel {
    private List<Consulta> consultas;
    private final ConsultaDAO consultaDAO = new ConsultaDAO();
    private Consulta selectedEvent;
    private String filterFecha, filterCliente, filterMascota;
    private boolean filterVisto;
    
    public ConsultaViewModel(){
        consultas = new ArrayList<>();
        filterMascota = "";
        filterCliente ="";
        filterFecha ="";
        filterVisto = false;
    }
    
    @Init
    public void inicio(){
        consultas = consultaDAO.findAll();
    }
    
    public List<Consulta> getConsultas(){
        return consultas;
    }
    
    public Consulta getSelectedEvent(){
        return selectedEvent;
    }

    public String getFilterFecha() {
        return filterFecha;
    }

    public String getFilterCliente() {
        return filterCliente;
    }

    public String getFilterMascota() {
        return filterMascota;
    }

    public boolean isFilterVisto() {
        return filterVisto;
    }
	
    public int getCountConsulta(){
        return new ConsultaDAO().getCount();
    }
	
    @NotifyChange
    public void setSelectedEvent(Consulta selectedEvent) {
        this.selectedEvent = selectedEvent;
    }
    @NotifyChange
    public void setFilterFecha(String filterFecha) {
        this.filterFecha = filterFecha;
    }
    @NotifyChange
    public void setFilterCliente(String filterCliente) {
        this.filterCliente = filterCliente;
    }
    @NotifyChange
    public void setFilterMascota(String filterMascota) {
        this.filterMascota = filterMascota;
    }
    @NotifyChange
    public void setFilterVisto(boolean filterVisto) {
        this.filterVisto = filterVisto;
    }
    
	
    
    
    @Command
    @NotifyChange("countConsulta")
    public void visto(@BindingParam("item") Consulta item){
        selectedEvent = item;
        selectedEvent.setVisto(true);
        consultaDAO.update(selectedEvent);
        Executions.getCurrent().sendRedirect(this.selectedEvent.getRuta(),"_blank");
    }
    
    @Command
    @NotifyChange("consultas")
    public void doSearch(){
       
        consultas = new ArrayList<>();
        if(!notNullOrEmpty(filterCliente) && 
                !notNullOrEmpty(filterMascota) && 
                !notNullOrEmpty(filterFecha) 
                && !filterVisto
                )
        {
            consultas = consultaDAO.findAll();
        }
        else{
            //System.out.println("fuera:"+ filterFecha+","+ filterCliente+","+ filterMascota+","+ filterVisto);
            for(Consulta c : consultaDAO.findAll()){
                //System.out.println("consulta:"+ c.getFecha()+","+ c.getClienteNombre()+","+ c.getMascota().getNombre()+","+ c.isVisto());
                if(comparar(filterFecha, c.getFecha()+"") && 
                        comparar(filterCliente, c.getClienteNombre()) && 
                        comparar(filterMascota, c.getMascota().getNombre()) 
                        &&  filterVisto == c.isVisto()
                    ){
                
                    // System.out.println("dentro:"+ filterFecha+","+ filterCliente+","+ filterMascota+","+ filterVisto);
                    consultas.add(c); 
                }
            }
        }
    }
    
    private boolean notNullOrEmpty(Object oString){
       
        if(oString == null) return false;
        else {
            return (!String.valueOf(oString).isEmpty());
        }
              
    }
    
    private boolean comparar(String filtro, String campo){
        
        return (campo.toLowerCase().startsWith(filtro.toLowerCase()));
    }
}
