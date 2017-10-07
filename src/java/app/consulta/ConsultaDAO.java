package app.consulta;

import com.clinica.veterinaria.mascotas.Mascota;
import com.clinica.veterinaria.mascotas.MascotaDAO;
import com.conexion.ManagerConexion;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * 
 */
public class ConsultaDAO extends ManagerConexion {
    private final String TABLA ="zk_consulta";
    
    public List<Consulta> findAll(){
        List<Consulta> consultas = new ArrayList<>();
        try{
            Statement stmt = getStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM "+ TABLA);
            List<Mascota> mascotas = new MascotaDAO().findAll();
            Consulta consulta;
            while(rs.next()){
                consulta = new Consulta();
                consulta.setId(rs.getInt(1));
                consulta.setFecha(rs.getDate(2));
                for(Mascota m: mascotas){
                    if(m.getId() == rs.getInt(3)){
                        consulta.setMascota(m);
                        break;
                    }
                }
                consulta.setNombre(rs.getString(4));
                consulta.setDescripcion(rs.getString(5));
                consulta.setVisto((rs.getInt(6) != 0));
                consultas.add(consulta);
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        
        return consultas;
    }
    
    public int getLastID(){
        int indice = 0;
        try{
            Statement stmt = getStatement();
            ResultSet rs = stmt.executeQuery("SELECT IFNULL(MAX(id),1) FROM "+ TABLA);
            rs.next();
            indice = rs.getInt(1);

            
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        
        return indice+1;
        
    }
    
    public int getCount(){
        int cant = 0;
        try{
            Statement stmt = getStatement();
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM "+ TABLA+ " WHERE visto = '0'");
            rs.next();
            cant = rs.getInt(1);
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return cant;
    }
    
    public void insert(String id_mascota, String nombre, String descripcion){
        if(id_mascota == null || nombre == null || descripcion == null) return;
        Consulta consulta =  new Consulta();
        Mascota mascota = new Mascota();
        mascota.setId(Integer.parseInt(id_mascota));
        consulta.setMascota(mascota);
        consulta.setNombre(nombre);
        consulta.setDescripcion(descripcion);
        insert(consulta);
        
    }
    
    public boolean insert(Consulta consulta){
        int indice = getLastID();
        java.sql.Date fecha = new java.sql.Date(new java.util.Date().getTime());
        //String fecha = new SimpleDateFormat("yyyy-MM-dd").format(it.getFecha());
        return execute("INSERT INTO "+TABLA+" VALUES ( "+indice+",'"+fecha+"',"+consulta.getMascota().getId()+", '"+ consulta.getNombre()+"', '"+ consulta.getDescripcion()+"',"+0+") ");
    }
    
    public boolean update(Consulta consulta){
        return execute("UPDATE zk_consulta SET id_mascota=" +consulta.getMascota().getId() +", nombre= '"+ consulta.getNombre()+"', descripcion='"+consulta.getDescripcion()+"', visto="+(consulta.isVisto()? 1:0) +" WHERE id = "+ consulta.getId());
    }
    
}
