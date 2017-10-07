package com.clinica.veterinaria.mascotas;

/**
 * Event DAO.
 * 
 * 
 */

import com.clinica.veterinaria.clientes.Cliente;
import com.clinica.veterinaria.clientes.ClienteDAO;
import com.clinica.veterinaria.especies.Especie;
import com.clinica.veterinaria.especies.EspecieDAO;
import com.clinica.veterinaria.razas.Raza;
import com.clinica.veterinaria.razas.RazaDAO;
import com.clinica.veterinaria.util.Fecha;
import com.conexion.ManagerConexion;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


public class MascotaDAO extends ManagerConexion {	 
	
        public boolean existChip(String chip){
            return !findAll("select * from zk_mascota WHERE chip ='"+chip+"' ").isEmpty();
        }
        
        public List<Mascota> findAll() {
		return findAll("select * from zk_mascota");
           
	}
        
        public List<Mascota> findAll(String consulta) {
		
            List<Mascota> allEvents = new ArrayList<>();
		try {
                    // get connection
                    Statement stmt = getStatement();
                    ResultSet rs = stmt.executeQuery(consulta);

                    // fetch all events from database
                    Mascota masc;
                    List<Especie> esplist = new EspecieDAO().findAll();
                    List<Raza> razalist = new RazaDAO().findAll();
                    List<Cliente> clilist = new ClienteDAO().findAll();
                    while (rs.next()) {
                        masc = new Mascota();
                        masc.setId(rs.getInt(1));
                        masc.setChip(rs.getString(2));
                        masc.setNombre(rs.getString(3));
                        masc.setSexo(rs.getString(4));
                        masc.setNacimiento(rs.getDate(5));
                        masc.setDefuncion(rs.getDate(6));
                        masc.setPeso(rs.getFloat(7));
                        masc.setAltura(rs.getFloat(8));
                        masc.setObserv(rs.getString(9));
//                                masc.setEspecie(rs.getString(10));

                        for(Especie e : esplist){                               
                            if(e.getId() == rs.getInt(10) ) {
                                masc.setEspecie(e);
                                break;
                            }
                        }

                        masc.setPelo(rs.getString(11));

//                                masc.setRaza(rs.getString(12));
                        for(Raza r : razalist){                               
                            if(r.getId() == rs.getInt(12) ) {
                                masc.setRaza(r);
                                break;
                            }
                        }

                        masc.setFechaalta(rs.getDate(13));
                        masc.setFechabaja(rs.getDate(14));

                        for(Cliente c : clilist){                               
                            if(c.getId() == rs.getInt(15) ) {
                                masc.setCliente(c);
                                break;
                            }
                        }

                        masc.setFoto(rs.getString(16));
                        allEvents.add(masc);
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
        
        
	
	public boolean delete(Mascota masc) {
		return execute("delete from zk_mascota where id = '" + masc.getId() + "'");
	}
	
	public boolean insert(Mascota masc) {
            masc.setId(getLastID());
            masc.setChip(generarCodigo(masc));
            return execute("INSERT INTO  zk_mascota (id, chip, nombre, sexo, fecha_nac, peso, altura, observaciones, especie, pelo, raza, id_cliente, foto )" +
                           " VALUES ('" + masc.getId()  + "' ,"+
                                    "'" + masc.getChip()       + "','" 
                                        + masc.getNombre()      + "','"
                                        + masc.getSexo()        + "','"
                                        + new SimpleDateFormat("yyyy-MM-dd").format(masc.getNacimiento())  + "',"
                                        + masc.getPeso()        + ",'"
                                        + masc.getAltura()      + "','"
                                        + masc.getObserv()      + "','"
                                        + masc.getEspecie().getId()     + "','"
                                        + masc.getPelo()        + "',"
                                        + masc.getRaza().getId()       + ","
                                        + masc.getCliente().getId() + ", '"
                                        + masc.getImagen()+"')");

	}
	
	public boolean update(Mascota masc) {
            
            
            String
            
                defuncion = sqlFormatFecha(masc.getDefuncion()),
                nacimiento = sqlFormatFecha(masc.getNacimiento()),
                fbaja = sqlFormatFecha(masc.getFechabaja()),
                observ = sqlFormatString(masc.getObserv()),
                foto = sqlFormatString(masc.getImagen());
            
            masc.setChip(generarCodigo(masc));
            
            
            if(!"NULL".equals(defuncion) && "NULL".equals(fbaja))      fbaja = defuncion;
            
            
            if(!"NULL".equals(fbaja)){ // en el caso que se de de baja ...
                execute("DELETE FROM zk_cita WHERE id_mascota=" + masc.getId() + " AND CURDATE() < fecha");
            }
                
            String sql = ("UPDATE zk_mascota SET " + 
                            "chip = '"          + masc.getChip()        + "', " +
                            "nombre = '"        + masc.getNombre()      + "', " +
                            "sexo = '"          + masc.getSexo()        + "', " +
                            "fecha_nac = "      +  nacimiento + ", "  +
                            "fecha_def = "      + defuncion             + ", " +   
                            "peso = "          + masc.getPeso()        + ", " +
                            "altura = "        + masc.getAltura()      + ", " +
                            "observaciones = "  + observ                + ", " +
                            "especie = '"       + masc.getEspecie().getId()     + "', " +
                            "pelo = '"          + masc.getPelo()        + "', " +
                            "raza = '"          + masc.getRaza().getId()        + "', " +
                            "id_cliente= '"      + masc.getCliente().getId()     + "', " +
                            "fecha_baja = "     +  fbaja  + ", "+
                            "foto = " + foto +
                            //new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(masc.getFechaAlta()) + 
                            " WHERE id = " + masc.getId() 
                            );
            
          
            return execute(sql);
        }
    
        private String sqlFormatFecha(java.util.Date texto){
            if(texto == null) return "NULL";
            else return "'"+Fecha.dateFormato.format(texto)+"'";
            
        }
        private String sqlFormatString(String texto){
            if(texto == null || texto.isEmpty()) return "NULL";
            else return "'"+texto+"'";
            
        }

	/*
        * Generar Chip
        */
        private String generarCodigo(Mascota mascota){ 
            String codigo = mascota.getChip();
            if(codigo == null || codigo.isEmpty()){
                codigo = String.format("%06d",mascota.getId());
            }
            
            return codigo;
        }
        

    public List<Mascota> getClienteMascotas(Cliente cli) {
            List<Mascota> allMasc = new ArrayList<>();
            try {
                // get connection
                Statement stmt = getStatement();
                ResultSet rs = stmt.executeQuery("select * from zk_mascota where id_cliente=" + cli.getId());

                // fetch all events from database
                Mascota masc;

                while (rs.next()) {
                    masc = new Mascota();
                    masc.setId(rs.getInt(1));
                    masc.setChip(rs.getString(2));
                    masc.setNombre(rs.getString(3));
                    masc.setSexo(rs.getString(4));
                    masc.setNacimiento(rs.getDate(5));
                    masc.setDefuncion(rs.getDate(6));
                    masc.setPeso(rs.getFloat(7));
                    masc.setAltura(rs.getFloat(8));
                    masc.setObserv(rs.getString(9));
//                                masc.setEspecie(rs.getString(10));

                    EspecieDAO esp = new EspecieDAO();
                    List<Especie> esplist = esp.findAll("select * from zk_especie where id= " + rs.getInt(10));
                    if(esplist.size() == 1)
                        masc.setEspecie(esplist.get(0));

                    masc.setPelo(rs.getString(11));
//                                masc.setRaza(rs.getString(12));
                    RazaDAO raza = new RazaDAO();
                    List<Raza> razalist = raza.findAll("select * from zk_raza where id= " + rs.getInt(12));
                    if(razalist.size() == 1)
                        masc.setRaza(razalist.get(0));

                    masc.setFechaalta(rs.getDate(13));
                    masc.setFechabaja(rs.getDate(14));
                    allMasc.add(masc);
                }

                closeResultSet(rs);
                closeStatement(stmt);
            } catch (SQLException e) {
                    e.printStackTrace(System.err);
            } finally {
                close();
            }

            return allMasc;
        }

    private int getLastID() {
  
            int last_id = 0;
            try{
                Statement stmt = getStatement();
                ResultSet rs = stmt.executeQuery("SELECT IFNULL(MAX(id),0) FROM zk_mascota");
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
    
 
}
