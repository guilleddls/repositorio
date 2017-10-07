package com.clinica.veterinaria.pedido_linea;

import com.clinica.veterinaria.pedidos.Pedido;
import com.clinica.veterinaria.pedidos.PedidoDAO;
import com.clinica.veterinaria.productos.Producto;
import com.clinica.veterinaria.productos.ProductoDAO;
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
public class PedidoLineaDAO extends ManagerConexion{
    
   

    public PedidoLinea findOne(int id){
        List<PedidoLinea> lineas = findAll("select * from zk_pedido_linea WHERE id ='"+id+"'");
        return (!lineas.isEmpty())? lineas.get(0): null;
    }
    
    public List<PedidoLinea> findAll() {
        return findAll("select * from zk_pedido_linea");
        /*
        List<PedidoLinea> allEvents = new ArrayList<PedidoLinea>();
        try {
                // get connection
            Statement stmt = ds.getStatement();
                ResultSet rs = stmt.executeQuery("select * from zk_pedido_linea");

                // fetch all events from database
                PedidoLinea pedido;

                while (rs.next()) {
                        pedido = new PedidoLinea();
                        pedido.setId (rs.getInt(1));
                        pedido.setCantidad (rs.getInt(2));
                        pedido.setCoste(rs.getFloat(3));
                        //pedido.setId_pedido(rs.getDate(4));
                        PedidoDAO ped = new PedidoDAO();
                        List<Pedido> pedlist = ped.findAll("select * from zk_pedido where id= " + rs.getInt(4));
                        if(pedlist.size() == 1)
                            pedido.setPedido(pedlist.get(0));

                        //pedido.setId_producto(rs.getBoolean(5));
                        ProductoDAO prod = new ProductoDAO();
                        List<Producto> prodlist = prod.findAll("select * from zk_producto where id= " + rs.getInt(5));
                        if(prodlist.size() == 1)
                            pedido.setProducto(prodlist.get(0));

                        allEvents.add(pedido);
                }
        } catch (SQLException e) {
                e.printStackTrace(System.err);
        } finally {
            ds.close();
        }

        return allEvents;*/
    }
	
    public List<PedidoLinea> findAll(String consulta) {
        List<PedidoLinea> allEvents = new ArrayList<>();
        try {
                // get connection
            Statement stmt = getStatement();
            ResultSet rs = stmt.executeQuery(consulta);

            // fetch all events from database
            PedidoLinea pedido;
            
            List<Pedido> pedList = new PedidoDAO().findAllToLinea();
             List<Producto> prodList = new ProductoDAO().findAll();
            while (rs.next()) {
                pedido = new PedidoLinea();
                pedido.setId (rs.getInt(1));
                pedido.setCantidad (rs.getInt(2));
                pedido.setCoste(rs.getFloat(3));

                // Relacion PedidoLinea - Pedido "select * from zk_pedido where id= " + rs.getInt(4)
                
                
                for(Pedido x :pedList){
                    if(x.getId() == rs.getInt(4)){
                        pedido.setPedido(x);
                        break;
                    }
                }
                
                for(Producto x:prodList){
                    if(x.getId() == rs.getInt(5)){
                        pedido.setProducto(x);
                        break;
                    }
                }
                   

                // Relacon PedidoLinea - Producto "select * from zk_producto where id= " + rs.getInt(5)
               
               
               

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
                        
	public boolean delete(PedidoLinea prod) {
            
            boolean rta = execute("delete from zk_pedido_linea where id = '" + prod.getId() + "'");
            
            if(rta){
                    Producto p = prod.getProducto();
                    p.setStock(prod.getCantidad()-p.getStock());
                    new ProductoDAO().update(p);
                }
            
            return rta;
	}
	
	public boolean insert(PedidoLinea linea) {
		boolean rta = execute("insert into zk_pedido_linea(cantidad, coste, id_pedido, id_producto)" +
                                "values ('" + linea.getCantidad() + "', '" 
                                            + linea.getProducto().getPrecio() + "', '" 
                                            + linea.getPedido().getId() + "','" 
                                            + linea.getProducto().getId() +  "')");
                
                if(rta){
                    Producto p = linea.getProducto();
                    p.setStock(linea.getCantidad()+p.getStock());
                    new ProductoDAO().update(p);
                }
                return rta;
	}
	
	public boolean update(PedidoLinea linea) {
            PedidoLinea pedidoViejo = findOne(linea.getId());
            
            boolean rta = execute("UPDATE zk_pedido_linea SET " +
                            " cantidad = '"         + linea.getCantidad() + 
                            "', coste = '"          + linea.getProducto().getPrecio()  + 
                            "', id_pedido = '"      + linea.getPedido().getId() + 
                            "', id_producto = '"     + linea.getProducto().getId() +
                            "' WHERE id = '"         + linea.getId() + "'");
            
            if(rta && pedidoViejo!=null){
                    
//                    Producto p = linea.getProducto();
//                    
//                    int cantidad = (pedidoViejo.getCantidad() < linea.getCantidad())? linea.getCantidad():-1*linea.getCantidad();
//                    cantidad = (pedidoViejo.getCantidad() != linea.getCantidad())? cantidad : 0;
//                    
//                    p.setStock(linea.getProducto().getStock()+cantidad);
//                    new ProductoDAO().update(p);
                
                int cantidadNueva = linea.getCantidad();
                int cantidadVieja = pedidoViejo.getCantidad();
                
                int cantidad = cantidadVieja - cantidadNueva; 
                float stock = linea.getProducto().getStock() - cantidad; // Si la cantidad Nueva es mayor el stock aumenta sino disminuye
                
                //int cantidad = (lineaVieja.getCantidad() < vl.getCantidad())? vl.getCantidad():-1*vl.getCantidad();
                //cantidad = (lineaVieja.getCantidad() != vl.getCantidad())? cantidad : 0;
                Producto p = linea.getProducto();
                p.setStock(stock);
                new ProductoDAO().update(p);
                
                
                //p.setStock(stock);
                //execute("UPDATE zk_producto SET stock = " +stock+" WHERE id = '"+ linea.getProducto().getId()+"'");
                }
            return rta;
            
            
    }
	
	
}