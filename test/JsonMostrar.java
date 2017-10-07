
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JsonMostrar {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
//        List<Cliente> clientes = new app.json.ObjetoJSON().doAlgo("http://localhost:8084/veterinaria/json/entidad.jsp?objeto=clientes");
//        for(Cliente c : clientes){
//            System.out.println(c.getId() + " - " + c.getNombre());
//        }
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-mm-dd");
        Object o = "2015-06-29";
        Date fecha = null;
        try {
            fecha = sf.parse((String) o);
        } catch (ParseException ex) {
            Logger.getLogger(JsonMostrar.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println(fecha);
    }
    
}
