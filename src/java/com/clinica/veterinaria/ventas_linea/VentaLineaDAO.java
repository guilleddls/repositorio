package com.clinica.veterinaria.ventas_linea;

import com.clinica.veterinaria.productos.Producto;
import com.clinica.veterinaria.productos.ProductoDAO;
import com.clinica.veterinaria.servicios.Servicio;
import com.clinica.veterinaria.servicios.ServicioDAO;
import com.clinica.veterinaria.ventas.Venta;
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
public class VentaLineaDAO extends ManagerConexion {
    private final static String TABLA ="zk_venta_linea";
    private final static String SELECT = "SELECT * FROM " + TABLA;

    
   
    public VentaLinea findOne(int id){
        List<VentaLinea> lineas = findAll(VentaLineaDAO.SELECT +" WHERE id = "+id);
        return (!lineas.isEmpty())? lineas.get(0): null;       
    }
    
    public List<VentaLinea> findAll() {
        return findAll(VentaLineaDAO.SELECT);
    }
    
    public List<VentaLinea> findByVenta(Venta venta){
        return findAll(VentaLineaDAO.SELECT+" WHERE id_venta =" + venta.getId());
    }
    
    public List<VentaLinea> findAll(String consulta) {
        List<VentaLinea> allEvents = new ArrayList<>();
        try {
            Statement stmt = getStatement();
            ResultSet rs = stmt.executeQuery(consulta);
            VentaLinea lineaVenta;
            List<Producto> prodlist = new ProductoDAO().findAll();
            List<Servicio> servlist = new ServicioDAO().findAll();

            while (rs.next()) {
                lineaVenta = new VentaLinea();
                lineaVenta.setId (rs.getInt(1));
                lineaVenta.setVenta(new Venta(rs.getInt(2)));
                lineaVenta.setTipo (rs.getInt(6));

                if(lineaVenta.getTipo() == 1){
                    for(Producto x : prodlist){
                        if(x.getId() == rs.getInt(3)){
                            lineaVenta.setProducto(x);
                            break;
                        }
                    }
                }
                else if(lineaVenta.getTipo() == 2){
                    for(Servicio x : servlist){
                        if(x.getId() == rs.getInt(3)){
                            lineaVenta.setServicio(x);
                            break;
                        }
                    }                           
                }
                lineaVenta.setCantidad (rs.getInt(4));
                lineaVenta.setFecha(rs.getDate(5));
                lineaVenta.setHora(rs.getTime(5));
                lineaVenta.setPvp(rs.getFloat(7));
                lineaVenta.setIva(rs.getInt(8));


                allEvents.add(lineaVenta);
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
    
    public int find_idVenta(VentaLinea ventalinea) {
        int id = 0;
        try {
            Statement stmt = getStatement();
            ResultSet rs = stmt.executeQuery("SELECT id_venta FROM "+ TABLA +" WHERE id = " + ventalinea.getId());
            rs.next();
            id = rs.getInt(1);
            closeResultSet(rs);
            closeStatement(stmt);
                    
        } catch (SQLException e) {
            e.printStackTrace(System.err);
        } finally {
            close();
        }

        return id;
    }
    
    

    public boolean insert(List<VentaLinea> lineas){
        //StringBuilder sql = new StringBuilder();
        int id = lineas.get(0).getVenta().getId();
        for(VentaLinea vl : lineas ){
            execute(stringSqlInsert(vl));
        }
        //execute(sql.toString());
        //sql.append(stringUpdateStock("-", id));
        //System.out.println(sql.toString());
        String rta = stringUpdateStock("-", id);
        return execute(rta);
    }
    
    public boolean update(List<VentaLinea> lineas){
        List<VentaLinea> viejas = findByVenta(lineas.get(0).getVenta());       
        sqlUpdate(viejas, lineas); // Borra actualiza y agrega      
        return true;
    }
    
    public boolean delete(List<VentaLinea> lineas){
        //StringBuilder sql = new StringBuilder();
        int id = lineas.get(0).getVenta().getId();
        execute("DELETE FROM " + TABLA + " WHERE id_venta = "+ id+" ; ");
        for(VentaLinea vl : lineas ){
            execute(sqlStringDelete(vl));
        }
        //execute(sql.toString());
       
        String rta = stringUpdateStock("+", id);
        return execute(rta);
    }

    
    private String stringSqlInsert(VentaLinea vl){
        String update = "INSERT INTO " + TABLA + " (cantidad, id_venta, id_producto, tipo, pvp, iva) VALUES (%1$.3f, %2$d, %3$d, '%4$d', '%5$.3f', '%6$.3f'); ";
        Object[] args = {vl.getCantidad(),vl.getVenta().getId(),vl.getItem().getId(),vl.getTipo()  ,vl.getPvp(), vl.getIva()};  
        
        return String.format(Locale.ENGLISH,update, args); // stringUpdateStock(vl, 0) +
    }
    
    
    private String sqlStringDelete(VentaLinea vtaLinea){
        return vtaLinea.getTipo() == 1 ? "UPDATE zk_producto SET stock = stock + " + vtaLinea.getCantidad() + " WHERE id = '" + vtaLinea.getProducto().getId() + "'; ":"";
               // + "DELETE FROM "+ TABLA +" WHERE id = '"+vtaLinea.getId()+"'";   
        //return sql;
    }
    
    private String stringSqlUpdate(VentaLinea vl){
        String update = "UPDATE " + TABLA + " SET cantidad = %1$.3f, id_producto = %2$d, pvp = %3$.3f, iva = %4$.3f WHERE id = %5$d AND tipo = %6$d ; ";
        Object[] args = {vl.getCantidad(),vl.getItem().getId(), vl.getPvp(), vl.getIva(), vl.getId(), vl.getTipo()};
        if(vl.getTipo()==1){
            execute(stringUpdateStock(vl, findOne(vl.getId()).getCantidad()));
        }
        return String.format(Locale.ENGLISH,update, args);
    }
    
    
    
    
    
    private String stringUpdateStock(VentaLinea vl, float cantidadAnterior){
        String stock = vl.getTipo() == 1? "UPDATE zk_producto SET stock = stock + %1$.3f WHERE id = %2$d ; " : "";
        Object[] args1 = {(cantidadAnterior - vl.getCantidad()), vl.getProducto().getId() };   
        return String.format(Locale.ENGLISH,stock, args1);
    }
    
    private String stringUpdateStock(String signo, int id){
       return "UPDATE zk_producto "
                + "INNER JOIN zk_venta_linea ON zk_producto.id = zk_venta_linea.id_producto "
                + "SET stock = stock "+ signo +" zk_venta_linea.cantidad "
                + "WHERE zk_venta_linea.id_venta = '"+id+"' ; ";        
    }
    
    /*
        Lineas Nueva: [   2,    4,    7]
        Lineas Viejas:[1, 2, 3, 4, 5]
                      -1 '2 -3 '4 -5+ +7
    */
    public void sqlUpdate(List<VentaLinea> ListaVieja, List<VentaLinea> ListaNueva){       
        //StringBuilder sql = new StringBuilder();
        for(VentaLinea i : ListaNueva){
            if(!contiene(ListaVieja, i)){//!ListaVieja.contains(i)){ //DELETE   System.out.print("-"+ i ); 
                delete(i);
                
            }
            else if(contiene(ListaVieja, i)){//ListaVieja.contains(i)){ // UPDATE System.out.print("'"+ i );
                execute(stringSqlUpdate(i) );
            }
        }
        for(VentaLinea i : ListaVieja){
            if(!contiene(ListaNueva, i)){//!ListaNueva.contains(i)){ // INSERT  System.out.print("+"+ i );
                execute(stringSqlInsert(i));
                String rta = stringUpdateStock("-", i.getVenta().getId());
                execute(rta);
            }
        }
        
        //return sql.toString();
    }
    
    private boolean contiene(List<VentaLinea> Lista, VentaLinea linea){
        boolean rta = false;
        for(VentaLinea l : Lista){
            if(l.getId() == linea.getId()){
                rta = true;
                break;
            }
        }
        return rta;
    }
    
      
    public boolean insert(VentaLinea vtaLinea) {
        //if esproducto && (vtaLinea.getProducto().getStock() >= vtaLinea.getCantidad()) ) return false; Para no dejar el stock en negativo
        //esproducto?  "UPDATE zk_producto SET stock = " + (vtaLinea.getProducto().getStock() - vtaLinea.getCantidad())+ " WHERE id = '" + vtaLinea.getProducto().getId() + "'; ":"").append(
        //if(vtaLinea.getTipo() == 1)  actualizarStock(vtaLinea, 0);
        
        return execute(stringSqlInsert(vtaLinea));

    }

    public boolean delete(VentaLinea vl) {
        boolean esproducto = vl.getTipo() == 1;
           
        String sql = new StringBuffer( 
                esproducto? "UPDATE zk_producto SET"+
                " stock = stock + " + vl.getCantidad()+ 
                " WHERE id = '" + vl.getProducto().getId() + "'; ":"").append(
                "DELETE FROM zk_venta_linea WHERE id = '").append(vl.getId() ).append( "'").toString();
        return execute(sql);
    }
    
    
    
    public boolean update(VentaLinea vl) {
        boolean 
        insertado = execute(stringSqlUpdate(vl));

           /* if(insertado && vl.getTipo() == 1){
                VentaLinea lineaVieja = findOne(vl.getId());
                actualizarStock(vl, lineaVieja);
                //execute("UPDATE zk_producto SET stock = " +stock+" WHERE id = '"+ vl.getProducto().getId()+"'");
            }*/

        return insertado;
    }
    
    private void actualizarStock(VentaLinea lineaNueva, float cantidadV){
        float cantidadNueva = lineaNueva.getCantidad();
        float cantidadVieja = cantidadV; //lineaVieja.getCantidad();

        float cantidad = cantidadVieja - cantidadNueva; 
        float stock = lineaNueva.getProducto().getStock() + cantidad; //Si la cantidad Nueva es mayor entonces se reduce el stock

        //int cantidad = (lineaVieja.getCantidad() < vl.getCantidad())? vl.getCantidad():-1*vl.getCantidad();
        //cantidad = (lineaVieja.getCantidad() != vl.getCantidad())? cantidad : 0;
        Producto p = lineaNueva.getProducto();
        p.setStock(stock);
        new ProductoDAO().update(p);
    }
    
    private void actualizarStock(VentaLinea lineaNueva, VentaLinea lineaVieja){
        if(lineaVieja == null) return;
        actualizarStock(lineaNueva, lineaVieja.getCantidad());
        
    }
    
    
    
    /* StringBuilder sql = new StringBuilder();
        sql.append(vl.getTipo() == 1?  new StringBuffer("UPDATE zk_producto SET stock = ").append(vl.getProducto().getStock() + (findOne(vl.getId()).getCantidad() - vl.getCantidad()))
                                                .append( " WHERE id = '" ) .append( vl.getProducto().getId() ).append( "'; ").toString():"")
        .append("UPDATE zk_venta_linea SET " ).append(
                            " cantidad = "     ).append( vl.getCantidad() ).append( 
                            ", id_producto = " ).append( vl.getItem().getId() ).append(
                            ", pvp = "         ).append( vl.getPvp() ).append(
                            ", iva = "        ).append( vl.getIva() ).append(
                            " WHERE id = '"    ).append( vl.getId() ).append(  "' AND tipo = " ).append(vl.getTipo()).append("; ");
        */
    
        /* StringBuilder sql = new StringBuilder();     
        sql.append(vl.getTipo() == 1?  new StringBuffer("UPDATE zk_producto SET stock = ").append((vl.getProducto().getStock() - vl.getCantidad()))
                                                .append( " WHERE id = '" ) .append(vl.getProducto().getId() ).append( "'; ").toString():"")

        .append("INSERT INTO zk_venta_linea(cantidad, id_venta, id_producto, tipo, pvp, iva) VALUES ('" )
                .append(vl.getCantidad()          )
                .append("','").append(vl.getVenta().getId()     )
                .append("','").append(vl.getItem().getId()      )
                .append( "',").append(vl.getTipo()              )
                .append( ", ").append(vl.getItem().getPrecio()  )
                .append( ", ").append(vl.getIva()               ).append( "); ");*/
    
}