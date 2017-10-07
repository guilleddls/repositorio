package com.clinica.veterinaria.razas;

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
public class RazaDAO extends ManagerConexion {
    
	
	
	public List<Raza> findAll() {
            List<Raza> allEvents = new ArrayList<>();
            try {
                    // get connection
                Statement stmt = getStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM zk_raza INNER JOIN zk_especie ON zk_raza.`especie` = zk_especie.`id`");
                //List<Especie> esplist = new EspecieDAO().findAll("select * from zk_especie");
                // fetch all events from database
                Raza raza;
                Especie especie;
                while (rs.next()) {
                    raza = new Raza();
                    raza.setId (rs.getInt(1));
                    raza.setRaza(rs.getString(3));
                    especie = new Especie();
                    especie.setId(rs.getInt(4));
                    especie.setEspecie(rs.getString(5));
                    raza.setEspecie(especie);
                    
                    /*
                    EspecieDAO esp = new EspecieDAO();
                    List<Especie> esplist = esp.findAll("select * from zk_especie where id= " + rs.getInt(2));
                    if(esplist.size() == 1)
                        raza.setEspecie(esplist.get(0));
                    */
                    allEvents.add(raza);
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
	
        public List<Raza> findAll(String consulta) {
		List<Raza> allEvents = new ArrayList<>();
		try {
			// get connection
		    Statement stmt = getStatement();
			ResultSet rs = stmt.executeQuery(consulta);

			// fetch all events from database
			Raza raza;
			
			while (rs.next()) {
				raza = new Raza();
				raza.setId (rs.getInt(1));
//                                raza.setEspecie (rs.getInt(2));
                                raza.setRaza(rs.getString(3));
                                
                                EspecieDAO esp = new EspecieDAO();
                                List<Especie> esplist = esp.findAll("select * from zk_especie where id= " + rs.getInt(2));
                                if(esplist.size() == 1)
                                    raza.setEspecie(esplist.get(0));
                                
				allEvents.add(raza);
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
                        ResultSet rs = stmt.executeQuery("select * from zk_raza");

                        // fetch all events from database
                        Raza prod;

                        while (rs.next()) {
                                prod = new Raza();
                                prod.setId (rs.getInt(1));
//                                prod.setEspecie (rs.getInt(2));
                                prod.setRaza(rs.getString(3));
                                allEvents.add(prod.getRaza());
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
                
	public boolean delete(Raza prod) {
		return execute("delete from zk_raza where id = " + prod.getId() );
	}
	
	public boolean insert(Raza prod) {
		return execute("insert into zk_raza(especie, raza)" +
                                "values (" + prod.getEspecie().getId() + ", '" + prod.getRaza()    +   "')");
	}
	
	public boolean update(Raza prod) {
            return execute("UPDATE zk_raza SET raza = '" + prod.getRaza() + "', especie = " + prod.getEspecie().getId() + " WHERE id = "  + prod.getId() );
    }
	
	
}