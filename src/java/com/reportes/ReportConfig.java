package com.reportes;

import java.util.HashMap;
import java.util.Map;
import net.sf.jasperreports.engine.JRDataSource;

/**
 *
 * 
 */
public class ReportConfig {
 
    private final String source ;
    private final Map<String, Object> parameters;
    private JRDataSource dataSource;
    private ReportType type;
 
    public ReportConfig(String ruta) {
        source = ruta;
        parameters = new HashMap<>();
        /*parameters.put("ReportTitle", "Address Report");
        parameters.put("DataFile", "CustomDataSource from java");*/
    }
    
    public void put(String nombre, String content){
        parameters.put(nombre, content);
    }
    
    public ReportType getType() {
        return type;
    }
 
    public void setType(ReportType selectedReportType) {
        this.type = selectedReportType;
    }
 
    public String getSource() {
        return source;
    }
 
    public Map<String, Object> getParameters() {
        return parameters;
    }
 
    public JRDataSource getDataSource() {
        return dataSource;
    }
 
    public void setDataSource(JRDataSource reportDataSource) {
        this.dataSource = reportDataSource;
    }
}
