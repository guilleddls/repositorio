package com.clinica.veterinaria.iva;

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
public class IvaDAO extends ManagerConexion{
        
	
	
	public List<Iva> findAll() {
		List<Iva> allEvents = new ArrayList<>();
		try {
			// get connection
		    Statement stmt = getStatement();
			ResultSet rs = stmt.executeQuery("select * from zk_iva");

			// fetch all events from database
			Iva iva;
			
			while (rs.next()) {
				iva = new Iva();
				iva.setId (rs.getInt(1));
                                iva.setValor(rs.getInt(2));
                                iva.setNombre (rs.getString(3));
                                iva.setDescripcion(rs.getString(4));
                                
				allEvents.add(iva);
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
	
        public List<Iva> findAll(String consulta) {
		List<Iva> allEvents = new ArrayList<>();
		try {
			// get connection
		    Statement stmt = getStatement();
			ResultSet rs = stmt.executeQuery(consulta);

			// fetch all events from database
			Iva iva;
			
			while (rs.next()) {
				iva = new Iva();
				iva.setId (rs.getInt(1));
                                iva.setValor(rs.getInt(2));
                                iva.setNombre (rs.getString(3));
                                iva.setDescripcion(rs.getString(4));
                                
				allEvents.add(iva);
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
                
	public boolean delete(Iva prod) {
		return execute("delete from zk_iva where id = '" + prod.getId() + "'");
	}
	
	public boolean insert(Iva prod) {
            if (prod.getDescripcion() == null)
                return execute("insert into zk_iva(valor, nombre, descripcion)" +
                               "values ('" + prod.getValor() + "', '" + prod.getNombre()    +   "', NULL)");
            else
                return execute("insert into zk_iva(valor, nombre, descripcion)" +
                               "values ('" + prod.getValor() + "', '" + prod.getNombre()    +   "', '" + prod.getDescripcion() + "')");
	}
	
	public boolean update(Iva prod) {
            if (prod.getDescripcion() == null)
                return execute("UPDATE zk_iva SET  "
                            + "valor = '" + prod.getValor()     + "', " 
                            + "nombre = '" + prod.getNombre()   + "', "
                            + "descripcion = NULL WHERE id = '"  + prod.getId() + "'");
            else
                return execute("UPDATE zk_iva SET  " 
                               + "valor = '" + prod.getValor()  + "', "
                               + "nombre = '" + prod.getNombre() + "', "
                               + "descripcion = '" + prod.getDescripcion() + "' WHERE id = '"  + prod.getId() + "'");
    }
	
	
}