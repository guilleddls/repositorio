
import app.consulta.Consulta;
import app.consulta.ConsultaDAO;
import app.iva.Iva;
import app.iva.IvaCompra;
import app.iva.IvaDAO;
import app.iva.IvaVenta;
import com.clinica.veterinaria.clientes.Cliente;
import com.clinica.veterinaria.clientes.ClienteDAO;
import com.clinica.veterinaria.mascotas.Mascota;
import com.clinica.veterinaria.proveedores.Proveedor;
import com.clinica.veterinaria.proveedores.ProveedorDAO;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
public class NewMain {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
       /*
        List<Cliente> clientes = new ClienteDAO().findAll();
        Object[] arreglo = new Object[clientes.size()];
        //HashMap<String, Object> map;
        JSONObject map;
//        map.put("value1", 1 );
//        map.put("valor", 2);
        int i = 0;
        for(Cliente c : clientes){
            //System.out.println(toJSONString(c.getCodigopostal()));
            map = new JSONObject();//LinkedHashMap();
            map.put("id",c.getId());
            map.put("nombre", c.getNombre());
            map.put("apellido", c.getApellidos());
            map.put("email", c.getEmail());
            map.put("usuario", c.getUsuario());
            map.put("password", c.getPassword());
            arreglo[i] = map;
            i++;
        }
        
        JSONObject rta = new JSONObject();
        rta.put("clientes", arreglo);
        if(!clientes.isEmpty())
        {
            rta.put("success", 1);
        }
        else
        {
            rta.put("success", 0);
            rta.put("error", "Mensaje de error");
        }
        
        //System.out.println("{\"clientes:"+(toJSONString(arreglo))+",\"success\":1}");
        System.out.println(rta);
               */
        //System.out.println(new app.consulta.ConsultaDAO().getCount()+"");
        double asd = 12.0;
        //System.out.println(String.format(java.util.Locale.US,"%1$tY-%1$tm-%1$td", new java.util.Date()));
        
        
        //List<Proveedor> clientes = new ProveedorDAO().findAll();
       
        
        //List<IvaCompra> ivas = new IvaDAO().findAllCompra();
        /*
        for(int i = 0; i<3; i++){
            IvaCompra iva =  new IvaCompra();
            iva.setFecha(new Date());
            iva.setNro_puesto(1);
            iva.setNro_factura(i);
            iva.setTipo("A");
            iva.setProveedor(clientes.get(i));
            iva.setTotal(10*i);
            iva.setIva21(2.1*i);
            iva.setNetogravado(8.9*i);
            iva.setAnulado(false);
            new IvaDAO().insert(iva);
        }
               
        for(IvaCompra iva : ivas){
            Object[] o ={iva.getId(), iva.getFecha(), iva.getTipo(), iva.getNro_puesto(), iva.getNro_factura(), iva.getPersona(), iva.getNetogravado(), iva.getIva21(), iva.getIva27(), iva.getIva10(), iva.getNogravado(), iva.getRetencion(), iva.getOpexcentas(), iva.getTotal(), (iva.isAnulado()?1:0) };
            System.out.println(Arrays.toString(o));
        }
             
        
        IvaCompra iva =  new IvaCompra();
            iva.setFecha(new Date());
            iva.setId(1);
            iva.setNro_puesto(1);
            iva.setNro_factura(10);
            iva.setTipo("A");
            iva.setProveedor(clientes.get(1));
            iva.setTotal(100);
            iva.setIva21(21);
            iva.setNetogravado(89);
            iva.setAnulado(false);
         
        new IvaDAO().update(iva);
       */   
        
        System.out.println("31-12-2014".substring(3, 5));
    }
    
}
