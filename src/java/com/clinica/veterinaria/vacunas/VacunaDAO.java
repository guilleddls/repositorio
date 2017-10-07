package com.clinica.veterinaria.vacunas;

import com.clinica.veterinaria.especies.Especie;
import com.clinica.veterinaria.especies.EspecieDAO;
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
public class VacunaDAO extends ManagerConexion{
    
	
	
	public List<Vacuna> findAll() {
		List<Vacuna> allEvents = new ArrayList<>();
		try {
			// get connection
                        Statement stmt = getStatement();
			ResultSet rs = stmt.executeQuery("select * from zk_vacuna");

			// fetch all events from database
			Vacuna vacuna;
			List<Especie> esplist = new EspecieDAO().findAll();
			while (rs.next()) {
				vacuna = new Vacuna();
				vacuna.setId (rs.getInt(1));
                                vacuna.setNombre(rs.getString(2));
                                vacuna.setDescripcion(rs.getString(3));
                                
                                for(Especie v: esplist){
                                    if(v.getId() == rs.getInt(4)){
                                        vacuna.setEspecie(v);
                                        break;
                                    }
                                }
                                /*
                                if(esplist.size() == 1) {
                                    vacuna.setEspecie(esplist.get(0));
                                }*/
                                
                                vacuna.setDias(rs.getInt(5));
                                vacuna.setFecha(rs.getDate(6));
                                
				allEvents.add(vacuna);
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
	
        public List<Vacuna> findAll(String consulta) {
		List<Vacuna> allEvents = new ArrayList<>();
		try {
			// get connection
                        Statement stmt = getStatement();
			ResultSet rs = stmt.executeQuery(consulta);

			// fetch all events from database
			Vacuna vacuna;
			
			while (rs.next()) {
				vacuna = new Vacuna();
				vacuna.setId (rs.getInt(1));
                                vacuna.setNombre(rs.getString(2));
                                vacuna.setDescripcion(rs.getString(3));
                                
                                EspecieDAO esp = new EspecieDAO();
                                List<Especie> esplist = esp.findAll("select * from zk_especie where id= " + rs.getInt(4));
                                if(esplist.size() == 1) {
                                    vacuna.setEspecie(esplist.get(0));
                                }
                                
                                vacuna.setDias(rs.getInt(5));
                                vacuna.setFecha(rs.getDate(6));
                                
				allEvents.add(vacuna);
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
        
	public boolean delete(Vacuna prod) {
		return execute("delete from zk_vacuna where id = " + prod.getId() );
	}
	
	public boolean insert(Vacuna prod) {
            String descripcion;
            if(prod.getDescripcion() == null){
                descripcion = "NULL";
            }
            else{
                descripcion = "'" + prod.getDescripcion() + "'";
            }
		return execute("insert into zk_vacuna(nombre, descripcion, especie, dias)" +
                                "values ('" 
                                            + prod.getNombre()          + "', " 
                                            + descripcion               + ", "   
                                            + prod.getEspecie().getId() + ", "
                                            + prod.getDias()    + ")");
	}
	
	public boolean update(Vacuna prod) {
            String descripcion;
            if(prod.getDescripcion() == null){
                descripcion = "NULL";
            }
            else{
                descripcion = "'" + prod.getDescripcion() + "'";
            }
            return execute("UPDATE zk_vacuna SET "
                            + "nombre = '" + prod.getNombre() + "', "
                            + "descripcion = " + descripcion + ", "
                            + "dias = "        + prod.getDias() + " "
                            + " WHERE id = "  + prod.getId() );
    }
	
	
}

