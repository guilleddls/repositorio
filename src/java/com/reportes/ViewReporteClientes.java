package com.reportes;

import com.clinica.veterinaria.clientes.Cliente;
import com.clinica.veterinaria.clientes.ClienteDAO;
import com.clinica.veterinaria.producto_familia.ProductoFamilia;
import com.clinica.veterinaria.producto_familia.ProductoFamiliaDAO;
import com.clinica.veterinaria.productos.Producto;
import com.clinica.veterinaria.productos.ProductoDAO;
import com.clinica.veterinaria.proveedores.Proveedor;
import com.clinica.veterinaria.proveedores.ProveedorDAO;
import com.clinica.veterinaria.ventas.Venta;
import com.clinica.veterinaria.ventas.VentaDAO;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Button;
import org.zkoss.zul.ListModelList;

/**
 *
 * 
 */
public class ViewReporteClientes {
    ReportType reportType = null;
    private ReportConfig reportConfig = null;
    private List<ProductoFamilia> selectedFamilias = new ArrayList<>();
    private Proveedor selectedProveedor;
    private boolean verProveedor = false;

    
    private final ListModelList<ReportType> reportTypesModel = new ListModelList<>(
            Arrays.asList(
                    new ReportType("PDF", "pdf"), 
                    new ReportType("HTML", "html"), 
                    new ReportType("Word (RTF)", "rtf"), 
                    new ReportType("Excel", "xls"), 
                    new ReportType("Excel (JXL)", "jxl"), 
                    new ReportType("CSV", "csv"), 
                    new ReportType("OpenOffice (ODT)", "odt")));
 
    @Command
    @NotifyChange("verProveedor")
    public void verProveedores(){
        verProveedor = !verProveedor;
        selectedProveedor = null;
    }
    
    public List<Proveedor> getProveedores(){
        return new ProveedorDAO().findAll();
    }
    
    public List<ProductoFamilia> getFamilias(){
        return new ProductoFamiliaDAO().findAll();
    }
 
    @Command("showReport")
    @NotifyChange("reportConfig")
    public void showReport() {
        String ruta = Sessions.getCurrent().getWebApp().getRealPath("/clientes/reporte/ReporteListaClientes.jasper");
        reportConfig = new ReportConfig(ruta);
        reportConfig.setType(reportType);
        reportConfig.setDataSource(new DataSourceListaCliente());
    }
    
    
    @Command("showDeudores")
    @NotifyChange("reportConfig")
    public void showDeudores() {
        String ruta = Sessions.getCurrent().getWebApp().getRealPath("/clientes/reporte/deudores.jasper");
        reportConfig = new ReportConfig(ruta);
        reportConfig.setType(reportType);
        reportConfig.setDataSource(new DataSourceListaDeudores());
        reportConfig.put("imagen", Sessions.getCurrent().getWebApp().getRealPath("/clientes/reporte/wood.jpg"));
    }
   
       
    
    @Command("showProductos")
    @NotifyChange("reportConfig")
    public void showProductos(@ContextParam(ContextType.COMPONENT) Button boton) {
        if(!selectedFamilias.isEmpty()){
        String ruta = Sessions.getCurrent().getWebApp().getRealPath("/productos/reporte/listaProductos.jasper");
        reportConfig = new ReportConfig(ruta);
        reportConfig.setType(reportType);
        reportConfig.setDataSource(new DataSourceProductos(selectedFamilias , selectedProveedor));
        }
        else
        Clients.showNotification("Seleccione una categoria", Clients.NOTIFICATION_TYPE_WARNING, boton, "end_center", 3000);
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

    public List<ProductoFamilia> getSelectedFamilias() {
        return selectedFamilias;
    }

    public void setSelectedFamilias(List<ProductoFamilia> seletedFamilias) {
        this.selectedFamilias = seletedFamilias;
    }

    public Proveedor getSelectedProveedor() {
        return selectedProveedor;
    }

    public void setSelectedProveedor(Proveedor selectedProveedor) {
        this.selectedProveedor = selectedProveedor;
    }

    public boolean isVerProveedor() {
        return verProveedor;
    }

    public void setVerProveedor(boolean verProveedor) {
        this.verProveedor = verProveedor;
    }
    
    
 
}


class DataSourceListaCliente implements JRDataSource {
    private int index = -1;
    private List<Cliente> dataList = new ArrayList<>();
  
    public DataSourceListaCliente() {
        this.dataList = new ClienteDAO().findAll();
    }
    
    @Override
    public boolean next() throws JRException {
        index++;
        return (index < dataList.size());
    }

    @Override
     public Object getFieldValue(JRField field) throws JRException {        
        if (field.getName() !=  null){
            switch (field.getName()) {
                case "Nombre":   
                    return dataList.get(index).getApellidos()+" "+dataList.get(index).getNombre();               
                case "Cuit":     
                    return dataList.get(index).getNif();              
                case "Direccion":               
                    return dataList.get(index).getDireccion();             
                case "Telefono":
                    return dataList.get(index).getTelefono();                         
            }
        }
        return "";
                
    }
}

class DataSourceListaDeudores implements JRDataSource {
    private int index = -1;
    private List<Venta> dataList = new ArrayList<>();
 
    public DataSourceListaDeudores() {
        this.dataList = new VentaDAO().findToDeudores();
    }
    
    @Override
    public boolean next() throws JRException {
        index++;
        return (index < dataList.size());
    }

    @Override
     public Object getFieldValue(JRField field) throws JRException {        
        if (field.getName() !=  null){
            switch (field.getName()) {
                case "suma":   
                    return dataList.get(index).getCliente().getDeuda();               
                case "cliente":     
                    return dataList.get(index).getCliente().getNombre();              
                case "zk_venta_fecha":               
                    return dataList.get(index).getFecha();             
                                       
            }
        }
        return "";
                
    }
              
}

class DataSourceProductos implements JRDataSource {
    private int index = -1;
    private List<Producto> dataList = new ArrayList<>();
 
    public DataSourceProductos(List<ProductoFamilia> familias, Proveedor prov) {
        int id = prov == null? -1: prov.getId();
        this.dataList = new ProductoDAO().findToProductos(id);
        List<Producto> aux = new ArrayList<>();
        for(Producto p : this.dataList) {
            for(ProductoFamilia f : familias){
                if(p.getFamilia().getId() == f.getId()){
                    aux.add(p);
                    break;
                }
            }
        }
        this.dataList  = aux;
    }
    
    @Override
    public boolean next() throws JRException {
        index++;
        return (index < dataList.size());
    }

    @Override
     public Object getFieldValue(JRField field) throws JRException {        
        if (field.getName() !=  null){
            switch (field.getName()) {
                case "codigo":   
                    return dataList.get(index).getCodigo();               
                case "nombre":     
                    return dataList.get(index).getNombre();              
                case "precio":               
                    return dataList.get(index).getPvp();  
                case "stock":               
                    return dataList.get(index).getStock();
                case "proveedor":
                    return dataList.get(index).getProveedor().getNombre();
                case "id_familia":
                    return dataList.get(index).getFamilia().getId();
                case "familia":
                    return dataList.get(index).getFamilia().getNombre();
                                       
            }
        }
        return "";
                
    }
              
}