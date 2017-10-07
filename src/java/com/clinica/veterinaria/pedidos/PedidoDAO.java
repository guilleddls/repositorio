package com.clinica.veterinaria.pedidos;

import com.clinica.veterinaria.pedido_linea.PedidoLinea;
import com.clinica.veterinaria.pedido_linea.PedidoLineaDAO;
import com.clinica.veterinaria.proveedores.Proveedor;
import com.clinica.veterinaria.proveedores.ProveedorDAO;
import com.clinica.veterinaria.user.User;
import com.clinica.veterinaria.user.UserDAO;
import com.conexion.ManagerConexion;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * 
 */
public class PedidoDAO extends ManagerConexion{

    

    
    public List<Pedido> findAllToLinea(){
        List<Pedido> allEvents = new ArrayList<>();
            try {
                    // get connection
                Statement stmt = getStatement();
                ResultSet rs = stmt.executeQuery("select * from zk_pedido ORDER BY id DESC");

                // fetch all events from database
                Pedido pedido;
                List<Proveedor> provList = new ProveedorDAO().findAll();



                while (rs.next()) {
                    pedido = new Pedido();
                    pedido.setId (rs.getInt(1));
                    pedido.setFecha (rs.getTimestamp(2));
                    pedido.setFecha_entrega(rs.getDate(3));
                    pedido.setFecha_pago(rs.getDate(4));
                    pedido.setPagado(rs.getBoolean(5));


                    for(Proveedor x :provList){
                        if(x.getId() == rs.getInt(6)){
                            pedido.setProveedor(x);
                            break;
                        }
                    }

                    /*
                    if(provlist.size() == 1) {
                        pedido.setProveedor(provlist.get(0));
                    }


                    for(int i=0; i<lnlist.size(); i++) {
                        pedido.asignarPedidoLinea(lnlist.get(i));
                    }


                     if(userlist.size() == 1) {
                        pedido.setEmpleado(userlist.get(0));
                    }
                    */
                        allEvents.add(pedido);
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
    public List<Pedido> findAll() {
        return findAll("select * from zk_pedido ORDER BY id DESC");
        
    }
    
    public List<Pedido> findAll(String consulta) {
        List<Pedido> allEvents = new ArrayList<>();
        try {
                // get connection
            Statement stmt = getStatement();
            ResultSet rs = stmt.executeQuery(consulta);

            // fetch all events from database
            Pedido pedido;
            List<Proveedor> provList = new ProveedorDAO().findAll();
            List<PedidoLinea> lpList = new PedidoLineaDAO().findAll();
            List<User> userList = new UserDAO().findAll();


            while (rs.next()) {
                pedido = new Pedido();
                pedido.setId (rs.getInt(1));
                pedido.setFecha (rs.getTimestamp(2));
                pedido.setFecha_entrega(rs.getDate(3));
                pedido.setFecha_pago(rs.getDate(4));
                pedido.setPagado(rs.getBoolean(5));


                for(Proveedor x :provList){
                    if(x.getId() == rs.getInt(6)){
                        pedido.setProveedor(x);
                        break;
                    }
                }
                for(PedidoLinea x : lpList){
                    if(x.getPedido().getId() == pedido.getId())
                        pedido.asignarPedidoLinea(x);
                }

                for(User x : userList){
                    if(x.getId() == rs.getInt(7)){
                        pedido.setEmpleado(x);
                        break;
                    }
                }
                /*
                if(provlist.size() == 1) {
                    pedido.setProveedor(provlist.get(0));
                }


                for(int i=0; i<lnlist.size(); i++) {
                    pedido.asignarPedidoLinea(lnlist.get(i));
                }


                 if(userlist.size() == 1) {
                    pedido.setEmpleado(userlist.get(0));
                }
                */
                    allEvents.add(pedido);
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

    public boolean delete(Pedido pedido) {
        boolean rta = execute("delete from zk_pedido where id = '" + pedido.getId() + "'");
        if(rta){
            execute("UPDATE zk_producto "
                    + "INNER JOIN zk_pedido_linea ON zk_producto.id = zk_pedido_linea.id_producto "
                    + "SET stock = stock - zk_pedido_linea.cantidad "
                    + "WHERE zk_pedido_linea.id_pedido = '" + pedido.getId() + "'");
            
            execute("DELETE FROM zk_pedido_linea WHERE id_pedido = '" + pedido.getId() + "'");
            }
        
        return  rta;
    }

    public int insert(Pedido prod) {
        String fecha_hora = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(prod.getFecha());
        //int pagado;
        int last_id = 0;
        //boolean insertado = false;
        int pagado = prod.isPagado()? 1: 0;
        
        String fecha_entrega = (prod.getFecha_entrega() == null)? "NULL" : "'"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(prod.getFecha_entrega())+"'";
                
        String fecha_pago = (prod.getFecha_pago() == null)? "NULL": "'"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(prod.getFecha_pago())+"'";
        /*
        if(prod.getFecha_entrega() == null && prod.getFecha_pago() == null) {
            if(execute("insert into zk_pedido(fecha, pagado, id_proveedor, id_empleado)" +
                           "values ('" + fecha_hora + "','"
                                       + pagado + "','"
                                       + prod.getProveedor().getId() +  "','"
                                       + prod.getEmpleado().getId()  + "')")){
                insertado = true;
            }
        }
        
        else if (prod.getFecha_entrega() != null && prod.getFecha_pago() != null) {*/
             execute("insert into zk_pedido(fecha, fecha_entrega, fecha_pago, pagado, id_proveedor, id_empleado)" +
                           "values ('"
                            + fecha_hora + "',"
                            + fecha_entrega   + ","
                            + fecha_pago     + ",'"
                            + pagado + "','"
                            + prod.getProveedor().getId() +  "','"
                            + prod.getEmpleado().getId()  +  "')");
            
       /* }

        else if(prod.getFecha_pago() == null && prod.getFecha_entrega() != null) {
            if(execute("insert into zk_pedido(fecha, fecha_entrega, pagado, id_proveedor, id_empleado)" +
                       "values ('" /*+ new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(prod.getFecha()) + "', '" 
                           + fecha_hora + "','"
                           + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(prod.getFecha_pago()) + "','"
                           + pagado + "','"
                           + prod.getProveedor().getId() +  "','"
                           + prod.getEmpleado().getId()  +  "')"))
            
        }

        else {
            if(execute("insert into zk_pedido(fecha, fecha_pago, pagado, id_proveedor, id_empleado)" +
                       "values ('" /*+ new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(prod.getFecha()) + "', '" 
                           + fecha_hora + "','"
                           + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(prod.getFecha_pago()) + "','"
                           + pagado + "','"
                           + prod.getProveedor().getId() +  "','"
                           + prod.getEmpleado().getId()  +  "')"))
            
        }
        */
        try{
                Statement stmt = getStatement();
                ResultSet rs = stmt.executeQuery("select id from zk_pedido WHERE fecha='" + fecha_hora +"'");
                rs.next();
                last_id = rs.getInt(1);
//                System.out.println("Id: "+  last_id + ", Fecha: " + fecha_hora);
                closeResultSet(rs);
                closeStatement(stmt);
            }
            catch (SQLException e){
                e.printStackTrace(System.err);
            }
            finally {
                close();
            }
        return last_id;
    }

    public boolean update(Pedido prod) {
        int pagado = prod.isPagado()? 1: 0;
        
        String fecha_entrega = (prod.getFecha_entrega() == null)? "NULL" : "'"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(prod.getFecha_entrega())+"'";
                
        String fecha_pago = (prod.getFecha_pago() == null)? "NULL": "'"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(prod.getFecha_pago())+"'";
        /*
        if(prod.getFecha_entrega() == null && prod.getFecha_pago() == null) {
            return execute("UPDATE zk_pedido SET "  +
                        "   fecha_entrega = NULL "  +
                        " , fecha_pago = NULL "     +
                        " , pagado = '"         + pagado  +
                        "', id_proveedor = '"   + prod.getProveedor().getId() +
                        "' WHERE id = '"        + prod.getId() + "'");
        }

        else if(prod.getFecha_pago() == null && prod.getFecha_entrega() != null) {
            return execute("UPDATE zk_pedido SET "  +
                        "   fecha_entrega = '"  + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(prod.getFecha_entrega()) +
                        "', fecha_pago = NULL " +
                        " , pagado = '"         + pagado  +
                        "', id_proveedor = '"   + prod.getProveedor().getId() +
                        "' WHERE id = '"        + prod.getId() + "'");
        }

        else if(prod.getFecha_pago() != null && prod.getFecha_entrega() == null) {
            return execute("UPDATE zk_pedido SET "  +
                        " fecha_entrega = NULL "  + 
                        ", fecha_pago= '"      + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(prod.getFecha_pago()) +
                        "', pagado = '"       + pagado  +
                        "', id_proveedor = '" + prod.getProveedor().getId() +
                        "' WHERE id = '"      + prod.getId() + "'");
        }

        else {*/
            return execute("UPDATE zk_pedido SET "  +
                        "   fecha_entrega = "  + fecha_entrega +//+ new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(prod.getFecha_entrega()) +
                        ", fecha_pago= "      + fecha_pago + //new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(prod.getFecha_pago()) +
                        ", pagado = '"         + pagado  +
                        "', id_proveedor = '"   + prod.getProveedor().getId() +
                        "' WHERE id = '"        + prod.getId() + "'");
        

    }

   
}