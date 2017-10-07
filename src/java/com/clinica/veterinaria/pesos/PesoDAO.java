package com.clinica.veterinaria.pesos;

import com.clinica.veterinaria.mascotas.Mascota;
import com.clinica.veterinaria.mascotas.MascotaDAO;
import com.conexion.ManagerConexion;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * 
 */
public class PesoDAO extends ManagerConexion{
    
	
	
    public List<Peso> findAll() {
        return findAll("select * from zk_peso ORDER BY fecha DESC");

    }
	
    public List<Peso> findAll(String consulta) {
        List<Peso> allEvents = new ArrayList<>();
        try {
                // get connection
            Statement stmt = getStatement();
            ResultSet rs = stmt.executeQuery(consulta);

            // fetch all events from database
            Peso peso;
            List<Mascota> esplist = new MascotaDAO().findAll();
            while (rs.next()) {
                peso = new Peso();
                peso.setId (rs.getInt(1));
//                                peso.setEspecie (rs.getInt(2));
                peso.setValor(rs.getFloat(3));


                for(Mascota m : esplist){
                    if(m.getId() == rs.getInt(2)) {
                        peso.setMascota(m);
                        break;
                    }
                }

                peso.setFecha(rs.getDate(4));

                allEvents.add(peso);
            }
            closeResultSet(rs);
            closeStatement(stmt);
        } catch (SQLException e) {
                e.printStackTrace(System.err);
        } finally {
            close();
        }

        return allEvents;
    }
        
        
	public boolean delete(Peso prod) {
		return execute("delete from zk_peso where id = '" + prod.getId() + "'");
	}
	
	public boolean insert(Peso prod) {
            String fecha_hora = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()); //prod.getFecha();
		return execute("insert into zk_peso(mascota, valor, fecha)" +
                                "values ('" 
                                + prod.getMascota().getId() + "', '" 
                                + prod.getValor()           + "', '"
                                + fecha_hora           + "')");
	}
	
	public boolean update(Peso prod) {
            return execute("UPDATE zk_peso SET valor = '" + prod.getValor() + "' WHERE id = '"  + prod.getId() + "'");
    }
	
	
}