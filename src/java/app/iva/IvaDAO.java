package app.iva;

import com.clinica.veterinaria.clientes.Cliente;
import com.clinica.veterinaria.clientes.ClienteDAO;
import com.clinica.veterinaria.proveedores.Proveedor;
import com.clinica.veterinaria.proveedores.ProveedorDAO;
import com.clinica.veterinaria.util.Fecha;
import com.conexion.ManagerConexion;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 *
 * 
 */
public class IvaDAO extends ManagerConexion {
    
    private String getCampos(){   
        return  "id, fecha, tipo, nro_puesto, nro_factura, id_persona , netogravado, iva21, iva27, iva10, nogravado, retencion, opexcentas, total, anulada";
    }
    
    private String getStringUpdate(Iva iva){
        String tabla = getTabla(iva);
        int id_persona = (tabla.equals("ivavta"))? ((IvaVenta)iva).getCliente().getId(): ((IvaCompra)iva).getProveedor().getId();
        Object[] args ={iva.getId(), sqlFormatFecha(iva.getFecha()), iva.getTipo(), iva.getNro_puesto(), iva.getNro_factura(), id_persona, iva.getNetogravado(), iva.getIva21(), iva.getIva27(), iva.getIva10(), iva.getNogravado(), iva.getRetencion(), iva.getOpexcentas(), iva.getTotal(), (iva.isAnulado()?1:0) };
        String sql = "UPDATE "+tabla+" SET fecha = %2$s, tipo = '%3$s', nro_puesto = '%4$d', nro_factura = '%5$d', id_persona = '%6$d', netogravado = '%7$f', iva21 = '%8$f', iva27 = '%9$f', iva10 = '%10$f', nogravado = '%11$f', retencion = '%12$f', opexcentas = '%13$f', total = '%14$f', anulada = %15$d WHERE id = %1$d";
        return String.format(java.util.Locale.US,sql, args);
    }
    
    private String getStringInsert(Iva iva){
        String tabla = getTabla(iva);
        int id_persona = (tabla.equals("ivavta"))? ((IvaVenta)iva).getCliente().getId(): ((IvaCompra)iva).getProveedor().getId();
        Object[] args ={iva.getId(),sqlFormatFecha(iva.getFecha()), iva.getTipo(), iva.getNro_puesto(), iva.getNro_factura(), id_persona, iva.getNetogravado(), iva.getIva21(), iva.getIva27(), iva.getIva10(), iva.getNogravado(), iva.getRetencion(), iva.getOpexcentas(), iva.getTotal(), (iva.isAnulado()?1:0) };
        String sql = "INSERT INTO "+tabla+"("+getCampos()+") VALUES ('%1$d', %2$s, '%3$s', '%4$d', '%5$d', '%6$d', '%7$f', '%8$f', '%9$f', '%10$f', '%11$f', '%12$f', '%13$f', '%14$f', %15$d)";
        
        //System.out.println(String.format(java.util.Locale.US,"%1$tY-%1$tm-%1$td", new java.util.Date()));
        return String.format(java.util.Locale.US,sql, args);
    }
    
    
    
    private String getTabla(Iva iva){
       return ( iva instanceof IvaCompra)? "ivacompra":"ivavta";
    }
    
    
    
    public Iva getOne(Iva iva){
        String sql = ("SELECT * FROM "+ getTabla(iva) +" WHERE tipo = '"+iva.getTipo()+"' ORDER BY nro_puesto DESC, nro_factura DESC LIMIT 1");
        Iva i;
        if(getTabla(iva).equals("ivacompra")){
            i = findAllCompra(sql).get(0);         
        }else{
            i = findAllVenta(sql).get(0);
        }
        iva.setNro_puesto(i.getNro_puesto());
        iva.setNro_factura(i.getNro_factura());
        return iva;
        
    }
    
    public boolean existeCodigo(Iva event, String tipo, int puesto, int num) {
        String condicion = (tipo.equals("Z"))?"":"AND nro_puesto="+puesto;
        String sql = ("SELECT * FROM "+ getTabla(event) +" WHERE tipo = '"+tipo+"'"+ condicion+" AND nro_factura =" +num);
        boolean rta;
        if(getTabla(event).equals("ivacompra")){
            rta = findAllCompra(sql).size() > 0;
        }else{
            rta = findAllVenta(sql).size() > 0;
        }
        return rta;
    }
    
    public List<IvaCompra> findAllCompra(){
        Calendar calendario = Calendar.getInstance();
        calendario.add(Calendar.YEAR, -1); //TOMA EL AÑO PASADO Y FINALES DE ESTE
        calendario.set(Calendar.MONTH, 1);
        calendario.set(Calendar.DAY_OF_MONTH, 1);
        Date fechaDesde = calendario.getTime();
        
        calendario = Calendar.getInstance();
        
        calendario.set(Calendar.MONTH, 12);
        calendario.set(Calendar.DAY_OF_MONTH, calendario.getActualMaximum(Calendar.DAY_OF_MONTH));
        
        Date fechaHasta = calendario.getTime();
        
      
        return findAllCompra(fechaDesde, fechaHasta);
    }
    
    public List<IvaVenta> findAllVenta(){
        Calendar calendario = Calendar.getInstance();
        calendario.add(Calendar.YEAR, -1); //TOMA EL AÑO PASADO Y FINALES DE ESTE
        calendario.set(Calendar.MONTH, 1);
        calendario.set(Calendar.DAY_OF_MONTH, 1);
        Date fechaDesde = calendario.getTime();
        
        calendario = Calendar.getInstance();
        
        calendario.set(Calendar.MONTH, 11);
        calendario.set(Calendar.DAY_OF_MONTH, calendario.getActualMaximum(Calendar.DAY_OF_MONTH));
        
        Date fechaHasta = calendario.getTime();
        
      
        return findAllVenta(fechaDesde, fechaHasta);
    }
    
    public List<IvaCompra> findAllCompra(int year) {
        return findAllCompra("SELECT * FROM ivacompra WHERE YEAR(fecha) >= '"+year+"' ORDER BY fecha DESC" );
    }
    
    public List<IvaVenta> findAllVenta(int year) {
        return findAllVenta("SELECT * FROM ivavta WHERE YEAR(fecha) >= '"+year+"' ORDER BY fecha DESC");
    }
    
    public List<IvaCompra> findAllCompra(int year, int month) {
        return findAllCompra("SELECT * FROM ivacompra WHERE YEAR(fecha) = '"+year+"' AND MONTH(fecha) = '"+month+"' AND anulada = 0");
    }
    
    public List<IvaVenta> findAllVenta(int year, int month) {
        String sql = "SELECT * FROM ivavta WHERE YEAR(fecha) = '"+year+"' AND MONTH(fecha) = '"+month+"' AND anulada = 0";
        return findAllVenta(sql);
    }
    
    public List<IvaCompra> findAllCompra(Date desde, Date hasta) {
        return findAllCompra("SELECT * FROM ivacompra WHERE fecha >= "+ sqlFormatFecha(desde)+" AND fecha <= "+ sqlFormatFecha(hasta));
    }
    
    public List<IvaVenta> findAllVenta(Date desde, Date hasta) {
        String sql = "SELECT * FROM ivavta WHERE fecha >= "+ sqlFormatFecha(desde)+" AND fecha <= "+ sqlFormatFecha(hasta);
        return findAllVenta(sql);
    }
    
    public List<IvaCompra> findAllCompra(String consulta) {
		
        List<IvaCompra> lista = new ArrayList<>();
        try {

            Statement stmt = getStatement();
            ResultSet rs = stmt.executeQuery(consulta);
            List<Proveedor> provList = new ProveedorDAO().findAll();

            IvaCompra iva;
            int i;
            while (rs.next()) {
                i = 1;
                iva = new IvaCompra();
                iva.setId(rs.getInt(i++));
                iva.setFecha(rs.getDate(i++));
                iva.setTipo(rs.getString(i++));
                iva.setNro_puesto(rs.getInt(i++));
                iva.setNro_factura(rs.getInt(i++));
                for(Proveedor p : provList){                               
                    if(p.getId() == rs.getInt(i) ) {
                        iva.setProveedor(p);                        
                        break;
                    }
                }
                i++;
                iva.setNetogravado(rs.getDouble(i++));
                iva.setIva21(rs.getDouble(i++));
                iva.setIva27(rs.getDouble(i++));
                iva.setIva10(rs.getDouble(i++));
                iva.setNogravado(rs.getDouble(i++));
                iva.setRetencion(rs.getDouble(i++));
                iva.setOpexcentas(rs.getDouble(i++));
                iva.setTotal(rs.getDouble(i++));
                iva.setAnulado(rs.getInt(i)==1);
                lista.add(iva);
            }
            closeResultSet(rs);
            closeStatement(stmt);
        } catch (SQLException e) {
                e.printStackTrace(System.err);
        } finally {
            close();
        }

        return lista;
    }
    
    public List<IvaVenta> findAllVenta(String consulta) {
		
        List<IvaVenta> lista = new ArrayList<>();
        try {

            Statement stmt = getStatement();
            ResultSet rs = stmt.executeQuery(consulta);
            List<Cliente> cliList = new ClienteDAO().findAll();

            IvaVenta iva;
            int i;
            while (rs.next()) {
                i = 1;
                iva = new IvaVenta();
                iva.setId(rs.getInt(i++));
                iva.setFecha(rs.getDate(i++));
                iva.setTipo(rs.getString(i++));
                iva.setNro_puesto(rs.getInt(i++));
                iva.setNro_factura(rs.getInt(i++));
                for(Cliente p : cliList){                               
                    if(p.getId() == rs.getInt(i) ) {
                        iva.setCliente(p);                       
                        break;
                    }
                }
                 i++;
                iva.setNetogravado(rs.getDouble(i++));
                iva.setIva21(rs.getDouble(i++));
                iva.setIva27(rs.getDouble(i++));
                iva.setIva10(rs.getDouble(i++));
                iva.setNogravado(rs.getDouble(i++));
                iva.setRetencion(rs.getDouble(i++));
                iva.setOpexcentas(rs.getDouble(i++));
                iva.setTotal(rs.getDouble(i++));
                iva.setAnulado(rs.getInt(i)==1);
                lista.add(iva);
            }
            closeResultSet(rs);
            closeStatement(stmt);
        } catch (SQLException e) {
                e.printStackTrace(System.err);
        } finally {
            close();
        }

        return lista;
    }
    
    
    public boolean delete(Iva iva) {
        
        return execute("DELETE FROM "+getTabla(iva) +" WHERE id = " + iva.getId());
    }

    public boolean insert(Iva iva) {
        String tabla = getTabla(iva);
        iva.setId(getLastID(tabla));
        return execute(getStringInsert(iva));

    }
	
    public boolean update(Iva iva) {
        return execute(getStringUpdate(iva));
    }
    
    public boolean anular(Iva iva){
        iva.setAnulado(!iva.isAnulado());
        return update(iva);
    }
    
    private String sqlFormatFecha(java.util.Date texto){
        if(texto == null) return "NULL";
        else return "'"+Fecha.dateFormato.format(texto)+"'";

    }
    private String sqlFormatString(String texto){
        if(texto == null || texto.isEmpty()) return "NULL";
        else return "'"+texto+"'";

    }
    
    
    private int getLastID(String tabla) {
  
            int last_id = 0;
            try{
                Statement stmt = getStatement();
                ResultSet rs = stmt.executeQuery("SELECT IFNULL(MAX(id),0) FROM "+ tabla);
                rs.next();
                last_id = rs.getInt(1);
                closeResultSet(rs);
                closeStatement(stmt);
            } catch (SQLException e) {
			e.printStackTrace(System.err);
		} finally {
		    close();
		}
            return last_id+1;

        }

    
}
