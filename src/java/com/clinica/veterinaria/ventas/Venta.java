package com.clinica.veterinaria.ventas;

import com.clinica.veterinaria.clientes.Cliente;
import com.clinica.veterinaria.facturas.Factura;
import com.clinica.veterinaria.user.User;
import com.clinica.veterinaria.ventas_linea.VentaLinea;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import org.zkoss.util.media.AMedia;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Messagebox;

/**
 * 
 */
public class Venta {
    
    private int _id;
    private Date _fecha;
    private Time _hora;
    private Cliente _cliente;
    private User _vendedor;
    private List <VentaLinea>_venta_lineas = new ArrayList<>();
    private double monto_deudor; // ---> Variable de control de la deuda
    private boolean _deudor; // Cambiamos al estado deudor
   
        
    private User _veterinario;  // Se va
    private String _albaran;    // ??????
    private Factura _factura;   // ??????
   
    
    public Venta () {}
    
    public Venta (int id){
        this._id = id;
    }
    public Venta(int _id, Date _fecha, Time _hora, Cliente _cliente, User _vendedor, User _veterinario , String _albaran, Factura _factura, boolean _facturado) {
        this._id = _id;
        this._fecha = _fecha;
        this._hora = _hora;
        this._cliente = _cliente;
        this._vendedor = _vendedor;
        this._veterinario = _veterinario;
        this._albaran = _albaran;
        this._factura = _factura;
        this._deudor = _facturado;
    }

    public Cliente getCliente() {
        return _cliente;
    }

    public void setCliente(Cliente _cliente) {
        this._cliente = _cliente;
    }

    public User getVendedor() {
        return _vendedor;
    }

    public void setVendedor(User _vendedor) {
        this._vendedor = _vendedor;
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

    public int getId() {
        return _id;
    }

    public void setId(int _id) {
        this._id = _id;
    }

    public User getVeterinario() {
        return _veterinario;
    }

    public void setVeterinario(User _veterinario) {
        this._veterinario = _veterinario;
    }

    public double getMonto_deudor() {
        return monto_deudor;
    }

    public void setMonto_deudor(double monto_deudor) {
        this.monto_deudor = monto_deudor;
    }

   
    
    
 
    
    public List<VentaLinea> getVenta_lineas() {
        return _venta_lineas;
    }

    public void setVenta_lineas(List<VentaLinea> _venta_lineas) {
        this._venta_lineas = _venta_lineas;
    }

    /*
    * Si contiene el producto/servicio en la venta
    */
    public boolean contiene(VentaLinea ventalinea){
        boolean existe = false;          
        for(VentaLinea l :_venta_lineas){
            if(ventalinea.equalsItem(l)){
                existe = true;
                break;
            }
        }
        return existe;
    }
    
    public boolean addVentaLinea(VentaLinea ventalinea) {
        boolean rta = false;
        if(!contiene(ventalinea)){
            _venta_lineas.add(ventalinea);
            rta = true;
        }
        return rta;
    }
    
    public void removerVentaLinea(VentaLinea ventalinea){       
        if(contiene(ventalinea)) 
            _venta_lineas.remove(ventalinea);
    }
    
    public void updateVentaLinea(VentaLinea ventalinea){
        for (final ListIterator<VentaLinea> i =_venta_lineas.listIterator(); i.hasNext();) {
            final VentaLinea x = i.next();
            if(ventalinea.equalsItem(x)){
                i.set(ventalinea);
                break;
            }
        }
    }
    
    public void eliminarVentaLinea(VentaLinea ventalinea) {
        _venta_lineas.remove(ventalinea);
    }
    

    
    //Coste total + iva
    public float getCoste() {
        float coste_total = 0;
        Iterator <VentaLinea> it = _venta_lineas.iterator();
        while(it.hasNext()){
            //VentaLinea ln = it.next();
            coste_total += it.next().getPreciototal(); //getPvp() * ln.getCantidad() * (1+(ln.getIva() * 0.01));
        }
        return coste_total;
    }
    
    public float getIvas() {
        float coste_total = 0;
        Iterator <VentaLinea> it = _venta_lineas.iterator();
        while(it.hasNext()){
            VentaLinea ln = it.next();
            coste_total += ln.getPreciototal()*ln.getIva()/100;//(ln.getPvp() * ln.getCantidad() * (1+(ln.getIva() * 0.01))) - (ln.getPvp() * ln.getCantidad());
        }
        return coste_total;
    }
    
    public float getCostesinIva() {
//        return getCoste() - getIvas();
        float coste_total = 0;
        Iterator <VentaLinea> it = _venta_lineas.iterator();
        while(it.hasNext()){
            //VentaLinea ln = it.next();
            coste_total += it.next().getMonto(); //ln.getPvp() * ln.getCantidad());
        }
        return coste_total;
    }
    
    public int getNum_articulos(){
        int num = 0;
        Iterator <VentaLinea> it = _venta_lineas.iterator();
        while(it.hasNext()){
            VentaLinea ln = it.next();
            num +=  ln.getCantidad();
        }
        return num;
    }
    
    public String getAlbaran() {
        return _albaran;
    }

    public void setAlbaran(String _albaran) {
        this._albaran = _albaran;
    }

    public Factura getFactura() {
        return _factura;
    }

    public void setFactura(Factura _factura) {
        this._factura = _factura;
    }

    public boolean isDeudor() {
        return _deudor;
    }

    public void setDeudor(boolean _facturado) {
        this._deudor = _facturado;
    }
    
    public void descargarAlbaran() throws FileNotFoundException, IOException {
        try{
            //if(this.getAlbaran() == null){                Messagebox.show("Venta: Factura no encontrado", "Aviso", Messagebox.OK, Messagebox.EXCLAMATION);            }            else{
//                File f = new File(this.getAlbaran() );
//                byte[] buffer = new byte[ (int) f.length() ];
//                FileInputStream fs = new FileInputStream(f);
//                fs.read( buffer ); 
//                fs.close();
//
//                ByteArrayInputStream is = new ByteArrayInputStream(buffer);
//                AMedia amedia = new AMedia("albaran-" + this.getFecha() + "-" + this.getId(), "pdf", "application/pdf", is);
                Desktop desktop = Executions.getCurrent().getDesktop();
                String sb = String.format("boleta-%tF-%d.pdf", getFecha(), getId() );
                //System.out.println("SB: " + sb);
                
                //sb.delete(0, 12);
                //System.out.println("SB despues: " + sb);
                String realpath = desktop.getWebApp().getRealPath("/albaranes");
                String slash = "/";
                String ruta = realpath + slash +sb;
                File f = new File(ruta);
                byte[] buffer = new byte [(int) f.length()]; 
                FileInputStream fs = new FileInputStream(f);
                fs.read( buffer ); 
                fs.close();

                ByteArrayInputStream is = new ByteArrayInputStream(buffer);
                AMedia amedia = new AMedia(f, null, null);

                
                Filedownload.save(amedia);
                
            
        }catch (FileNotFoundException e){
            Messagebox.show("FileNotFoundException: Factura no encontrado", "Aviso", Messagebox.OK, Messagebox.EXCLAMATION);
        }catch (IOException e) {
            Messagebox.show("IOException: Factura no encontrado", "Aviso", Messagebox.OK, Messagebox.EXCLAMATION);
        }
        
    }

    
    
}
