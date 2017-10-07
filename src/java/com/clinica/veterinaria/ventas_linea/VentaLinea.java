package com.clinica.veterinaria.ventas_linea;

import com.clinica.veterinaria.items.Item;
import com.clinica.veterinaria.productos.Producto;
import com.clinica.veterinaria.servicios.Servicio;
import com.clinica.veterinaria.ventas.Venta;
import java.sql.Time;
import java.util.Date;

/**
 *
 * 
 */
public class VentaLinea {
    
    private int _id;
    private Venta _venta;
    
    private float _cantidad;
    private Date _fecha;
    private Time _hora;
    private Producto _producto;
    private Servicio _servicio;
    private int _tipo; //1)Producto 2)Servicio
    private float _pvp;
    
    //private float _importe_total; --> Se calcula como la cantidad por el precio unitario
    private float _iva;

    public VentaLinea () {
    }

    public int getId() {
        return _id;
    }

    public void setId(int _id) {
        this._id = _id;
    }
    
    public Date getFecha() {
        return _fecha;
    }

    public void setFecha(Date _fecha) {
        this._fecha = _fecha;
    }

    public Time getHora() {
        return _hora;
    }

    public void setHora(Time _hora) {
        this._hora = _hora;
    }
    
    public float getCantidad() {
        return _cantidad;
    }

    public void setCantidad(float _cantidad) {
        this._cantidad = _cantidad;
    }

    public Venta getVenta() {
        return _venta;
    }

    public void setVenta(Venta _venta) {
        this._venta = _venta;
    }

    /*
    * No me sirve por ahora con iva
    */
    public float getPreciototal(){
        return  (float) (this.getPvp() * this._cantidad * (1+(this.getIva() /100))) ;
    }
    
    /*
    * Este es el nuevo Total
    */
    public float getMonto(){
        return (float) (this.getPvp() * this._cantidad) ;
    }

    public int getTipo() {
        return _tipo;
    }

    public void setTipo(int _tipo) {
        this._tipo = _tipo;
    }
    
    public Producto getProducto() {
        return _producto;
    }

    public void setProducto(Producto _producto) {
        this._producto = _producto;
    } 
      
    public Servicio getServicio() {
        return _servicio;
    }

    public void setServicio(Servicio _servicio) {
        this._servicio = _servicio;
    }

    
    
    /*
    * Metodos que saca del Producto/Servicio
    */
    public Item getItem(){
        return _tipo == 1? _producto : _servicio;
    }
    
    public float getPrecio(){
        return getItem().getPrecio();
    }
    
    public String getNombre(){
        return getItem().getNombre();
    }
    
    public String getDetalle(){
        return getItem().getNombre() + ((_tipo == 2)?"(Servicio)":""); 
    }
    
    
    public String getCodigo(){
        return getItem().getCodigo();
    }

    public String getDescripcion(){
        return getItem().getDescripcion() == null? "": getItem().getDescripcion();
    }
    // Fin de los metodos del Producto/Servicio
    
    
    public void setPvp(float _pvp) {
        this._pvp = _pvp;
    }
    
    public float getPvp(){
        return _pvp;
    }

    /*
    * Por ahora no me va a servir el iva
    */
    public float getIva() {
        return _iva; // == 0? getItem().getIva().getValor(): _iva; 
    }

    public void setIva(float _iva) {
        this._iva = _iva;
    }

    
    // Si contiene el item dentro de la linea
    public boolean equalsItem(VentaLinea linea){
        return  linea.getTipo() == this.getTipo() && 
                linea.getItem().getId() == this.getItem().getId();
    }
  
  
    
    
}
