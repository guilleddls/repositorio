package app.iva;

import com.clinica.veterinaria.util.Fecha;
import java.util.Date;

/**
 *
 * 
 */
public abstract class  Iva {
    
    private int id;
    private Date fecha;
    private String tipo;
    private int nro_puesto;
    private int nro_factura;
    
    private double netogravado;
    private double iva21;
    private double iva27;
    private double iva10;
    private double nogravado;
    private double retencion;
    private double opexcentas;
    private double total;
    private boolean anulado;
    private String fechastring;
    
    
    public Iva() {
        this.id = -1;
        this.tipo ="";
        fechastring = ""; 
    }

    
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFacturaCodigo(){
        String rta;
        if(tipo.equals("Z")){
            rta = String.valueOf(nro_factura);
        }else{
            rta = String.format("%04d", nro_puesto) +"-" + String.format("%08d", nro_factura);
        }
        return rta;
    }
    
    
    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
        
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getNro_puesto() {
        return nro_puesto;
    }

    public void setNro_puesto(int nro_puesto) {
        this.nro_puesto = nro_puesto;
    }

    public int getNro_factura() {
        return nro_factura;
    }

    public void setNro_factura(int nro_factura) {
        this.nro_factura = nro_factura;
    }


    public double getNetogravado() {
        return netogravado;
    }

    public void setNetogravado(double netogravado) {
        this.netogravado = netogravado;
    }

    public double getIva21() {
        return iva21;
    }

    public void setIva21(double iva21) {
        this.iva21 = iva21;
    }

    public double getIva27() {
        return iva27;
    }

    public void setIva27(double iva27) {
        this.iva27 = iva27;
    }

    public double getIva10() {
        return iva10;
    }

    public void setIva10(double iva10) {
        this.iva10 = iva10;
    }

    public double getNogravado() {
        return nogravado;
    }

    public void setNogravado(double nogravado) {
        this.nogravado = nogravado;
    }

    public double getRetencion() {
        return retencion;
    }

    public void setRetencion(double retencion) {
        this.retencion = retencion;
    }

    public double getOpexcentas() {
        return opexcentas;
    }

    public void setOpexcentas(double opexcentas) {
        this.opexcentas = opexcentas;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public boolean isAnulado() {
        return anulado;
    }

    public void setAnulado(boolean anulado) {
        this.anulado = anulado;
    }

    public String getPersona(){
        return "";
    }
    
    public String getCuit(){
        return "";
    }

    public String getFechastring() {
        return (fecha != null)? Fecha.toDateFormat(fecha, "dd-MM-yyyy") :fechastring;
    }

    public void setFechastring(String fechastring) {
        this.fechastring = fechastring;
        this.fecha = Fecha.toDate(fechastring, "dd-MM-yyyy");
    }
    
    

}
