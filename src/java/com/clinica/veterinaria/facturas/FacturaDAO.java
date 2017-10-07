
package com.clinica.veterinaria.facturas;

import com.clinica.veterinaria.clientes.Cliente;
import com.clinica.veterinaria.clientes.ClienteDAO;
import com.clinica.veterinaria.user.User;
import com.clinica.veterinaria.user.UserDAO;
import com.clinica.veterinaria.ventas.Venta;
import com.clinica.veterinaria.ventas.VentaDAO;
import com.conexion.ManagerConexion;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 */
public class FacturaDAO extends ManagerConexion{
        
	
	
    public List<Factura> findAll() {
        List<Factura> allEvents = new ArrayList<>();
        try {
                // get connection
            Statement stmt = getStatement();
            ResultSet rs = stmt.executeQuery("select * from zk_factura");

            // fetch all events from database
            Factura factura;
            List<Cliente> clilist = new ClienteDAO().findAll();
            List<User> userlist = new UserDAO().findAll();
            List<Venta> lnlist = new VentaDAO().findAll();
            while (rs.next()) {

                factura = new Factura();
                factura.setId (rs.getInt(1));
                factura.setNumero (rs.getInt(2));

                for(Cliente x: clilist){
                    if(x.getId() == rs.getInt(3)){
                        factura.setCliente(x);
                        break;
                    }
                }
                for(User x: userlist){
                    if(x.getId()==rs.getInt(4)){
                        factura.setEmpleado(x);
                        break;
                    }
                }



                factura.setRuta(rs.getString(5));
                factura.setFecha(rs.getDate(6));



                for(int i=0; i<lnlist.size(); i++) {
                    if(lnlist.get(i).getFactura().getId() == factura.getId())
                    factura.asignarVenta(lnlist.get(i));
                }

                allEvents.add(factura);
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

    public List<Factura> findAll(String consulta) {
        List<Factura> allEvents = new ArrayList<>();
        try {
                // get connection
            Statement stmt = getStatement();
            ResultSet rs = stmt.executeQuery(consulta);

            // fetch all events from database
            Factura factura;

            while (rs.next()) {
                boolean erroneo = false;
                factura = new Factura();
                factura.setId (rs.getInt(1));
                factura.setNumero (rs.getInt(2));

                ClienteDAO cli = new ClienteDAO();
                List<Cliente> clilist = cli.findAll("select * from zk_cliente where id=" + rs.getInt(3));
                if(clilist.size() == 1) {
                    factura.setCliente(clilist.get(0));
                }else{
                    erroneo = true;
                }

                UserDAO user = new UserDAO();
                List<User> userlist = user.findAll("select * from zk_usuario where id=" + rs.getInt(4));
                if(userlist.size() == 1) {
                    factura.setEmpleado(userlist.get(0));
                }else{
                    erroneo = true;
                }

                factura.setRuta(rs.getString(5));
                factura.setFecha(rs.getDate(6));

                allEvents.add(factura);
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

    public boolean delete(Factura fact) {
        
        boolean rta = execute("DELETE FROM zk_factura WHERE id = '" + fact.getId() + "'");
        if(rta){
            execute("UPDATE zk_venta SET factura = 0, facturado = '0' WHERE factura = '"+fact.getId()+"'" );
        }
        return rta;
    }

    public Map insert(Factura prod) {
        int last_id = 0, last_num=0;
        Map map = new HashMap<>();
        String fecha_hora = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(prod.getFecha());

        try {
                Statement stmt = getStatement();
                ResultSet rs = stmt.executeQuery("SELECT numero FROM zk_factura ORDER BY numero DESC LIMIT 1");
                rs.next();
                last_num = rs.getInt(1) + 1;
                closeResultSet(rs);
                    closeStatement(stmt);
//                System.out.println("Id: "+  last_id + ", Fecha: " + fecha_hora);
            } catch (SQLException e) {
                e.printStackTrace(System.err);
            } finally {
                close();
            }

        boolean insertado = execute("insert into zk_factura(numero, cliente, empleado, factura, fecha)" +
                       "values ('" 
                        + last_num   + "', '" 
                        + prod.getCliente().getId()  + "', '" 
                        + prod.getEmpleado().getId() + "', '" 
                        + prod.getRuta()     + "', '"
                        + fecha_hora         + "')");
        if(insertado){
            try {
                Statement stmt = getStatement();
                ResultSet rs = stmt.executeQuery("SELECT id FROM zk_factura WHERE cliente='"+ prod.getCliente().getId() + "' AND fecha='" + fecha_hora + "'");
                rs.next();
                last_id = rs.getInt(1);
                closeResultSet(rs);
                    closeStatement(stmt);
//                System.out.println("Id: "+  last_id + ", Fecha: " + fecha_hora);
            } catch (SQLException e) {
                e.printStackTrace(System.err);
            } finally {
                close();
            }

        }
        map.put("last_id", last_id);
        map.put("last_num", last_num);
        return map;
    }

    public boolean update(Factura prod) {
            return execute("UPDATE zk_factura SET  " 
                           + "numero = '"   + prod.getNumero()      + "', "
                           + "cliente = '"  + prod.getCliente().getId()     + "', "
                           + "empleado = '" + prod.getEmpleado().getId()    + "', "
                           + "factura = '"  + prod.getRuta()        + "' "
                           + "WHERE id = '" + prod.getId() + "'");
    }

    public boolean updatenumero(Factura fact){
        return execute("UPDATE zk_factura SET  " 
                        + "numero =   '" + fact.getNumero()      + "', "
                        + "empleado = '" + fact.getEmpleado().getId()    + "' "
                        + "WHERE id = '" + fact.getId() + "'");
    }

    public boolean updatefecha(Factura fact){
        String fecha_hora = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(fact.getFecha());
        return execute("UPDATE zk_factura SET  " 
                        + "fecha =   '" + fecha_hora      + "', "
                        + "empleado = '" + fact.getEmpleado().getId()    + "' "
                        + "WHERE id = '" + fact.getId() + "'");
    }

    public boolean updatePDF(Factura fact){
        return execute("UPDATE zk_factura SET  " 
                        + "factura = '" + fact.getRuta()    + "' "
                        + "WHERE id = '" + fact.getId() + "'");
    }

	
}