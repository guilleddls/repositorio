package com.clinica.veterinaria.productos;

import com.clinica.veterinaria.items.Item;
import com.clinica.veterinaria.iva.Iva;
import com.clinica.veterinaria.producto_familia.ProductoFamilia;
import com.clinica.veterinaria.proveedores.Proveedor;
import java.util.Date;

/**
 * Producto.
 * 
 * 
 */

//import java.util.Date;

public class Producto extends Item{
    private float _pvp;
    private float _stock;
    private ProductoFamilia _familia;
    private Proveedor _proveedor;
    private String _fotografia;

    public Producto () {}
    
    public Producto(int _id, String _codigo, String _nombre, String _descripcion, 
                    float _precio, Iva _iva, String _observaciones, Date _fecha_alta,
                    float _pvp, int _stock, ProductoFamilia _familia, 
                    Proveedor _proveedor, String _fotografia ) {
        
        super(_id, _codigo, _nombre, _descripcion,  _precio, _iva, _observaciones, _fecha_alta);
        this._pvp = _pvp;
        this._stock = _stock;
        this._familia = _familia;
        this._proveedor = _proveedor;
        this._fotografia = _fotografia;
    }

    public void setPvp(float spvp){
        super.setPrecio(spvp);
        this._pvp = spvp;
    }
    public float getPvp() {
        return _pvp;
    }
    public void setStock(float stock){
        this._stock = stock;
    }
    public float getStock(){
        return _stock;
    }
    
    public ProductoFamilia getFamilia() {
        return _familia;
    }

    public void setFamilia(ProductoFamilia _familia) {
        this._familia = _familia;
    }


    public String getFotografia() {
        return getStringFoto(_fotografia);
    }
    
    
    private String getStringFoto(String texto){
        if(texto == null) return null;
        else if (texto.equalsIgnoreCase("null")) return null;
        else
            return "http://json-veterinaria.16mb.com/productos/uploads/"+texto;
    }

    public String getImagen(){
        return _fotografia;
    }
    
    public void setFotografia(String _fotografia) {
        this._fotografia = _fotografia;
    }

    public Proveedor getProveedor() {
        return _proveedor;
    }

    public void setProveedor(Proveedor _proveedor) {
        this._proveedor = _proveedor;
    }

}
