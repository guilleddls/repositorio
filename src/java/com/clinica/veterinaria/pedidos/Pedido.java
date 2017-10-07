package com.clinica.veterinaria.pedidos;

import com.clinica.veterinaria.pedido_linea.PedidoLinea;
import com.clinica.veterinaria.proveedores.Proveedor;
import com.clinica.veterinaria.user.User;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import org.zkoss.zk.ui.Executions;

/**
 *
 * 
 */
public class Pedido {
    
    private int _id;
    private Date _fecha;
    private Date _fecha_entrega;
    private Date _fecha_pago;
    private boolean _pagado;
    private Proveedor _proveedor;
    private HashSet <PedidoLinea> _pedidos = new HashSet<>();
    private User _empleado;
    
    public Pedido () {}

    public Pedido(int _id, Date _fecha, Date _fecha_entrega, Date _fecha_pago, 
                  boolean _pagado, Proveedor _proveedor, HashSet <PedidoLinea> _pedidos, User _empleado  ) {
        this._id = _id;
        this._fecha = _fecha;
        this._fecha_entrega = _fecha_entrega;
        this._fecha_pago = _fecha_pago;
        this._pagado = _pagado;
        this._proveedor = _proveedor;
        this._pedidos = _pedidos;
        this._empleado = _empleado;
    }
    
    public int getId() {
        return _id;
    }

    public void setId(int _id) {
        this._id = _id;
    }
    
    public Date getFecha_entrega() {
        return _fecha_entrega;
    }

    public void setFecha_entrega(Date _fecha_entrega) {
        this._fecha_entrega = _fecha_entrega;
    }

    public Date getFecha_pago() {
        return _fecha_pago;
    }

    public void setFecha_pago(Date _fecha_pago) {
        this._fecha_pago = _fecha_pago;
    }
    
    public Date getFecha() {
        return _fecha;
    }

    public void setFecha(Date _fecha) {
        this._fecha = _fecha;
    }

    public boolean isPagado() {
        return _pagado;
    }

    public void setPagado(boolean _pagado) {
        this._pagado = _pagado;
    }

    public Proveedor getProveedor() {
        return _proveedor;
    }

    public void setProveedor(Proveedor _proveedor) {
        this._proveedor = _proveedor;
    }

    public User getEmpleado() {
        return _empleado;
    }

    public void setEmpleado(User _empleado) {
        this._empleado = _empleado;
    }

    public HashSet<PedidoLinea> getPedidos() {
        return _pedidos;
    }

    public void setPedidos(HashSet<PedidoLinea> _pedidos) {
        this._pedidos = _pedidos;
    }
    
    
    public boolean agregarLinea(PedidoLinea pedidolinea){
        boolean existe = false;     
        for(PedidoLinea l :_pedidos){
            if(l.getProducto().getId() == pedidolinea.getProducto().getId()){

                existe= true;
                break;
            }
        }
        if(!existe) _pedidos.add(pedidolinea);
        
        return !existe; //Se envia si se agregó
    }
    
    public boolean removerLinea(PedidoLinea pedidolinea){
        boolean existe = false;
        PedidoLinea pedAux = null;
       
        for(PedidoLinea l :_pedidos){
            if(l.getProducto().getId() == pedidolinea.getProducto().getId()){
                pedAux = l;
                existe= true;
                break;
            }
        }
        if(pedAux != null) _pedidos.remove(pedAux);
        return existe;
    }
    
    public boolean asignarPedidoLinea(PedidoLinea pedidolinea) {
        
        boolean retorno= !_pedidos.contains(pedidolinea);
        if(retorno)_pedidos.add(pedidolinea);
        return retorno;
    }
    
    public void eliminarPedidoLinea(PedidoLinea pedidolinea) {
        _pedidos.remove(pedidolinea);
    }
    
    public float getCoste() {
        float coste_total = 0;
        Iterator <PedidoLinea> it = _pedidos.iterator();
        while(it.hasNext()){
            PedidoLinea ln = it.next();
            coste_total += ln.getCostetotal();
        }
        return coste_total;
    }
    
    public int getNum_articulos(){
        int num = 0;
        Iterator <PedidoLinea> it = _pedidos.iterator();
        while(it.hasNext()){
            PedidoLinea ln = it.next();
            num +=  ln.getCantidad();
        }
        return num;
    }
    
}
