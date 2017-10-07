
import com.clinica.veterinaria.ventas.Venta;
import com.clinica.veterinaria.ventas_linea.VentaLinea;
import com.clinica.veterinaria.ventas_linea.VentaLineaDAO;



public class Reporte {
   
    public static void main(String[] args){
        //StringBuilder sb = new StringBuilder(String.format("/boleta-%tF-%d.pdf", new Date(), 1 ));
        //System.out.println("SB: " + sb);

        //sb.delete(0, 12);
        //String realpath = Executions.getCurrent().getDesktop().getWebApp().getRealPath("/albaranes");
        //System.out.println(realpath + sb.toString());
        /*Venta selectedVenta = new Venta();
        selectedVenta.setId(94);;
        selectedVenta.setVenta_lineas(new VentaLineaDAO().findByVenta(selectedVenta));
        for(VentaLinea x : selectedVenta.getVenta_lineas()){
            System.out.println(x.getId());
        }*/
        
        String rta = "99-11132791-3";
        System.out.println(rta.substring(0, 2)); // if(esto = 99 -> crea 99-00000001-9)
        int asd = (Integer.parseInt(rta.substring(3, 11))) +1; //.replace("-","")
        System.out.println("99-"+asd+"-9");
        
    }
    
    

    
    
    
    
   
    
}
