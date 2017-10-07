package com.clinica.veterinaria.clientes;

import com.clinica.veterinaria.mascotas.Mascota;
import com.clinica.veterinaria.mascotas.MascotaDAO;
import com.clinica.veterinaria.poblaciones.Poblacion;
import com.clinica.veterinaria.poblaciones.PoblacionDAO;
import com.clinica.veterinaria.provincias.Provincia;
import com.clinica.veterinaria.provincias.ProvinciaDAO;
import com.conexion.ManagerConexion;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class ClienteDAO extends ManagerConexion{	
	
	

    public boolean existeUsuario(String usuario){
        return !findAll("SELECT * FROM zk_cliente WHERE usuario = '"+usuario+"'").isEmpty();
    }

    public boolean existeEmail(String email){
        return !findAll("SELECT * FROM zk_cliente WHERE email = '"+email+"'").isEmpty();
    }

    
    
    public Cliente findOne(int id){
        List<Cliente> lineas = findAll("SELECT * FROM zk_cliente WHERE id = "+id);
        return (!lineas.isEmpty())? lineas.get(0): null;
        
    }
    
    
    public List<Cliente> findAll() {
        List<Cliente> allEvents = new ArrayList<>();
        try {
                // get connection
            Statement stmt = getStatement();
            ResultSet rs = stmt.executeQuery("select * from zk_cliente");

            // fetch all events from database
            Cliente cli;
            List<Poblacion> poblist = new PoblacionDAO().findAll();
            List<Provincia> prolist = new ProvinciaDAO().findAll();
            while (rs.next()) {
                cli = new Cliente();
                cli.setId(rs.getInt(1));
                cli.setNombre(rs.getString(2));
                cli.setApellidos(rs.getString(3));
                cli.setNif(rs.getString(4));
                cli.setDireccion(rs.getString(5));

                for(Poblacion p : poblist){
                    if(p.getId() == rs.getInt(6)){
                        cli.setCiudad(p);
                        break;
                    }

                }
                for(Provincia p : prolist){
                    if(p.getId() == rs.getInt(7)){
                        cli.setProvincia(p);
                        break;
                    }

                }

                cli.setTelefono(rs.getInt(8));
                cli.setTelefono2(rs.getInt(9));
                cli.setEmail(rs.getString(10));
                cli.setFechaalta(rs.getDate(11));
                cli.setCodigopostal(rs.getInt(12));

                cli.setUsuario(rs.getString(13));
                cli.setPassword(rs.getString(14));

                allEvents.add(cli);

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

    public List<Cliente> findAll(String consulta) {
        List<Cliente> allEvents = new ArrayList<>();
        try {
                // get connection
            Statement stmt = getStatement();
            ResultSet rs = stmt.executeQuery(consulta);

            // fetch all events from database
            Cliente cli;
            List<Poblacion> poblist = new PoblacionDAO().findAll();
            List<Provincia> prolist = new ProvinciaDAO().findAll();
            while (rs.next()) {
                cli = new Cliente();
                cli.setId(rs.getInt(1));
                cli.setNombre(rs.getString(2));
                cli.setApellidos(rs.getString(3));
                cli.setNif(rs.getString(4));
                cli.setDireccion(rs.getString(5));


                for(Poblacion p : poblist){
                    if(p.getId() == rs.getInt(6)){
                        cli.setCiudad(p);
                        break;
                    }

                }
                for(Provincia p : prolist){
                    if(p.getId() == rs.getInt(7)){
                        cli.setProvincia(p);
                        break;
                    }

                }
                cli.setTelefono(rs.getInt(8));
                cli.setTelefono2(rs.getInt(9));
                cli.setEmail(rs.getString(10));
                cli.setFechaalta(rs.getDate(11));
                cli.setCodigopostal(rs.getInt(12));
                cli.setUsuario(rs.getString(13));
                cli.setPassword(rs.getString(14));
                allEvents.add(cli);

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

    //Dado un cliente, se saca sus mascotas
    public List<Mascota> getClienteMascotas(Cliente cli) {
        return new MascotaDAO().findAll("select * from zk_mascota where id_cliente=" + cli.getId());

    }

    public boolean delete(Cliente cli) {
            return execute("delete from zk_cliente where id = " + cli.getId());
    }

    public Cliente insertRapido(Cliente cli){
        cli.setId(getNewID());
        cli.setNif(getCuitRapido() );
        execute("INSERT INTO zk_cliente( id, nombre, apellidos, cuit, direccion, ciudad, provincia, telefono, telefono2, email, codigopostal) VALUES ("
                + cli.getId() + ",'" 
                + cli.getNombre() + "','" 
                + cli.getApellidos() + "','" 
                + cli.getNif() + "','" 
                + "S/D" + "','" 
                + 1 + "','" 
                + 6 +  "','" 
                + 0 + "'," 
                + 0 +  "," 
                + "" + ","
                + 0 + ")");
        
        return cli;
    }
    
    public boolean insert(Cliente cli) {
        String email = (cli.getEmail() == null || "".equals(cli.getEmail()))? "NULL" : "'" + cli.getEmail() + "'";
        String telefono2 = (cli.getTelefono2()  == 0)? "NULL" : "'" + cli.getTelefono2() + "'";
        String codigopostal = (cli.getCodigopostal()  == 0)? "NULL" : ""+cli.getCodigopostal();


        System.out.println("Nombre: "+ cli.getNombre() +"\n"+
        "Apellidos: " + cli.getApellidos()+"\n"+
        "CUIT: " + cli.getNif()+"\n"+
        "Direccion: "+ cli.getDireccion()+"\n"+
        "Ciudad: " + cli.getCiudad().getId() +"\n"+
        "Provincia: " + cli.getProvincia().getId()+"\n"+
        "Telefono: " +cli.getTelefono()+"\n"+
        "Telefono2: " + telefono2 +"\n"+
        "Email: " + email );

        return execute("insert into zk_cliente(nombre, apellidos, cuit, direccion, ciudad, provincia, telefono, telefono2, email, codigopostal) " +
            "values ('" 
                + cli.getNombre() + "','" 
                + cli.getApellidos() + "','" 
                + cli.getNif() + "','" 
                + cli.getDireccion() + "','" 
                + cli.getCiudad().getId() + "','" 
                + cli.getProvincia().getId() +  "','" 
                + cli.getTelefono() + "'," 
                + telefono2 +  "," 
                + email + ","
                + codigopostal + ")");
    }

    public boolean update(Cliente cli) {
        String email = (cli.getEmail() == null || "".equals(cli.getEmail()))? "NULL" : "'" + cli.getEmail() + "'";
        String telefono2 = (cli.getTelefono2()  == 0)? "NULL" : "'" + cli.getTelefono2() + "'";
        String codigopostal = (cli.getCodigopostal()  == 0)? "NULL" : ""+cli.getCodigopostal();

        System.out.println("UPDATE zk_cliente SET " +
                "nombre = '"    + cli.getNombre() + "', " +
                "apellidos = '" + cli.getApellidos() + "', " + 
                "cuit = '"       + cli.getNif() +  "'," +
                "direccion = '" + cli.getDireccion()    + "' , " +
                "ciudad = '"    + cli.getCiudad().getId()       + "' , " +
                "provincia = '" + cli.getProvincia().getId()    + "' , " +
                "telefono = '"  + cli.getTelefono()     + "' , " +
                "telefono2 = " + telefono2    + " , " +
                "email = "     + email        + " ,  " +
                "codigopostal = "+ codigopostal+ 
                " WHERE id = " + cli.getId() );

        return execute(
                "UPDATE zk_cliente SET " +
                "nombre = '"    + cli.getNombre() + "', " +
                "apellidos = '" + cli.getApellidos() + "', " + 
                "cuit = '"       + cli.getNif() +  "'," +
                "direccion = '" + cli.getDireccion()    + "' , " +
                "ciudad = '"    + cli.getCiudad().getId()       + "' , " +
                "provincia = '" + cli.getProvincia().getId()    + "' , " +
                "telefono = '"  + cli.getTelefono()     + "' , " +
                "telefono2 = " + telefono2    + " , " +
                "email = "     + email        + " , " +
                "codigopostal = "     + codigopostal        + 
                " WHERE id = '" + cli.getId() +"'"
                );
    }

        
        
    public boolean updateMovil(Cliente cliente){
        
        return execute("UPDATE zk_cliente SET usuario = '"+cliente.getUsuario()+"', password = '" +cliente.getPassword()+"', email = '"+cliente.getEmail()+"' WHERE id = " + cliente.getId());
    }
        
    public boolean updateWeb(String id, String pass){
        
        return execute("UPDATE zk_cliente SET password = '" +pass+"' WHERE id = "+ id);
    }
    
    
    public List<Cliente> findDeudores() {
        List<Cliente> allEvents = new ArrayList<>();
        try {
                // get connection
            Statement stmt = getStatement();
            ResultSet rs = stmt.executeQuery("SELECT zk_cliente.* , SUM(monto_deudor), COUNT(id_cliente) FROM zk_cliente INNER JOIN zk_venta ON zk_venta.id_cliente = zk_cliente.id WHERE monto_deudor > 0 GROUP BY zk_cliente.id");

            // fetch all events from database
            Cliente cli;
            List<Poblacion> poblist = new PoblacionDAO().findAll();
            List<Provincia> prolist = new ProvinciaDAO().findAll();
            while (rs.next()) {
                cli = new Cliente();
                cli.setId(rs.getInt(1));
                cli.setNombre(rs.getString(2));
                cli.setApellidos(rs.getString(3));
                cli.setNif(rs.getString(4));
                cli.setDireccion(rs.getString(5));


                for(Poblacion p : poblist){
                    if(p.getId() == rs.getInt(6)){
                        cli.setCiudad(p);
                        break;
                    }

                }
                for(Provincia p : prolist){
                    if(p.getId() == rs.getInt(7)){
                        cli.setProvincia(p);
                        break;
                    }

                }
                cli.setTelefono(rs.getInt(8));
                cli.setTelefono2(rs.getInt(9));
                cli.setEmail(rs.getString(10));
                cli.setFechaalta(rs.getDate(11));
                cli.setCodigopostal(rs.getInt(12));
                cli.setUsuario(rs.getString(13));
                cli.setPassword(rs.getString(14));
                cli.setSaldoDeudor(rs.getFloat(15));
                cli.setBoletasImpagas(rs.getInt(16));
                allEvents.add(cli);

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
    
    public String getCuitRapido(){
        String lastID = "";
        try{
            Statement stmt = getStatement();
            ResultSet rs = stmt.executeQuery("SELECT IFNULL(MAX(cuit),0) FROM zk_cliente");
            rs.next();
            lastID = rs.getString(1);
            closeResultSet(rs);
            closeStatement(stmt);
        } catch (SQLException e) {
            e.printStackTrace(System.err);
        }
        //String rta = "99-11132791-3";
        if(lastID.substring(0, 2).equals("99")){ // if(esto = 99 -> crea 99-00000001-9)
            int asd = (Integer.parseInt(lastID.substring(3, 11))) +1; //.replace("-","")
            lastID = ("99-"+asd+"-9");
        }
        else{
            lastID = "99-00000001-9";
        }
        return lastID;
    }
    
    public int getNewID(){
        int lastID = 0;
        try{
            Statement stmt = getStatement();
            ResultSet rs = stmt.executeQuery("SELECT IFNULL(MAX(id),0) AS id FROM zk_cliente");
            rs.next();
            lastID = rs.getInt(1);
            closeResultSet(rs);
            closeStatement(stmt);
        } catch (SQLException e) {
            e.printStackTrace(System.err);
        }
        return lastID + 1;
    }
}
