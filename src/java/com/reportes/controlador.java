package com.reportes;


import app.iva.IvaReporte;
import com.conexion.ManagerConexion;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;
import org.zkoss.bind.annotation.Command;
import org.zkoss.zk.ui.Sessions;

/**
 *
 * 
 */
public class controlador  {
    
    @Command
    public void showDeudores(){
        try{
            ManagerConexion man = new ManagerConexion();
            Connection con = man.getConnection();
        String master = Sessions.getCurrent().getWebApp().getRealPath("/clientes/reporte/deudores.jrxml");
            //String master ="C:\\ReporteListaClientes.jrxml";
            System.out.println("RUTA: "+master);
            JasperReport masterReport =  (JasperReport) JRLoader.loadObjectFromFile(master);//JasperCompileManager.compileReport(master);
           
            
                
            JasperPrint jasperPrint = JasperFillManager.fillReport(masterReport, null, con );
            JasperViewer jviewer = new JasperViewer(jasperPrint, false);
            jviewer.setTitle("REPORTE DE DEUDORES");
            jviewer.setVisible(true);
            //JasperViewer.viewReport(jasperPrint);
            
        
        } catch (SQLException | JRException | IllegalStateException j) {
            System.err.println("Mensaje de error: " + j.getMessage());
            j.printStackTrace(System.err);
        }
    }
    
    
    @Command
    public void showProductos(){
        try{
            ManagerConexion man = new ManagerConexion();
            Connection con = man.getConnection();
        String master = Sessions.getCurrent().getWebApp().getRealPath("/productos/reporte/listaProductos.jasper");
            //String master ="C:\\ReporteListaClientes.jrxml";
            System.out.println("RUTA: "+master);
            JasperReport masterReport =  (JasperReport) JRLoader.loadObjectFromFile(master);//JasperCompileManager.compileReport(master);
           
            
                
            JasperPrint jasperPrint = JasperFillManager.fillReport(masterReport, null, con );
            JasperViewer jviewer = new JasperViewer(jasperPrint, false);
            jviewer.setTitle("REPORTE DE PRODUCTOS");
            jviewer.setVisible(true);
            //JasperViewer.viewReport(jasperPrint);
            
        
        } catch (SQLException | JRException | IllegalStateException j) {
            System.err.println("Mensaje de error: " + j.getMessage());
            j.printStackTrace(System.err);
        }
    }
    
    
    @Command
    public void showReport() {
        try{
            //ruta = ; 
        String master = Sessions.getCurrent().getWebApp().getRealPath("/clientes/reporte/ReporteListaClientes.jasper");
            //String master ="C:\\ReporteListaClientes.jrxml";
            System.out.println("RUTA: "+master);
            JasperReport masterReport =  (JasperReport) JRLoader.loadObjectFromFile(master);//JasperCompileManager.compileReport(master);
           
            DataSourceListaCliente ds = new DataSourceListaCliente(); //Le envia una lista
                
            JasperPrint jasperPrint = JasperFillManager.fillReport(masterReport, null, ds );
            JasperViewer jviewer = new JasperViewer(jasperPrint, false);
            jviewer.setTitle("REPORTE DE CLIENTES");
            jviewer.setVisible(true);
            //JasperViewer.viewReport(jasperPrint);
            
        
        } catch (JRException | IllegalStateException j) {
            System.err.println("Mensaje de error: " + j.getMessage());
            j.printStackTrace(System.err);
        }
        
    }
    
    
    @Command
    public void showReportIva() {
        try{
            //ruta = ; 
        //String master = Sessions.getCurrent().getWebApp().getRealPath("/ivas/reporte/IvaReporte.jasper");
        String master = System.getProperty("user.dir") + "/web/ivas/reporte/IvaReporte.jasper";
            //String master ="C:\\ReporteListaClientes.jrxml";
            System.out.println("RUTA: "+master);
            JasperReport masterReport =  (JasperReport) JRLoader.loadObjectFromFile(master);//JasperCompileManager.compileReport(master);
           
            JRDataSource ds = IvaReporte.getDataSource(true, 11, 2015); //Le envia una lista
            Map parametros = new HashMap();
                parametros.put("titulo","LIBRO IVA COMPRA");
                parametros.put("periodo", "12/2015");
            
            
            JasperPrint jasperPrint = JasperFillManager.fillReport(masterReport, parametros, ds );
            JasperViewer jviewer = new JasperViewer(jasperPrint, false);
            jviewer.setTitle("REPORTE DE IVA");
            jviewer.setVisible(true);
            //JasperViewer.viewReport(jasperPrint);
            
        
        } catch (JRException | IllegalStateException j) {
            System.err.println("Mensaje de error: " + j.getMessage());
            j.printStackTrace(System.err);
        }
    }
}
