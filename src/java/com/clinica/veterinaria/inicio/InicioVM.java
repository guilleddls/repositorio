package com.clinica.veterinaria.inicio;

import com.clinica.veterinaria.productos.Producto;
import com.clinica.veterinaria.productos.ProductoDAO;

import java.util.List;
import org.zkoss.bind.annotation.Init;

/**
 *
 * 
 */
public class InicioVM {
    
    private List<Producto> prodList;
    
    @Init
    public void init () {
  
        prodList = new ProductoDAO().findAll("SELECT * FROM zk_producto WHERE stock < 3");
        
    }
    public List<Producto> getAvisoProducto(){
        return new ProductoDAO().findAll("SELECT * FROM zk_producto WHERE stock < 3");
    }
    
    /**
     *
     * @param parametro Executions.getCurrent().getParameter("ref");
     * @return ruta del include para el contenido
     */
    public static String fillPrincipal(String parametro){
         
        // parametro = Executions.getCurrent().getParameter("ref");
        switch (parametro) {
            case "clientes":                
                return "clientes/cliente-template.zul";               
            case "citas":               
                return "citas/cita-template.zul";              
            case "mascotas":                
                return "mascotas/mascota-template.zul";             
            case "calendario":               
                return "calendario/calendario.zul";           
            case "ventas":                
                return "ventas/venta-template.zul";             
            case "facturas":                
                return "facturas/factura-template.zul";                   
            case "proveedores": 
                return "proveedores/proveedor-template.zul";     
            case "pedidos": 
                return "pedidos/pedido-template.zul";
            case "productos":       
                return "productos/producto-template.zul";              
            case "servicios":                
                return "servicios/servicio-lista.zul";               
            case "chart-mascotas":                
                return "estadisticas/estadistica-mascota.zul";                
            case "chart-movimientos":                
                return "estadisticas/estadistica-monetario.zul";                
            default: return "page.zul"; 
            
        }
       
    }
    
    
    public static boolean[] getMarcador(String bookmark){
        return new boolean[] { false,
            bookmark.equals(""), 
            bookmark.equals("clientes") , 
            bookmark.equals("citas") , 
            bookmark.equals("mascotas"), 
            bookmark.equals("calendario"), 
            bookmark.equals("productos"), 
            bookmark.equals("servicios"), 
            bookmark.equals("ventas"), 
            bookmark.equals("facturas") , 
            bookmark.equals("proveedores"), 
            bookmark.equals("pedidos"), 
            bookmark.equals("chart-mascotas"), 
            bookmark.equals("chart-movimientos")
        };

    }
}
