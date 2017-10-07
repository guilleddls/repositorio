package com.clinica.veterinaria.mascota_vacuna;

import com.clinica.veterinaria.mascotas.Mascota;
import com.clinica.veterinaria.mascotas.MascotaDAO;
import com.clinica.veterinaria.user.User;
import com.clinica.veterinaria.user.UserDAO;
import com.clinica.veterinaria.vacunas.Vacuna;
import com.clinica.veterinaria.vacunas.VacunaDAO;
import com.conexion.ManagerConexion;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 */
public class MascotaVacunaDAO extends ManagerConexion{
        
	
	
	public List<MascotaVacuna> findAll() {
		List<MascotaVacuna> allEvents = new ArrayList<>();
		try {
			// get connection
		    Statement stmt = getStatement();
			ResultSet rs = stmt.executeQuery("select * from zk_mascota_vacuna");

			// fetch all events from database
			MascotaVacuna mascvac;
			   List<Mascota> esplist = new MascotaDAO().findAll();
                           List<Vacuna> vaclist = new VacunaDAO().findAll();
                           List<User> userlist = new UserDAO().findAll();
                                
			while (rs.next()) {
				mascvac = new MascotaVacuna();
				mascvac.setId (rs.getInt(1));
                                
                                for(Mascota m : esplist){
                                    if(m.getId() == rs.getInt(2)){
                                        mascvac.setMascota(m);
                                        break;
                                    }
                                }
                                
                                for(Vacuna v: vaclist){
                                    if(v.getId() == rs.getInt(3)){
                                        mascvac.setVacuna(v);
                                        break;
                                    }
                                    
                                }
                                
                                for(User u : userlist){
                                    if(u.getId() == rs.getInt(5)){
                                        mascvac.setVeterinario(u);
                                        break;
                                    }
                                }
                                /*
                                if(esplist.size() == 1) {
                                    mascvac.setMascota(esplist.get(0));
                                }
                                
                                
                                  if(vaclist.size() == 1) {
                                    mascvac.setVacuna(vaclist.get(0));
                                }
                                        */
                                mascvac.setFecha(rs.getDate(4));
                                mascvac.setHora(rs.getTime(4));
                                
                                /*
                                 if(esplist.size() == 1) {
                                    mascvac.setVeterinario(userlist.get(0));
                                }*/
                                
				allEvents.add(mascvac);
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
	
        public List<MascotaVacuna> findAll(String consulta) {
		List<MascotaVacuna> allEvents = new ArrayList<>();
		try {
			// get connection
		    Statement stmt = getStatement();
			ResultSet rs = stmt.executeQuery(consulta);

			// fetch all events from database
			MascotaVacuna mascvac;
			
			while (rs.next()) {
				mascvac = new MascotaVacuna();
				mascvac.setId (rs.getInt(1));
                                
                                MascotaDAO esp = new MascotaDAO();
                                List<Mascota> esplist = esp.findAll("select * from zk_mascota where id= " + rs.getInt(2));
                                if(esplist.size() == 1) {
                                    mascvac.setMascota(esplist.get(0));
                                }
                                
                                VacunaDAO vac = new VacunaDAO();
                                List<Vacuna> vaclist = vac.findAll("select * from zk_vacuna where id= " + rs.getInt(3));
                                if(vaclist.size() == 1) {
                                    mascvac.setVacuna(vaclist.get(0));
                                }
                                mascvac.setFecha(rs.getDate(4));
                                mascvac.setHora(rs.getTime(4));
                                
                                UserDAO user = new UserDAO();
                                List<User> userlist = user.findAll("select * from zk_usuario where id= " + rs.getInt(5));
                                if(esplist.size() == 1) {
                                    mascvac.setVeterinario(userlist.get(0));
                                }
                                
				allEvents.add(mascvac);
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
                
	public boolean delete(MascotaVacuna prod) {
		return execute("delete from zk_mascota_vacuna where id = '" + prod.getId() + "'");
	}
	
	public boolean insert(MascotaVacuna prod) {
                return execute("insert into zk_mascota_vacuna(mascota, vacuna, veterinario)" +
                               "values (" + prod.getMascota().getId() + ", " 
                                          + prod.getVacuna().getId()  +  ", "
                                          + prod.getVeterinario().getId() + ")");
            
	}
	
	public boolean update(MascotaVacuna prod) {
                return execute("UPDATE zk_mascota_vacuna SET  "
                                + "mascota = " + prod.getMascota().getId()     + ", " 
                                + "vacuna =  " + prod.getVacuna().getId()   + ", "
                                + "WHERE id = "  + prod.getId() );

        }
	
	
}