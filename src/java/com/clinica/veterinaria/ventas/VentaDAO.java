package com.clinica.veterinaria.ventas;

import com.clinica.veterinaria.clientes.Cliente;
import com.clinica.veterinaria.clientes.ClienteDAO;
import com.clinica.veterinaria.user.User;
import com.clinica.veterinaria.user.UserDAO;
import com.clinica.veterinaria.ventas_linea.VentaLinea;
import com.clinica.veterinaria.ventas_linea.VentaLineaDAO;
import com.conexion.ManagerConexion;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 *
 * 
 */
public class VentaDAO extends ManagerConexion {

    public Venta findOne(int id){
        List<Venta> lineas = findAll("SELECT * FROM zk_venta WHERE id = "+id);
        return (!lineas.isEmpty())? lineas.get(0): null;
        
    }
    
    public List<Venta> findAll() {
        return findAll("select * from zk_venta order by fecha desc");
    }
    
    public List<Venta> findByCliente(int id){
        return findAll("SELECT * FROM zk_venta WHERE id_cliente = "+id);
    }

    public List<Venta> findAll(String consulta) {
        List<Venta> allEvents = new ArrayList<>();
        try  {   
        Statement stmt = getStatement();
        ResultSet rs = stmt.executeQuery(consulta);

        Venta venta;
        //Factura factura;
        List<Cliente> clientes = new ClienteDAO().findAll();
        List<User> usuarios = new UserDAO().findAll();

        List<VentaLinea> lineasVta = new VentaLineaDAO().findAll();
        while (rs.next()) {
            venta = new Venta();
            venta.setId(rs.getInt(1));
            venta.setFecha(rs.getDate(2));
            venta.setHora(rs.getTime(2));

            //factura = new Factura(rs.getInt(7));
            //venta.setFactura(factura);

            for(int i=0; i<clientes.size(); i++){
                if(clientes.get(i).getId()==rs.getInt(3)){
                    venta.setCliente(clientes.get(i));
                    break;
                }
            }
            for(User x : usuarios){
                if(x.getId() == rs.getInt(4)){
                    venta.setVendedor(x);
                    break;
                }
            }
            /*       for(User x :userlist){       if(x.getId() == rs.getInt(5)){      venta.setVeterinario(x);      break;       }            }*/

            for(int i=0; i<lineasVta.size(); i++) {
                if(venta.getId() == lineasVta.get(i).getVenta().getId())
                    venta.addVentaLinea(lineasVta.get(i));
            }

            venta.setAlbaran(rs.getString(6));
            venta.setDeudor(rs.getInt(8) > 0);
            venta.setMonto_deudor(rs.getDouble(9));

            allEvents.add(venta);
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
    


    //String fecha_hora = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(venta.getFecha());
    //      String fecha_hora = new SimpleDateFormat("yyyy-MM-dd").format(it.getFecha()) + " " + new SimpleDateFormat("HH:mm:ss").format(it.getHora());
    
    public boolean insert(Venta v) {       
        String sql = "INSERT INTO zk_venta (id, fecha, id_cliente, id_vendedor, facturado, monto_deudor) VALUES (%1$d, '%2$tF %2$tT', %3$d, %4$d, %5$d, %6$.3f); ";
        Object[] args = {v.getId(), v.getFecha(), v.getCliente().getId(), v.getVendedor().getId(), v.isDeudor()? 1:0, v.getMonto_deudor() }; 
        
        System.out.println(String.format(Locale.ENGLISH, sql, args));
        return execute(String.format(Locale.ENGLISH, sql, args));      
    }

 
    public boolean update(Venta v) {
        String sql = "UPDATE zk_venta SET fecha = '%1$tF %1$tT', id_cliente = '%2$d', facturado = '%3$d', monto_deudor = %4$.3f  WHERE id = '%5$d' ;";
        Object[] args = {v.getFecha(), v.getCliente().getId(), v.isDeudor()?1:0, v.getMonto_deudor(), v.getId()};
        //System.out.println(String.format(Locale.ENGLISH, sql, args));
        return execute(String.format(Locale.ENGLISH, sql, args));
    }
    
    public boolean delete(Venta venta) {
        return execute("DELETE FROM zk_venta WHERE id = '" + venta.getId() + "'");
    }
    
    
        
    public int getNewID(){
        int lastID = 0;
        try{
            Statement stmt = getStatement();
            ResultSet rs = stmt.executeQuery("SELECT IFNULL(MAX(id),0) AS id FROM zk_venta");
            rs.next();
            lastID = rs.getInt(1);
            closeResultSet(rs);
            closeStatement(stmt);
        } catch (SQLException e) {
            e.printStackTrace(System.err);
        }
        finally{
            close();
        }
        return lastID + 1;
    }
        


	
        
        
        
        
        
        
        
    public boolean updateCliente(Venta it) {
        return execute("UPDATE zk_venta SET " +  "id_cliente = '" + it.getCliente().getId()   + "' " +   " WHERE id = '" + it.getId() + "'"   );
    }

    public boolean updateVeterinario(Venta it) {
        return execute("UPDATE zk_venta SET " + "id_veterinario = '" + it.getVeterinario().getId()   + "'  " +  " WHERE id = '" + it.getId() + "'"   );
    }

    public boolean updateFactura(Venta venta) {
        String facturaID = (venta.getFactura() != null)? "'" + venta.getFactura().getId() + "'":"0";
        int facturado = (venta.isDeudor())? 1:0 ;
        if(facturado == 0 )   facturaID = "0";
        return execute("UPDATE zk_venta SET " +   "factura   = "  + facturaID    + ",  " + "facturado = '" + facturado  + "' " +  " WHERE id = '" + venta.getId() + "'"   );
    }

    public boolean updateAlbaran(Venta it) {
        return execute("UPDATE zk_venta SET " + "albaran = '" + it.getAlbaran()  + "'  " + " WHERE id = '" + it.getId() + "'"   );
    }

	
        
    /**
     * Metodo para hacer el reporte de deudores cree una propiedad Deuda en Cliente 29/06/2015
     * @return
     */
    public List<Venta> findToDeudores(){
        List<Venta> ventas = new ArrayList<>();
        String consulta ="SELECT " +
            "SUM(zk_venta_linea.`pvp`*(zk_venta_linea.`cantidad`*(1+(zk_venta_linea.`iva`/100)))) AS suma, " +
            "zk_venta.`id` AS id_venta, " +
            "zk_venta.`fecha` AS zk_venta_fecha, " +
            "zk_venta.`id_cliente`, " +
            "CONCAT(zk_cliente.`nombre`,\" \", zk_cliente.`apellidos` ) AS cliente " +
            "FROM `zk_venta` zk_venta " +
            "INNER JOIN `zk_venta_linea` zk_venta_linea ON zk_venta.`id` = zk_venta_linea.`id_venta` " +
            "INNER JOIN `zk_cliente` zk_cliente ON zk_venta.`id_cliente` = zk_cliente.`id` " +
            "WHERE " +
            "zk_venta.`id` NOT IN ( " +
            "SELECT zk_venta.`id` AS zk_venta_id " +
            "FROM `zk_factura` zk_factura INNER JOIN `zk_venta` zk_venta ON zk_factura.`id` = zk_venta.`factura` " +
            ") AND id_cliente <> 1 " +
            "GROUP BY  id_cliente " +
            "HAVING suma > 0";
        try {
                // get connection
                Statement stmt = getStatement();
                ResultSet rs = stmt.executeQuery(consulta);

                // fetch all events from database
                Cliente cli;
                Venta venta;
                while (rs.next()) {
                    cli = new Cliente();
                    venta = new Venta();
                    cli.setDeuda(rs.getDouble(1));
                    venta.setId(rs.getInt(2));
                    venta.setFecha(rs.getDate(3));
                    cli.setId(4);
                    cli.setNombre(rs.getString(5));
                    venta.setCliente(cli);
                    
                    ventas.add(venta);
                }
                closeResultSet(rs);
                closeStatement(stmt);
        } catch (SQLException e) {
                e.printStackTrace(System.err);
        } finally {
            close();
        }
       
            return ventas;
    }
}
