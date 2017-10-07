package app.iva;


import com.reportes.ReportConfig;
import com.reportes.ReportType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.ListModelList;

/**
 *
 * 
 */
public class IvaReporte {
    private ReportType reportType = null;
    private ReportConfig reportConfig = null;
    private int year = Calendar.getInstance().get(Calendar.YEAR);
    private String month = ""+(Calendar.getInstance().get(Calendar.MONTH)+1);
    private String tipo = "venta";
    
    public static DataSourceIva getDataSource(boolean venta, int mes, int year){
        return new DataSourceIva(venta, mes, year);
    }
    
    private final ListModelList<ReportType> reportTypesModel = new ListModelList<>(
            Arrays.asList(
                    new ReportType("PDF", "pdf"), 
                    new ReportType("HTML", "html"), 
                    new ReportType("Word (RTF)", "rtf"), 
                    new ReportType("Excel", "xls"), 
                    new ReportType("Excel (JXL)", "jxl"), 
                    new ReportType("CSV", "csv"), 
                    new ReportType("OpenOffice (ODT)", "odt"))
    );
    
    @Command("showReport")
    @NotifyChange("reportConfig")
    public void showReport() {
        String ruta = Sessions.getCurrent().getWebApp().getRealPath("/ivas/reporte/IvaReporte.jasper");
        reportConfig = new ReportConfig(ruta);
        reportConfig.setType(reportType);
        reportConfig.setDataSource(new DataSourceIva(isVenta(), toInt() ,year)); //new DataSourceListaCliente());
        //Aca pasamos los parametros
        //reportConfig.put("imagen", Sessions.getCurrent().getWebApp().getRealPath("/clientes/reporte/wood.jpg"));
        reportConfig.put("titulo", "LIBRO IVA "+((isVenta())? "VENTA": "COMPRA"));
        reportConfig.put("periodo", toInt()+"/"+year);
        
    }

    
    private boolean isVenta(){      
        return tipo.equals("venta");
    }
    
    private int toInt(){
       return Integer.parseInt(month);
    }
    
   
    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }


    
    public ListModelList<ReportType> getReportTypesModel() {
        return reportTypesModel;
    }
 
    
    public ReportConfig getReportConfig() {
        return reportConfig;
    }
     
    public ReportType getReportType() {
        return reportType;
    }
 
    public void setReportType(ReportType reportType) {
        this.reportType = reportType;
    }
    
    
}




class DataSourceIva implements JRDataSource {
    private int index = -1;
    private List<Iva> dataList = new ArrayList<>();
 
    public DataSourceIva(boolean venta, int mes, int year) {
        if(venta){
            dataList = new ArrayList<Iva>(new IvaDAO().findAllVenta(year, mes));
            //System.out.println(dataList.size());
        }
        else{
            dataList = new ArrayList<Iva>(new IvaDAO().findAllCompra(year, mes));
        }
    }
    
    @Override
    public boolean next() throws JRException {
        index++;
        return (index < dataList.size());
    }

    @Override
     public Object getFieldValue(JRField field) throws JRException {        
        if (field.getName() !=  null){
            Iva factura = dataList.get(index);
            switch (field.getName()) {  
                case "fecha":     
                    return factura.getFecha();
                case "tipo":   
                    return factura.getTipo(); 
                case "codigo":   
                    return factura.getTipo()+ "|"+factura.getFacturaCodigo();                            
                case "cuit":               
                    return factura.getCuit();  
                case "persona":               
                    int tam = factura.getPersona().length();
                    tam = tam > 22? 22: tam;
                    return factura.getPersona().substring(0, tam);
                case "netogravado":
                    return factura.getNetogravado();
                case "iva21":
                    return factura.getIva21();
                case "iva27":
                    return factura.getIva27();
                case "iva10":
                    return factura.getIva10();
                case "nogravado":
                    return factura.getNogravado() + factura.getOpexcentas() + factura.getRetencion()  ;
                case "excenta":
                    return factura.getOpexcentas();
                case "retencion":
                    return factura.getRetencion();
                case "total":
                    return factura.getTotal();                         
            }
        }
        return "";
                
    }
    
    
              
}
