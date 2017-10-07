package com.clinica.veterinaria.ficheros;

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
public class FicheroDAO extends ManagerConexion{
    	
	
	
	public List<Fichero> findAll() {
		List<Fichero> allEvents = new ArrayList<>();
		try {
			// get connection
		    Statement stmt = getStatement();
                    ResultSet rs = stmt.executeQuery("select * from zk_ficheros");

                    // fetch all events from database
                    Fichero prod;

                    while (rs.next()) {
                            prod = new Fichero();
                            prod.setId (rs.getInt(1));
                            prod.setId_externo(rs.getInt(2));
                            prod.setTipo(rs.getInt(3));
                            prod.setRuta(rs.getString(4));
                            prod.setFecha(rs.getDate(5));

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
	
        public List<Fichero> findAll(String consulta) {
		List<Fichero> allEvents = new ArrayList<>();
		try {
			// get connection
		    Statement stmt = getStatement();
			ResultSet rs = stmt.executeQuery(consulta);

			// fetch all events from database
			Fichero prod;
			
			while (rs.next()) {
                            prod = new Fichero();
                            prod.setId (rs.getInt(1));
                            prod.setId_externo(rs.getInt(2));
                            prod.setTipo(rs.getInt(3));
                            prod.setRuta(rs.getString(4));
                            prod.setFecha(rs.getDate(5));

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
        
	public boolean delete(Fichero prod) {
//            System.out.println("delete from zk_ficheros where id = '" + prod.getId() + "'");
		return execute("delete from zk_ficheros where id = '" + prod.getId() + "'");
	}
	
	public int insert(Fichero archivo) {
            int index = lastID();
		if(execute("insert into zk_ficheros(id, id_externo, tipo, ruta)" +
                                "values ('" + index + "', " 
                                            + archivo.getId_externo()  + ", " 
                                            + archivo.getTipo()        + ",'"
                                            + archivo.getImagen()+ "')"))
                {
//                    System.out.println("SELECT * FROM zk_ficheros WHERE id_externo='"+ prod.getId_externo() + "' AND "
//                        + " tipo='"+ prod.getTipo() +"' AND ruta='"+prod.getRuta()+"'");
//                    List<Fichero> fichero = findAll("SELECT * FROM zk_ficheros WHERE id_externo='"+ archivo.getId_externo() + "' AND "
//                        + " tipo='"+ archivo.getTipo() +"' AND ruta='"+archivo.getRuta()+"'");
                    return index; //fichero.get(0).getId();
                }
                else{
                    System.out.println("Fichero NO insertado en la BD");
                    return 0;
                }
	}
	
        public int lastID(){
            int last_id = 0;
            try{
                Statement stmt = getStatement();
                ResultSet rs = stmt.executeQuery("SELECT IFNULL(MAX(id),0) FROM zk_ficheros");
                rs.next();
                last_id = rs.getInt(1);
                closeResultSet(rs);
                closeStatement(stmt);
            } catch (SQLException e) {
			e.printStackTrace(System.err);
		} finally {
		    close();
		}
            return last_id+1;

        }
        
        
//	public boolean update(Fichero prod) {
//            return execute("UPDATE zk_ficheros SET especie = '" + prod.getFichero() + "' WHERE id = '"  + prod.getId() + "'");
//    }
	
	
}