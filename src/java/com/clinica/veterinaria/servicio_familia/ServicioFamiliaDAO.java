package com.clinica.veterinaria.servicio_familia;

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
public class ServicioFamiliaDAO extends ManagerConexion {
        
	
	
	public List<ServicioFamilia> findAll() {
		List<ServicioFamilia> allEvents = new ArrayList<>();
		try {
			// get connection
		    Statement stmt = getStatement();
			ResultSet rs = stmt.executeQuery("select * from zk_servicio_familia");

			// fetch all events from database
			ServicioFamilia familia;
			
			while (rs.next()) {
				familia = new ServicioFamilia();
				familia.setId (rs.getInt(1));
                                familia.setNombre (rs.getString(2));
                                familia.setDescripcion(rs.getString(3));
                                
				allEvents.add(familia);
			}
		} catch (SQLException e) {
			e.printStackTrace(System.err);
		} finally {
		    close();
		}
		
		return allEvents;
	}
	
        public List<ServicioFamilia> findAll(String consulta) {
		List<ServicioFamilia> allEvents = new ArrayList<>();
		try {
			// get connection
		    Statement stmt = getStatement();
			ResultSet rs = stmt.executeQuery(consulta);

			// fetch all events from database
			ServicioFamilia familia;
			
			while (rs.next()) {
				familia = new ServicioFamilia();
				familia.setId (rs.getInt(1));
                                familia.setNombre (rs.getString(2));
                                familia.setDescripcion(rs.getString(3));
                                
				allEvents.add(familia);
			}
		} catch (SQLException e) {
			e.printStackTrace(System.err);
		} finally {
		    close();
		}
		
		return allEvents;
	}
                
	public boolean delete(ServicioFamilia prod) {
		return execute("delete from zk_servicio_familia where id = '" + prod.getId() + "'");
	}
	
	public boolean insert(ServicioFamilia prod) {
            if (prod.getDescripcion() == null)
                return execute("insert into zk_servicio_familia(nombre, descripcion)" +
                               "values ('" + prod.getNombre()    +   "', NULL)");
            else
                return execute("insert into zk_servicio_familia(nombre, descripcion)" +
                               "values ('" + prod.getNombre()    +   "', '" + prod.getDescripcion() + "')");
	}
	
	public boolean update(ServicioFamilia prod) {
            if (prod.getDescripcion() == null)
                return execute("UPDATE zk_servicio_familia SET nombre = '" + prod.getNombre() + "', descripcion = NULL WHERE id = '"  + prod.getId() + "'");
            else
                return execute("UPDATE zk_servicio_familia SET nombre = '" + prod.getNombre() + "', descripcion = '" + prod.getDescripcion() + "' WHERE id = '"  + prod.getId() + "'");
    }
	
	
}