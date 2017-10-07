package com.reportes;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * 
 */
public class ControlReporte {

    public ControlReporte() {
    }
    
    public void showReporte(){
        try {
                      
                                 
            String master = System.getProperty("user.dir") + "/src/ComponenteReportes/ReporteGVenta.jasper";
            if (master == null) {
                System.err.println("No se encontró el archivo de reporte maestro");
                System.exit(2);
            }
            JasperReport masterReport = null;
            try {
                masterReport = (JasperReport) JRLoader.loadObjectFromFile(master);
            } catch (JRException e) {
                System.err.println("Error cargando el reporte maestro:" + e.getMessage());
                System.exit(3);
            }
            
            
                
            DataSourceListaCliente ds = new DataSourceListaCliente(); //Le envia una lista
                
            JasperPrint jasperPrint = JasperFillManager.fillReport(masterReport, null, ds );
            /*JasperViewer jviewer = new JasperViewer(jasperPrint, false);
            jviewer.setTitle("REPORTE DE VENTAS");
            jviewer.setVisible(true);*/
            
            JasperViewer.viewReport(jasperPrint);
            
        
        } catch (JRException j) {
            System.err.println("Mensaje de error: " + j.getMessage());
            j.printStackTrace(System.err);
        }
    }
    
    
}
