package app.consulta;

import com.clinica.veterinaria.clientes.Cliente;
import com.clinica.veterinaria.mascotas.Mascota;
import java.util.Date;


public class Consulta {

    private int id;
    private Date fecha;
    private Mascota mascota;
    private String descripcion;
    private String nombre;
    private final String ruta;
    private boolean visto;
    
    public Consulta(){
        //ruta ="http://php-utnveterinaria.rhcloud.com/solicitudes/uploads/";
        ruta = "http://json-veterinaria.16mb.com/solicitudes/uploads/";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    
    
    public Mascota getMascota() {
        return mascota;
    }

    public void setMascota(Mascota mascota) {
        this.mascota = mascota;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
     
    public String getRuta(){
        return ruta+nombre;
    }
    
    public Cliente getCliente(){
        return mascota.getCliente();
    }
    
    public String getClienteNombre(){
        return getCliente().getApellidos()+" "+getCliente().getNombre();
    }

    public boolean isVisto() {
        return visto;
    }

    public void setVisto(boolean visto) {
        this.visto = visto;
    }
    
    
    
}
