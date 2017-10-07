package app.pagos;

import java.util.Date;

public class Pago {
    private int id;
    private int id_venta;
    private Date fecha;
    private double monto;
    
    public Pago() {
    }

    public Pago(int id, int id_venta, Date fecha, double monto) {
        this.id = id;
        this.id_venta = id_venta;
        this.fecha = fecha;
        this.monto = monto;
    }

    

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public int getIdVenta() {
        return id_venta;
    }

    public void setIdVenta(int id_venta) {
        this.id_venta = id_venta;
    }
    
    
    
}
