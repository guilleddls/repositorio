package app.json;

import com.clinica.veterinaria.citas.Cita;
import com.clinica.veterinaria.citas.CitaDAO;
import com.clinica.veterinaria.clientes.Cliente;
import com.clinica.veterinaria.clientes.ClienteDAO;
import com.clinica.veterinaria.especies.Especie;
import com.clinica.veterinaria.especies.EspecieDAO;
import com.clinica.veterinaria.ficheros.Fichero;
import com.clinica.veterinaria.ficheros.FicheroDAO;
import com.clinica.veterinaria.historial.Historial;
import com.clinica.veterinaria.historial.HistorialDAO;
import com.clinica.veterinaria.mascota_vacuna.MascotaVacuna;
import com.clinica.veterinaria.mascota_vacuna.MascotaVacunaDAO;
import com.clinica.veterinaria.mascotas.Mascota;
import com.clinica.veterinaria.mascotas.MascotaDAO;
import com.clinica.veterinaria.pesos.Peso;
import com.clinica.veterinaria.pesos.PesoDAO;
import com.clinica.veterinaria.productos.Producto;
import com.clinica.veterinaria.productos.ProductoDAO;
import com.clinica.veterinaria.razas.Raza;
import com.clinica.veterinaria.razas.RazaDAO;
import com.clinica.veterinaria.servicio_familia.ServicioFamilia;
import com.clinica.veterinaria.servicio_familia.ServicioFamiliaDAO;
import com.clinica.veterinaria.servicios.Servicio;
import com.clinica.veterinaria.servicios.ServicioDAO;
import com.clinica.veterinaria.user.User;
import com.clinica.veterinaria.user.UserDAO;
import com.clinica.veterinaria.vacunas.Vacuna;
import com.clinica.veterinaria.vacunas.VacunaDAO;
import java.util.ArrayList;
import java.util.List;
import org.zkoss.json.JSONArray;
import org.zkoss.json.JSONObject;

/**
 *
 * 
 */
public class ObjetoJSON {
    
    // getting JSON string from URL
    
    JSONArray products;
    
     
    public List<Cliente> doAlgo(String url){
        JSONObject json = new JSONParser().makeHttpRequest(url, "POST", new ArrayList());
        List<Cliente> clientes = null;
        try {
            clientes = new ArrayList<>();
            // Checking for SUCCESS TAG
            int success = (int)json.get("success");

            if (success == 1) {
                // products found
                // Getting Array of Products
                products = (JSONArray) json.get("clientes");
                Cliente cliente;
                // looping through All Products
                //Log.i("ramiro", "produtos.length" + products.length());
                for (Object product : products) {
                    JSONObject c = (JSONObject) product;
                    // Storing each json item in variable
                    cliente = new Cliente();
                    cliente.setId((int) c.get("id"));
                    cliente.setNombre((String) c.get("nombre"));
                    cliente.setApellidos((String)c.get("apellido"));
                    cliente.setEmail((String)c.get("email"));
                    cliente.setUsuario((String)c.get("usuario"));
                    cliente.setPassword((String)c.get("password"));
                    
                    /*
                    // creating new HashMap
                        HashMap map = new HashMap();
                                    // adding each child node to HashMap key => value
                        map.put("id", id);
                        map.put("nombre", name);
                            */
                    
                    clientes.add(cliente);
                }
            }
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
        return clientes;
    }
    
    public String printJSON(String ref){
        if(ref == null) return "";
        
        switch(ref){
            case "clientes": return ClienteJSON();
            case "web": return WebJSON();
        }
        return "";
    }
    
    public String ClienteJSON(){
        List<Cliente> clientes = new ClienteDAO().findAll();
        List<Mascota> mascotas = new MascotaDAO().findAll();
        List<Especie> especies = new EspecieDAO().findAll();
        List<Raza> razas = new RazaDAO().findAll();
        List<Cita> citas = new CitaDAO().findAll();
        List<User> usuarios = new UserDAO().findAll();
        List<Historial> historias = new HistorialDAO().findAll();
        List<ServicioFamilia> servicios = new ServicioFamiliaDAO().findAll();
        List<Vacuna> vacunas = new VacunaDAO().findAll();
        List<MascotaVacuna> mascotaVacunas = new MascotaVacunaDAO().findAll();
        List<Peso> pesos = new PesoDAO().findAll();
        List<Fichero> archivos = new FicheroDAO().findAll();
        
        
        //HashMap<String, Object> map;
        
        JSONObject stringJSON = new JSONObject();
        
        JSONObject map;
        Object[] arreglo;
        int i;
        // JSON CLIENTES
        arreglo = new Object[clientes.size()];
        i = 0;
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
        stringJSON.put("clientes", arreglo);
        
        // JSON MASCOTAS
        arreglo = new Object[mascotas.size()];
        i = 0;
        for(Mascota m : mascotas){
            map = new JSONObject();
            map.put("id",m.getId());
            map.put("codigo", m.getChip());
            map.put("nombre", m.getNombre());
            map.put("sexo", m.getSexo());
            map.put("id_cliente", m.getCliente()!= null? m.getCliente().getId(): 0);
            map.put("id_raza", m.getRaza()!=null? m.getRaza().getId() : 0);
            map.put("id_especie", m.getEspecie() != null? m.getEspecie().getId() : 0);
            map.put("peso",m.getPeso());
            map.put("pelo", m.getPelo());
            map.put("altura", m.getAltura());
            map.put("observacion",m.getObserv());
            map.put("nacimiento", m.getNacimiento()+"");
            map.put("fechaAlta", m.getFechaalta()+"");
            map.put("fechaBaja", m.getFechabaja()+"");
            map.put("defuncion",m.getDefuncion()+"");
            map.put("foto", m.getFoto());
            arreglo[i] = map;
            i++;
        }
        stringJSON.put("mascotas", arreglo);
        
        // ESPECIES JSON
        arreglo = new Object[especies.size()];
        i = 0;
        for(Especie e : especies){
            map = new JSONObject();
            map.put("id",e.getId());
            map.put("nombre", e.getEspecie());
            arreglo[i] = map;
            i++;
        }
        stringJSON.put("especies", arreglo);
        
        // RAZAS JSON
        arreglo = new Object[razas.size()];
        i = 0;
        for(Raza r : razas){
            map = new JSONObject();
            map.put("id",r.getId());
            map.put("nombre", r.getRaza());
            map.put("id_especie", r.getEspecie() != null? r.getEspecie().getId(): 0);
            arreglo[i] = map;
            i++;
        }
        stringJSON.put("razas", arreglo);
        
        // CITAS JSON
        
        arreglo = new Object[citas.size()];
        i = 0;
        for(Cita c : citas){
            map = new JSONObject();
            map.put("id", c.getId());
            map.put("fecha", c.getFecha()+"");
            map.put("hora",c.getHora()+"");
            map.put("informe", c.getInforme());
            map.put("estado", c.getEstado());
            map.put("id_sevicio", c.getServicio()!=null? c.getServicio().getId():0);
            map.put("id_mascota", c.getMascota() !=null? c.getMascota().getId(): 0);
            map.put("id_cliente", c.getCliente()!= null? c.getCliente().getId(): 0);
            map.put("id_empleado", c.getEmpleado() !=null? c.getEmpleado().getId(): 0);
            arreglo[i] = map;
            i++;
        }
        stringJSON.put("citas", arreglo);
        
        // USUARIOS JSON
        
        arreglo = new Object[usuarios.size()];
        i = 0;
        for(User c : usuarios){
            map = new JSONObject();
            map.put("id",c.getId());
            map.put("nombre", c.getNombre());
            map.put("apellido", c.getApellidos());
            map.put("email", c.getEmail());
            map.put("usuario", c.getUser());
            map.put("password", c.getPassword());
            map.put("tipo", c.getTipo());
            arreglo[i] = map;
            i++;
        }
        stringJSON.put("usuarios", arreglo);
        
        // Historia JSON
        arreglo = new Object[historias.size()];
        i = 0;
        for(Historial h : historias){
            map = new JSONObject();
            map.put("id", h.getId());
            map.put("id_mascota", h.getMascota() != null? h.getMascota().getId() : 0);
//            map.put("id_veterinario", h.getId_veterinario() !=null? h.getId_veterinario().getId() : null);
            map.put("peso", h.getPeso().getValor());
            map.put("fecha", h.getFecha()+"");
            map.put("hora", h.getHora()+"");
            map.put("tipo", h.getTipo());
            map.put("tratamiento", h.getTratamiento());
            map.put("diagnostico", h.getDiagnostico());
            map.put("anamnesis", h.getAnamnesis());
            arreglo[i] = map;
            i++;
        }
        stringJSON.put("historias", arreglo);
        
        ///////////////////////////////////////////////////
        // JSON SERVICIOS
        arreglo = new Object[servicios.size()];
        i = 0;
        for(ServicioFamilia s : servicios){
            map = new JSONObject();
            map.put("id", s.getId());
            map.put("nombre",s.getNombre());
            map.put("descripcion",s.getDescripcion());
            arreglo[i] = map;
            i++;
        }
        stringJSON.put("servicios", arreglo);
        
        ///////////////////////////////////////////////////
        // JSON VACUNAS
        arreglo = new Object[vacunas.size()];
        i = 0;
        for(Vacuna v : vacunas){
            map = new JSONObject();
            map.put("id", v.getId());
            map.put("nombre", v.getNombre());
            map.put("descripcion", v.getDescripcion());
            map.put("id_especie", v.getEspecie() != null? v.getEspecie().getId() : 0);
            arreglo[i] = map;
            i++;
        }
        stringJSON.put("vacunas", arreglo);
        
        ///////////////////////////////////////////////////
        // JSON MASCOTAS-VACUNAS
        arreglo = new Object[mascotaVacunas.size()];
        i = 0;
        for(MascotaVacuna v : mascotaVacunas){
            map = new JSONObject();
            map.put("id", v.getId());
            map.put("fecha", v.getFecha()+"");
            map.put("hora", v.getHora()+"");
            map.put("id_mascota", v.getMascota() != null ? v.getMascota().getId() : 0);
            map.put("id_vacuna", v.getVacuna()!= null ? v.getVacuna().getId() : 0);
            map.put("id_veterinario", v.getVeterinario() != null ? v.getVeterinario().getId() : 0);
            arreglo[i] = map;
            i++;
        }
        stringJSON.put("mascotaVacunas", arreglo);
        
        ///////////////////////////////////////////////////
        // JSON PESOS
        arreglo = new Object[pesos.size()];
        i = 0;
        for(Peso p : pesos){
            map = new JSONObject();
            map.put("id", p.getId());
            map.put("valor", p.getValor());
            map.put("fecha", p.getFecha()+"");
            map.put("id_mascota", p.getMascota() != null ? p.getMascota().getId() : 0);
            arreglo[i] = map;
            i++;
        }
        stringJSON.put("pesos", arreglo);
        
        ///////////////////////////////////////////////////
        // JSON ARCHIVOS
        arreglo = new Object[archivos.size()];
        i = 0;
        for(Fichero p : archivos){
            map = new JSONObject();
            map.put("id", p.getId());
            map.put("id_historia", p.getId_externo());
            //map.put("nombre", p.getNombre());
            map.put("ruta",p.getRuta());
            arreglo[i] = map;
            i++;
        }
        stringJSON.put("archivos", arreglo);
        
        if(!clientes.isEmpty())
        {
            stringJSON.put("success", 1);
        }
        else
        {
            stringJSON.put("success", 0);
            stringJSON.put("error", "Mensaje de error");
        }
        return stringJSON.toString();
    }
    
    
    public String WebJSON(){
        List<Producto> productos = new ProductoDAO().findAll();
        List<Servicio> servicios = new ServicioDAO().findAll();
        
        //HashMap<String, Object> map;
        
        JSONObject stringJSON = new JSONObject();
        
        JSONObject map;
        Object[] arreglo;
        int i;
        // JSON CLIENTES
        arreglo = new Object[productos.size()];
        i = 0;
        for(Producto p : productos){
            //System.out.println(toJSONString(c.getCodigopostal()));
            map = new JSONObject();//LinkedHashMap();
            map.put("id",p.getId());
            map.put("nombre", p.getNombre());
            map.put("familia", p.getFamilia().getDescripcion());
            map.put("descripcion", p.getDescripcion());
            map.put("precio", p.getPvp());
            map.put("fotografia", p.getFotografia());
            arreglo[i] = map;
            i++;
        }
        stringJSON.put("productos", arreglo);
        
        arreglo = new Object[servicios.size()];
        i = 0;
        for(Servicio p : servicios){
            //System.out.println(toJSONString(c.getCodigopostal()));
            map = new JSONObject();//LinkedHashMap();
            map.put("id",p.getId());
            map.put("nombre", p.getNombre());
            map.put("familia", p.getFamilia().getDescripcion());
            map.put("descripcion", p.getDescripcion());
            map.put("precio", p.getPrecio());
          
            arreglo[i] = map;
            i++;
        }
        stringJSON.put("servicios", arreglo);
        
        
        
        if(!productos.isEmpty() && !servicios.isEmpty())
        {
            stringJSON.put("success", 1);
        }
        else
        {
            stringJSON.put("success", 0);
            stringJSON.put("error", "Mensaje de error");
        }
        return stringJSON.toString();
    }
}
