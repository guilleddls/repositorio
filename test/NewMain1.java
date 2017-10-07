
import com.clinica.veterinaria.productos.Producto;
import com.clinica.veterinaria.productos.ProductoDAO;
import com.clinica.veterinaria.util.Conversion;
import java.util.List;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * 
 */
public class NewMain1 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
//        List<Producto> list = new ProductoDAO().findAll();
//        
//        for(Producto p : list){
//            System.out.println(p.getFotografia());
//        }
    //    System.out.println("123456789".substring(5, 8)); // 678
        
        List<Producto> prodlist = new ProductoDAO().findAll("SELECT * FROM zk_producto WHERE codigo LIKE '%' ORDER BY id DESC");
        Producto producto = prodlist.get(4);
        System.out.println(producto.getCodigo());
        System.out.println(producto.getCodigo().length());
        String sub = (producto.getCodigo()+"AAAA").substring(8, 14);
        
        String texto = null;
        if(texto != null && !texto.isEmpty())  System.out.println(sub);
        int num = Conversion.stringToInt(sub);
        System.out.println(String.format("%03d", num));
    }
    
}
