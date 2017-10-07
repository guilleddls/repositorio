package com.clinica.veterinaria.productos;

/**
 * Event DAO.
 * 
 * 
 */
import com.clinica.veterinaria.iva.Iva;
import com.clinica.veterinaria.iva.IvaDAO;
import com.clinica.veterinaria.producto_familia.ProductoFamilia;
import com.clinica.veterinaria.producto_familia.ProductoFamiliaDAO;
import com.clinica.veterinaria.proveedores.Proveedor;
import com.clinica.veterinaria.proveedores.ProveedorDAO;
import com.conexion.ManagerConexion;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAO extends ManagerConexion {	
	public Producto findByCodigo(String codigo){
            Producto producto = null;
            List<Producto> list = findAll("SELECT * FROM zk_producto WHERE codigo = '"+codigo+"'");
            if(!list.isEmpty()){
                producto = list.get(0);
            }
            return producto;
        }
        
        public Producto findOne(int id){
            Producto producto = null;
            List<Producto> list = findAll("SELECT * FROM zk_producto WHERE id = '"+id+"'");
            if(!list.isEmpty()){
                producto = list.get(0);
            }
            return producto;
        }
        
        public boolean existCodigo(String codigo){
            if(codigo == null) return false;
            return !findAll("SELECT * FROM zk_producto WHERE codigo = '"+codigo+"'").isEmpty();
        }
	public List<Producto> findAll() {
            return findAll("select * from zk_producto");
           
	}
        
        public List<Producto> findAll(String consulta) {
            List<Producto> allEvents = new ArrayList<>();
            try {
                    // get connection
                Statement stmt = getStatement();
                ResultSet rs = stmt.executeQuery(consulta);

                // fetch all events from database
                Producto prod;
                List<ProductoFamilia> famlist = new ProductoFamiliaDAO().findAll();
                List<Proveedor> provlist = new ProveedorDAO().findAll();
                List<Iva> ivalist = new IvaDAO().findAll();
                while (rs.next()) {
                    prod = new Producto();
                    prod.setId (rs.getInt(1));
                    for(ProductoFamilia p : famlist){
                        if(p.getId() == rs.getInt(2)){
                            prod.setFamilia(p);
                            break;
                        }
                    }
                    for(Proveedor p : provlist){
                        if(p.getId() == rs.getInt(3)){
                            prod.setProveedor(p);
                            break;
                        }
                    }

                    prod.setCodigo (rs.getString(4));
                    prod.setNombre (rs.getString(5));
                    prod.setPvp (rs.getFloat(6));

                    //prod.setIva (rs.getInt(7));

                    for(Iva i : ivalist){
                        if(i.getId() == rs.getInt(7)) {
                            prod.setIva(i);
                            break;
                        }
                    }

                    prod.setPrecio (rs.getFloat(8));
                    prod.setStock (rs.getInt(9));
                    prod.setDescripcion (rs.getString(10));
                    prod.setFotografia (rs.getString(11));
                    prod.setObservaciones (rs.getString(12));
                    prod.setFecha_alta (rs.getDate(14));
                    prod.setPrecioUnidad(rs.getFloat(15));
                    allEvents.add(prod);
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
	
	public boolean delete(Producto prod) {
		return execute("delete from zk_producto where id = " + prod.getId());
	}
	
	public boolean insert(Producto prod) {
            prod.setCodigo(crearCodigo(prod));
            String 
                    descripcion = sqlNullOrEmpty(prod.getDescripcion()),
                    foto = sqlNullOrEmpty(prod.getImagen()),
                    observacion = sqlNullOrEmpty(prod.getObservaciones());
            
            return execute("insert into zk_producto("
                                    + "id_familia, "
                                    + "id_proveedor, "
                                    + "codigo, "
                                    + "nombre, "
                                    + "pvp, "
                                    + "iva, "
                                    + "precio,"
                                    + "stock, "
                                    + "descripcion, "
                                    + "fotografia, "
                                    + "observaciones, "
                                    + "precioUnidad)" +
                                "values ('" 
                                        + prod.getFamilia().getId()     +   "','"
                                        + prod.getProveedor().getId()   +   "','"
                                        + prod.getCodigo()              +   "','" 
                                        + prod.getNombre()              +   "','" 
                                        + prod.getPvp()                 +   "','"
                                        + prod.getIva().getId()         +   "','"
                                        + prod.getPrecio()              +   "','"
                                        + prod.getStock()               +   "',"
                                        + descripcion                   +   ","
                                        + foto                          +   ","
                                        + observacion                   +   ","
                                        + prod.getPrecioUnidad()        +
                                    ")");
	}
	
	public boolean update(Producto prod) {
            prod.setCodigo(crearCodigo(prod));
            return execute("UPDATE zk_producto SET "
                            +   "id_familia = '"    + prod.getFamilia().getId()     + "', "
                            +   "id_proveedor = '"  + prod.getProveedor().getId()   + "',"
                            +   "codigo = '"        + prod.getCodigo()              + "', "
                            +   "nombre = '"        + prod.getNombre()              + "', " 
                            +   "pvp    = '"        + prod.getPvp()                 + "', "
                            +   "iva    = '"        + prod.getIva().getId()         + "', "
                            +   "precio = '"        + prod.getPrecio()              + "', "
                            +   "stock = '"         + prod.getStock()               + "', "
                            +   "descripcion = '"   + prod.getDescripcion()         + "', "
                            +   "fotografia = '"    + prod.getImagen()+ "', "
                            +   "observaciones = '" + prod.getObservaciones()       + "' " 
                            +   "precioUnidad = "   + prod.getPrecioUnidad() +
                            " WHERE id = "         + prod.getId()              
                    );
    }
	
                
        public List<Producto> findToProductos(int id){
            List<Producto> productos = new ArrayList<>();
            String consulta ="SELECT " +
                                "zk_producto.`id`, " +                                                   
                                "zk_producto.`codigo`, " +
                                "zk_producto.`nombre`, " +
                                "zk_producto.`precio` , " +                          
                                "zk_producto.`pvp`,  " +
                                "zk_producto.`stock`, " +
                                "zk_producto_familia.`id`, " +
                                "zk_producto_familia.`nombre`, " +
                                "zk_proveedor.`id` , " +
                                "zk_proveedor.`nombre`" +
                        "FROM `zk_producto` " +
                                "INNER JOIN `zk_producto_familia` ON zk_producto.`id_familia` = zk_producto_familia.`id` " +
                                "INNER JOIN `zk_proveedor` ON zk_producto.`id_proveedor` = zk_proveedor.`id` " +
                        (id > 0? " WHERE zk_proveedor.id = "+id:"") +" ORDER BY id_familia";          
            try {
                    // get connection
                Statement stmt = getStatement();
                ResultSet rs = stmt.executeQuery(consulta);
                

                Producto prod;
                Proveedor prov;
                
                ProductoFamilia familia;
                while (rs.next()) {
                    prod = new Producto();
                    prov = new Proveedor();
                    familia = new ProductoFamilia();
                    
                    prod.setId(rs.getInt(1));
                    prod.setCodigo(rs.getString(2));
                    prod.setNombre(rs.getString(3));
                    prod.setPrecio(rs.getFloat(4));
                    prod.setPvp(rs.getFloat(5));
                    prod.setStock(rs.getInt(6));
                    
                    familia.setId(rs.getInt(7));
                    familia.setNombre(rs.getString(8));                   
                    prov.setId(rs.getInt(9));
                    prov.setNombre(rs.getString(10));
                    
                    prod.setFamilia(familia);
                    prod.setProveedor(prov);
                    productos.add(prod);
                }
                closeResultSet(rs);
                        closeStatement(stmt);
            } catch (SQLException e) {
                    e.printStackTrace(System.err);
            } finally {
                close();
            }
           return productos;
            
        }
        
        
        private String sqlNullOrEmpty(String texto){
            if(texto == null) return "NULL";
            else if(texto.isEmpty()) return "NULL";
            else return "'"+texto+"'";
            
        }
        
    private boolean isNullorEmpty(String texto){
        if(texto == null) return true;
        else return texto.isEmpty();
    }
        
    public int getLastCodInt(String codigo){
        List<Producto> prodlist = findAll("SELECT * FROM zk_producto WHERE codigo LIKE '"+codigo+"%' ORDER BY id DESC");
        /*
        if (prodlist.isEmpty()){
            return 1;
        }
        else{
           String sub = prodlist.get(0).getCodigo().substring(8,codigo.length()); // CODIGO FFFNNPP001
           int num = Conversion.stringToInt(sub); // Convierte el 001 en numero, si hay excepcion devuelve 0
           return num + 1;
        }
                */
        return prodlist.size() + 1;
        
    }
    
    private String crearCodigo(Producto producto){
        String rta = producto.getCodigo();
        if(isNullorEmpty(rta))
        {
            // LE AGREGO VET PARA Q TENGA SI O SI MAS DE 3 CARACTERES
            String sbfamilia = (producto.getFamilia().getNombre()+"VET").substring(0, 3),
                   sbnombre = (producto.getNombre()+"VET").substring(0, 3),
                   sbproveedor = (producto.getProveedor().getNombre()+"VET").substring(0, 2);
            // ESTE ES EL CODIGO - PREFIJO
            String prefijo = (sbfamilia + sbnombre + sbproveedor).toLowerCase();

            // PREFIJO FFFNNNPP 
            // SUFIJO 001
            // CODIGO FFFNNPP001
            int num = getLastCodInt(prefijo);
            String sufijo = String.format("%03d", num);
               
            rta = prefijo + sufijo;
                //producto.setCodigo(codigo+fmt);
        }
        return rta;
    }
}