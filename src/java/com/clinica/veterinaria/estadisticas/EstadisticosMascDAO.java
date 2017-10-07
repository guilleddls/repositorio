
package com.clinica.veterinaria.estadisticas;

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
public class EstadisticosMascDAO  extends ManagerConexion{
            
//	private final DataSourceMascota ds = new DataSourceMascota();
        //private final DataSourceMascotaVacuna dsmv = new DataSourceMascotaVacuna();
       // private final DataSourceCita dsc = new DataSourceCita();
       // private final DataSourceVentaLinea dsvl = new DataSourceVentaLinea();
       // private final DataSourcePedidoLinea dspl = new DataSourcePedidoLinea();
	
	public List<EstadisticosMasc> findAll() {
		List<EstadisticosMasc> allEvents = new ArrayList<>();
		try {
			// get connection
		    Statement stmt = getStatement();
			ResultSet rs = stmt.executeQuery("select * from zk_mascota");

			// fetch all events from database
			EstadisticosMasc estad;
			
			while (rs.next()) {
				estad = new EstadisticosMasc();
                              
				allEvents.add(estad);
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
	
	public List<EstadisticosMasc> findAll(String consulta) {
		List<EstadisticosMasc> allEvents = new ArrayList<>();
		try {
			// get connection
		    Statement stmt = getStatement();
			ResultSet rs = stmt.executeQuery(consulta);

			// fetch all events from database
			EstadisticosMasc estad;
			
			while (rs.next()) {
				estad = new EstadisticosMasc();
                                
                                estad.setTipo(rs.getString(1));
				estad.setEnero (rs.getInt(2));
                                estad.setFebrero (rs.getInt(3));
                                estad.setMarzo (rs.getInt(4));
                                estad.setAbril (rs.getInt(5));
                                estad.setMayo (rs.getInt(6));
                                estad.setJunio(rs.getInt(7));
                                estad.setJulio (rs.getInt(8));
                                estad.setAgosto (rs.getInt(9));
                                estad.setSeptiembre (rs.getInt(10));
                                estad.setOctubre (rs.getInt(11));
                                estad.setNoviembre (rs.getInt(12));
                                estad.setDiciembre (rs.getInt(13));
                                
				allEvents.add(estad);
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
        
        public List<EstadisticosMonetario> findAllMonetario(String consulta) {
		List<EstadisticosMonetario> allEvents = new ArrayList<>();
		try {
			// get connection
		    Statement stmt = getStatement();
			ResultSet rs = stmt.executeQuery(consulta);

			// fetch all events from database
			EstadisticosMonetario estad;
			
			while (rs.next()) {
				estad = new EstadisticosMonetario();
                                
                                estad.setTipo(rs.getString(1));
				estad.setEnero (rs.getFloat(2));
                                estad.setFebrero (rs.getFloat(3));
                                estad.setMarzo (rs.getFloat(4));
                                estad.setAbril (rs.getFloat(5));
                                estad.setMayo (rs.getFloat(6));
                                estad.setJunio(rs.getFloat(7));
                                estad.setJulio (rs.getFloat(8));
                                estad.setAgosto (rs.getFloat(9));
                                estad.setSeptiembre (rs.getFloat(10));
                                estad.setOctubre (rs.getFloat(11));
                                estad.setNoviembre (rs.getFloat(12));
                                estad.setDiciembre (rs.getFloat(13));
                                
				allEvents.add(estad);
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

	
}