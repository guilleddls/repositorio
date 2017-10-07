package com.clinica.veterinaria.historial;

import com.clinica.veterinaria.ficheros.Fichero;
import com.clinica.veterinaria.ficheros.FicheroDAO;
import com.clinica.veterinaria.mascotas.Mascota;
import com.clinica.veterinaria.mascotas.MascotaDAO;
import com.clinica.veterinaria.pesos.Peso;
import com.clinica.veterinaria.pesos.PesoDAO;
import com.clinica.veterinaria.user.User;
import com.clinica.veterinaria.user.UserDAO;
import com.conexion.ManagerConexion;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class HistorialDAO extends ManagerConexion{
    
    public List<Historial> findAll() {
        List<Historial> allEvents = new ArrayList<>();
        try {
            // get connection
            Statement stmt = getStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM zk_historial");

            // fetch all events from database
            Historial histo;
            List<User> userlist =  new UserDAO().findAll();
            List<Mascota> masclist = new MascotaDAO().findAll();
            List<Peso> pesolist = new PesoDAO().findAll();
            while (rs.next()) {
                histo = new Historial();
                histo.setId(rs.getInt(1));
                for(Mascota m: masclist){
                    if(m.getId() == rs.getInt(2)){
                        histo.setMascota(m);
                        break;
                    }
                }                             

                for(User x :userlist){
                    if(x.getId() == rs.getInt(4)){
                        histo.setId_veterinario(x);
                        break;
                    }
                }

                histo.setFecha(rs.getDate(4));
                histo.setHora(rs.getTime(4));
                histo.setTipo(rs.getInt(5));



                for(Peso p : pesolist){
                    if(p.getId()== rs.getInt(6)){
                        histo.setPeso(p);
                        break;
                    }
                }

                histo.setAnamnesis(rs.getString(7));
                histo.setDiagnostico(rs.getString(8));
                histo.setTratamiento(rs.getString(9));


                allEvents.add(histo);
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
        
        public List<Historial> findAll(String consulta) {
            List<Historial> allEvents = new ArrayList<>();
            try {
                // get connection
                Statement stmt = getStatement();
                ResultSet rs = stmt.executeQuery(consulta);

                // fetch all events from database
                Historial histo;
                List<User> userlist =  new UserDAO().findAll();
                List<Mascota> masclist = new MascotaDAO().findAll();
                List<Peso> pesolist = new PesoDAO().findAll();
                List<Fichero> filelist = new FicheroDAO().findAll("SELECT * FROM zk_ficheros WHERE tipo = 1");
                    
                while (rs.next()) {
                    histo = new Historial();
                    histo.setId(rs.getInt(1));
                    for(Mascota m: masclist){
                        if(m.getId() == rs.getInt(2)){
                            histo.setMascota(m);
                            break;
                        }
                    }                             

                    for(User x :userlist){
                        if(x.getId() == rs.getInt(4)){
                            histo.setId_veterinario(x);
                            break;
                        }
                    }

                    histo.setFecha(rs.getDate(4));
                    histo.setHora(rs.getTime(4));
                    histo.setTipo(rs.getInt(5));
                    for(Peso p : pesolist){
                        if(p.getId()== rs.getInt(6)){
                            histo.setPeso(p);
                            break;
                        }
                    }

                    histo.setAnamnesis(rs.getString(7));
                    histo.setDiagnostico(rs.getString(8));
                    histo.setTratamiento(rs.getString(9));

                    
                    for(Fichero archivo : filelist) {
                        if(archivo.getId_externo() == histo.getId()){
                            histo.addFichero(archivo);
                            break;
                        }
                    }
                    allEvents.add(histo);
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
        
	public boolean delete(Historial masc) {
		return execute("delete from zk_historial where id = '" + masc.getId() + "'");
	}
	
	public int insert(Historial cli) {
            int last_id = -1;
            String anamnesis, diagnostico, tratamiento;
            String fecha_hora = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(cli.getFecha());
            
            if(cli.getAnamnesis() == null || "".equals(cli.getAnamnesis())){
                anamnesis = "NULL";
            }
            else{
                anamnesis = "'" + cli.getAnamnesis() + "'";
            }
            
            if(cli.getDiagnostico() == null || "".equals(cli.getDiagnostico())){
                diagnostico = "NULL";
            }
            else{
                diagnostico = "'" + cli.getDiagnostico() + "'";
            }
            
            if(cli.getTratamiento() == null || "".equals(cli.getTratamiento())){
                tratamiento = "NULL";
            }
            else{
                tratamiento = "'" + cli.getTratamiento() + "'";
            }
            
            cli.getPeso().setFecha(cli.getFecha());
            
            if(new PesoDAO().insert(cli.getPeso()))
            {
                List<Peso> pesolist = new PesoDAO().findAll("select * from zk_peso where fecha='" + fecha_hora + "' AND mascota="+cli.getMascota().getId());
                if(pesolist.size() == 1) {
                    cli.setPeso(pesolist.get(0));
                }
            }
            if( execute("insert into zk_historial(id_mascota, id_veterinario, fecha, tipo_visita, id_peso, " +
                           "anamnesis, diagnostico, tratamiento) " +
                           "values (" 
                            + cli.getMascota().getId() + ", " 
                            + cli.getId_veterinario().getId() + ", '"
                            + fecha_hora    + "', "
                            + cli.getTipo() + ", " 
                            + cli.getPeso().getId() + ", "
                            + anamnesis + ", " 
                            + diagnostico +  ", " 
                            + tratamiento + ")"))
            {
                try{
                    Statement stmt = getStatement();
                    ResultSet rs = stmt.executeQuery("select id from zk_historial where "
                            + "id_mascota="+cli.getMascota().getId() 
                            + " AND "
                            + "fecha='" + fecha_hora + "'");
                    rs.next();
                    last_id = rs.getInt(1);
                    closeResultSet(rs);
                    closeStatement(stmt);
    //                System.out.println("Id: "+  last_id + ", Fecha: " + fecha_hora);
                    }
                
                    catch (SQLException e){
                        e.printStackTrace(System.err);
                    }
                    finally {
                        close();
                    }
            }
                
            return last_id;
	}
	
	public boolean update(Historial cli) {
           
            
            int tipo_visita = (cli.getTipo() == 0)? 1: cli.getTipo();
            
            
            return execute("UPDATE zk_historial SET " +
                    "tipo_visita = '"   + tipo_visita         + "', " +
                    "anamnesis = '"     + cli.getAnamnesis()    + "', " +
                    "diagnostico = '"   + cli.getDiagnostico()  + "', " +
                    "tratamiento = '"   + cli.getTratamiento()  + "' " +
                    //new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(cli.getFechaAlta()) + 
                    " WHERE id = '" + cli.getId() + "'"
                    );
        }

	 
}
