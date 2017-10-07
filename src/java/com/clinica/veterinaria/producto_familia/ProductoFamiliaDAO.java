package com.clinica.veterinaria.producto_familia;

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
public class ProductoFamiliaDAO extends ManagerConexion{
        
	
	
	public List<ProductoFamilia> findAll() {
		List<ProductoFamilia> allEvents = new ArrayList<>();
		try {
			// get connection
		    Statement stmt = getStatement();
			ResultSet rs = stmt.executeQuery("select * from zk_producto_familia");

			// fetch all events from database
			ProductoFamilia familia;
			
			while (rs.next()) {
				familia = new ProductoFamilia();
				familia.setId (rs.getInt(1));
                                familia.setNombre (rs.getString(2));
                                familia.setDescripcion(rs.getString(3));
                                if(rs.getInt(4) == 0) {
                                    familia.setTratamiento(false);
                                }
                                    else if(rs.getInt(4) == 1){
                                    familia.setTratamiento(true);
                                }
				allEvents.add(familia);
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
	
        public List<ProductoFamilia> findAll(String consulta) {
		List<ProductoFamilia> allEvents = new ArrayList<>();
		try {
			// get connection
		    Statement stmt = getStatement();
			ResultSet rs = stmt.executeQuery(consulta);

			// fetch all events from database
			ProductoFamilia familia;
			
			while (rs.next()) {
				familia = new ProductoFamilia();
				familia.setId (rs.getInt(1));
                                familia.setNombre (rs.getString(2));
                                familia.setDescripcion(rs.getString(3));
                                if(rs.getInt(4) == 0) {
                                    familia.setTratamiento(false);
                                }
                                    else {
                                    familia.setTratamiento(true);
                                }
                                
				allEvents.add(familia);
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
                
	public boolean delete(ProductoFamilia prod) {
		return execute("delete from zk_producto_familia where id = '" + prod.getId() + "'");
	}
	
	public boolean insert(ProductoFamilia prod) {
            int trat;
            if(prod.isTratamiento()) {
                trat = 1;
            }
            else {
                trat = 0;
            }
            
            if (prod.getDescripcion() == null) {
                return execute("insert into zk_producto_familia(nombre, descripcion, tratamiento)" +
                               "values ('" + prod.getNombre()    +   "', NULL," + trat +")");
            }
            else {
                return execute("insert into zk_producto_familia(nombre, descripcion, tratamiento)" +
                               "values ('" + prod.getNombre()    +   "', '" + prod.getDescripcion() + "', " + trat + ")");
            }
	}
	
	public boolean update(ProductoFamilia prod) {
            int trat;
            if(prod.isTratamiento()) {
                trat = 1;
            }
            else {
                trat = 0;
            }
            
            if (prod.getDescripcion() == null) {
                return execute("UPDATE zk_producto_familia SET nombre = '" + prod.getNombre() + "', descripcion = NULL, tratamiento=" + trat + " WHERE id = '"  + prod.getId() + "'");
            }
            else {
                return execute("UPDATE zk_producto_familia SET nombre = '" + prod.getNombre() + "', descripcion = '" + prod.getDescripcion() + "', tratamiento=" + trat + " WHERE id = '"  + prod.getId() + "'");
            }
    }
	
	
}