
package com.clinica.veterinaria.servicios;

import com.clinica.veterinaria.iva.Iva;
import com.clinica.veterinaria.iva.IvaDAO;
import com.clinica.veterinaria.servicio_familia.ServicioFamilia;
import com.clinica.veterinaria.servicio_familia.ServicioFamiliaDAO;
import com.conexion.ManagerConexion;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 */
public class ServicioDAO extends ManagerConexion{
    
	
	
	public List<Servicio> findAll() {
            return findAll("select * from zk_servicio");
            /*
		List<Servicio> allEvents = new ArrayList<Servicio>();
		try {
			// get connection
		    Statement stmt = getStatement();
			ResultSet rs = stmt.executeQuery("select * from zk_servicio");

			// fetch all events from database
			Servicio serv;
			
			while (rs.next()) {
				serv = new Servicio();
				serv.setId (rs.getInt(1));
                                serv.setCodigo (rs.getString(2));
                                serv.setNombre (rs.getString(3));
                                serv.setDescripcion(rs.getString(4));
                                serv.setPrecio(rs.getFloat(5));
                                
                                IvaDAO iva = new IvaDAO();
                                List<Iva> ivalist = iva.findAll("select * from zk_iva where id= " + rs.getInt(6));
                                if(ivalist.size() == 1) {
                                    serv.setIva(ivalist.get(0));
                                }
                                
                                ServicioFamiliaDAO familia = new ServicioFamiliaDAO();
                                List<ServicioFamilia> famlist = familia.findAll("select * from zk_servicio_familia where id= " + rs.getInt(7));
                                if(famlist.size() == 1) {
                                    serv.setFamilia(famlist.get(0));
                                }
                                
				allEvents.add(serv);
			}
		} catch (SQLException e) {
			e.printStackTrace(System.err);
		} finally {
		    close();
		}
		
		return allEvents;
                    */
	}
	
        public List<Servicio> findAll(String consulta) {
            List<Servicio> allEvents = new ArrayList<>();
		try {
			// get connection
		    Statement stmt = getStatement();
			ResultSet rs = stmt.executeQuery(consulta);

			// fetch all events from database
			Servicio serv;
			List<Iva> ivalist = new IvaDAO().findAll();
                        List<ServicioFamilia> famlist = new ServicioFamiliaDAO().findAll();
			while (rs.next()) {
				serv = new Servicio();
				serv.setId (rs.getInt(1));
                                serv.setCodigo (rs.getString(2));
                                serv.setNombre (rs.getString(3));
                                serv.setDescripcion(rs.getString(4));
                                serv.setPrecio(rs.getFloat(5));
                                for(Iva i : ivalist){
                                    if(i.getId()== rs.getInt(6)){
                                        serv.setIva(i);
                                    }                                   
                                }
                                
                                for(ServicioFamilia f : famlist){
                                    if(f.getId()== rs.getInt(7)){
                                        serv.setFamilia(f);
                                    }
                                    
                                }
                                
                                
                                
				allEvents.add(serv);
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
        
                
	public boolean delete(Servicio prod) {
		return execute("delete from zk_servicio where id = " + prod.getId() );
	}
	
	public boolean insert(Servicio prod) {
		return execute("insert into zk_servicio(codigo, servicio, descripcion, precio, id_iva, id_familia)" +
                               "values ('" + 
                                prod.getCodigo()        + "', '" + 
                                prod.getNombre()        + "', '" + 
                                prod.getDescripcion()   + "', " +
                                prod.getPrecio()        + ", " +
                                prod.getIva().getId()   + ",  "+
                                prod.getFamilia().getId() + ")");
	}
	
	public boolean update(Servicio prod) {
            if(prod.getIva() != null) {
                System.out.println("IVA:  " + prod.getIva().getId() );
            }
            else {
                System.out.println("IVA:  NULL" );
            }
            return execute("UPDATE zk_servicio SET "
                    + "codigo = '"      + prod.getCodigo()      + "', "
                    + "servicio = '"    + prod.getNombre()    + "', "
                    + "descripcion = '" + prod.getDescripcion() + "', "
                    + "precio = "       + prod.getPrecio()      + ", " 
                    + "id_iva = "       + prod.getIva().getId() + " "
                    + "WHERE id = "    + prod.getId()       );
    }
	
	
}