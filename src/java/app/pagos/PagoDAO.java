package app.pagos;

import com.conexion.ManagerConexion;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PagoDAO extends ManagerConexion {
    
    public List<Pago> findAll(){
        return findAll("SELECT * FROM zk_pago");
    }
    
    public List<Pago> findByVenta(int id_venta){
        return findAll("SELECT * FROM zk_pago WHERE id_venta = "+ id_venta);
    }
    
    public List<Pago> findAll(String sql){
        List<Pago> pagos = new ArrayList<>();
        try {            
            Statement stmt = getStatement();
            ResultSet rs = stmt.executeQuery(sql);
            Pago pago;
            while(rs.next()){
                pago = new Pago(rs.getInt(1), rs.getInt(2), rs.getDate(3), rs.getDouble(4) );
                pagos.add(pago);
            }
            rs.close();
            stmt.close();
            
        } catch (SQLException ex) {
            System.err.println(ex);
        }
        return pagos;
    }
    
    public void insert(Pago pago){
        execute("INSERT INTO zk_pago (id_venta,fecha, monto) VALUES ("+pago.getIdVenta()+",'"+new java.sql.Date(pago.getFecha().getTime())+"',"+pago.getMonto() +")");
        execute("UPDATE zk_venta SET monto_deudor = monto_deudor - "+ pago.getMonto() +" WHERE id = " +pago.getIdVenta());
    }
        
    public void delete(Pago pago){
        delete(pago,1);  
    }
    
    /**
     * @param pago
     * @param idDelete 1: borrar solo el pago, 2: borrar todos los pagos de una venta 
     */
    private void delete(Pago pago, int idDelete){
        switch(idDelete){
            case 1: execute("DELETE FROM zk_pago WHERE id = "+pago.getId());
                    execute("UPDATE zk_venta SET monto_deudor = monto_deudor + "+ pago.getMonto()+" WHERE id = " +pago.getIdVenta() );
                    break;
            case 2: execute("DELETE FROM zk_pago WHERE id_venta = "+pago.getIdVenta()); break;
        }
    }
        
    public void update(Pago pago){
        execute("UPDATE zk_venta INNER JOIN zk_pago ON zk_venta.id = zk_pago.id_venta SET monto_deudor = monto_deudor + monto - "+pago.getMonto()+" WHERE zk_venta.id = " + pago.getIdVenta());
        execute("UPDATE zk_pago SET fecha = '"+new java.sql.Date(pago.getFecha().getTime())+"', monto = "+pago.getMonto()+" WHERE id = "+pago.getId());
    }
    
 
    
}
