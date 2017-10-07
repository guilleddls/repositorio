package com.clinica.veterinaria.poblaciones;

/**
 * Event DAO.
 * 
 * 
 */
import com.clinica.veterinaria.provincias.Provincia;
import com.clinica.veterinaria.provincias.ProvinciaDAO;
import com.conexion.ManagerConexion;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PoblacionDAO extends ManagerConexion{	
	
	
	
	public List<Poblacion> findAll() {
		List<Poblacion> allEvents = new ArrayList<>();
		try {
			// get connection
		    Statement stmt = getStatement();
			ResultSet rs = stmt.executeQuery("select * from zk_poblacion");

			// fetch all events from database
			Poblacion prod;
			List<Provincia> provincias = new ProvinciaDAO().findAll();
			while (rs.next()) {
				prod = new Poblacion();
				prod.setId (rs.getInt(1));
                                prod.setProvincia (rs.getInt(2));
                                prod.setPoblacion (rs.getString(3));
                                for(Provincia p : provincias){
                                    if(prod.getProvincia() == p.getId()){
                                        prod.setProv(p);
                                        break;
                                    }
                                }
				allEvents.add(prod);
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
	
        public List<Poblacion> findAll(String consulta) {
		List<Poblacion> allEvents = new ArrayList<>();
		try {
			// get connection
		    Statement stmt = getStatement();
			ResultSet rs = stmt.executeQuery(consulta);

			// fetch all events from database
			Poblacion prod;
			List<Provincia> provincias = new ProvinciaDAO().findAll();
			while (rs.next()) {
				prod = new Poblacion();
				prod.setId (rs.getInt(1));
                                prod.setProvincia (rs.getInt(2));
                                prod.setPoblacion (rs.getString(3));
                                for(Provincia p : provincias){
                                    if(prod.getProvincia() == p.getId()){
                                        prod.setProv(p);
                                        break;
                                    }
                                }
				allEvents.add(prod);
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
        
        public List<String> findAllString() {
                List<String> allEvents = new ArrayList<>();
                try {
                        // get connection
                    Statement stmt = getStatement();
                        ResultSet rs = stmt.executeQuery("select * from zk_poblacion");

                        // fetch all events from database
                        Poblacion prod;

                        while (rs.next()) {
                                prod = new Poblacion();
                                prod.setId (rs.getInt(1));
                                prod.setProvincia (rs.getInt(2));
                                prod.setPoblacion (rs.getString(3));
                                allEvents.add(prod.getPoblacion());
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
                
	public boolean delete(Poblacion prod) {
		return execute("delete from zk_poblacion where id = " + prod.getId() );
	}
	
	public boolean insert(Poblacion prod) {
		return execute("insert into zk_poblacion(provincia, poblacion)" +
                                "values ( " + prod.getProv().getId() + " , '" + prod.getPoblacion() + "')");
	}
	
	public boolean update(Poblacion prod) {
            return execute("UPDATE zk_poblacion SET poblacion = '" + prod.getPoblacion() + "', provincia ="+prod.getProv().getId()+" WHERE id = "  + prod.getId() );
    }
	
	
}