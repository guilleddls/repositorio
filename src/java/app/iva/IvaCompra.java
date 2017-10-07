package app.iva;

import com.clinica.veterinaria.proveedores.Proveedor;

/**
 *
 * 
 */
public class IvaCompra extends Iva{
    private Proveedor proveedor;

    public IvaCompra() {
        super();
    }
    
    public Proveedor getProveedor() {
        return proveedor;
    }

    public void setProveedor(Proveedor proveedor) {
        this.proveedor = proveedor;
    }
    
    @Override
    public String getPersona(){
        return (proveedor != null)? getProveedor().getNombre(): "";
    }
    
    @Override
    public String getCuit(){
       return (proveedor != null)? getProveedor().getNif(): "";
    }
    
}
