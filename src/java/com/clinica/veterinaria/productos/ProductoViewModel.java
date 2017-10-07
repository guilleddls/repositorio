package com.clinica.veterinaria.productos;

import com.clinica.veterinaria.albaranes.FirstPdf;
import com.clinica.veterinaria.albaranes.MyFirstTable;
import com.clinica.veterinaria.iva.Iva;
import com.clinica.veterinaria.iva.IvaDAO;
import com.clinica.veterinaria.producto_familia.ProductoFamilia;
import com.clinica.veterinaria.producto_familia.ProductoFamiliaDAO;
import com.clinica.veterinaria.proveedores.Proveedor;
import com.clinica.veterinaria.proveedores.ProveedorDAO;
import com.clinica.veterinaria.util.UploadFile;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.Validator;
import org.zkoss.bind.annotation.*;
import org.zkoss.bind.validator.AbstractValidator;
import org.zkoss.image.Image;
import org.zkoss.io.Files;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;
            
public class ProductoViewModel {
    @Wire
    Image imgText, mainImage;

    private String filterCodigo = "",
                   filterNombre = "",
                   filterFamilia = "",
                   filterProveedor = "",
                   filterIva = "",
                   filterStock = "",
                   filterFechaalta = "";

    private final ProductoFamiliaDAO prodfamiliaDao = new ProductoFamiliaDAO();
    private final ProveedorDAO proveedorDao = new ProveedorDAO();
    private final IvaDAO ivaDao = new IvaDAO();
    private ListModelList<Producto> productos, prodprov;
    private List<Producto> events;
    private final ProductoDAO eventDao = new ProductoDAO();

    private Set <Producto> selectedItems;
    private Producto selectedEvent, newEvent = new Producto();
    private Proveedor selectedProveedor =  new Proveedor();

    private ProductoFamilia auxProdFamilia = new ProductoFamilia();
    private Iva auxIva = new Iva();
    private Media media;
    private final DataSourceProducto ds = new DataSourceProducto();




    @Init
    public void initSetup(@ContextParam(ContextType.VIEW) Component view, 
                          @ExecutionArgParam("selectedProducto") Producto selectedEvent) 
    {
        Selectors.wireComponents(view, this, false);
        this.selectedEvent = selectedEvent;
//            productos = getProductos();
        if(this.selectedEvent != null){
            auxProdFamilia = this.selectedEvent.getFamilia();
            auxIva = this.selectedEvent.getIva();
        }
    }

    public Media getMedia() {
        return media;
    }

    public Producto getSelectedEvent() {
        return selectedEvent;
    }

    public void setSelectedEvent(Producto selectedEvent) {
        this.selectedEvent = selectedEvent;
    }

    public Set getSelectedItems() {
        return selectedItems;
    }

    public void setSelectedItems(Set<Producto> selectedItems) {
        this.selectedItems = selectedItems;
    }

    public Producto getNewEvent() {
        return newEvent;
    }

    public void setNewEvent(Producto newEvent) {
        this.newEvent = newEvent;
    }

    public List<Producto> getEvents() {
        if(events == null) {
            events = new ListModelList<>();//new ListModelList<Producto>(eventDao.findAll());
        }
        return events;
    }

    public ListModelList<Producto> getProductos() {
        if(productos == null) {
            productos = new ListModelList<>(getEvents());
        }
        productos.setMultiple(true);
        return productos;
    }

    public void setProductos(ListModelList<Producto> productos) {
        this.productos = productos;
    }

    //Devuelve el listado de productos de un proveedor
    public List<Producto> getProdprov() {
        prodprov = new ListModelList<>(eventDao.findAll("select * from zk_producto where id_proveedor = " + this.selectedProveedor.getId() ));
        prodprov.setMultiple(true);
        return prodprov;
    }

    public List<ProductoFamilia> getFamilias() {
        return prodfamiliaDao.findAll();
    }

    public List<Proveedor> getProveedores() {
        return proveedorDao.findAll();
    }

    public List<Iva> getIvas() {
        return ivaDao.findAll();
    }

    public String getFilterCodigo() {
        return filterCodigo;
    }

    /* FILTRADO */
    @NotifyChange
    public void setFilterCodigo(String filterCodigo) {
        this.filterCodigo = filterCodigo;
    }

    public String getFilterNombre() {
        return filterNombre;
    }

    @NotifyChange
    public void setFilterNombre(String filterNombre) {
        this.filterNombre = filterNombre;
    }

    public String getFilterProveedor() {
        return filterProveedor;
    }

    @NotifyChange
    public void setFilterProveedor(String filterProveedor) {
        this.filterProveedor = filterProveedor;
    }

    public String getFilterFamilia() {
        return filterFamilia;
    }

    @NotifyChange
    public void setFilterFamilia(String filterFamilia) {
        this.filterFamilia = filterFamilia;
    }

    public String getFilterIva() {
        return filterIva;
    }

    @NotifyChange
    public void setFilterIva(String filterIva) {
        this.filterIva = filterIva;
    }

    public String getFilterFechaalta() {
        return filterFechaalta;
    }

    @NotifyChange
    public void setFilterFechaalta(String filterFechaalta) {
        this.filterFechaalta = filterFechaalta;
    }

    public String getFilterStock() {
        return filterStock;
    }

    public void setFilterStock(String filterStock) {
        this.filterStock = filterStock;
    }

    
    // Metodos para el doSearch
    private boolean filtrar(Object[] obj, String[] filtros){
        String ivastring;
        for (int i =0; i < obj.length ; i++){ 
            ivastring = String.valueOf(obj[i]).toLowerCase();      
            if (!ivastring.startsWith(filtros[i].toLowerCase())) return false;
        }
        return true;
    }
    
    // True: si son todos nulos o todos vacios
    private boolean areNullOrEmpty(String[] text){
        for (String txt : text) {
            if (txt != null ) { //Si es nulo  no puede ser vacio
                 if ( !txt.isEmpty() ) return false; //Sino es nulo puede ser vacio
            }
           
        }
        return true;
    }
    
    @GlobalCommand
    @Command
    @NotifyChange({"doSearchProducto", "events"})
    public void doSearchProducto()   {
        String[] listaFiltro = {filterCodigo, filterNombre, filterProveedor, filterFamilia, filterIva, filterStock, filterFechaalta};
        events = new ListModelList<>();
        if(areNullOrEmpty(listaFiltro)){
            events.addAll(eventDao.findAll()); 
        }
        else     {
            for (Producto prod : eventDao.findAll()) {
                Object[] objetoIva = {prod.getCodigo(), prod.getNombre(), prod.getProveedor().getNombre(), prod.getFamilia().getNombre(), prod.getIva().getValor(), prod.getStock(),  prod.getFecha_alta()};
                if (filtrar(objetoIva, listaFiltro)){
                    events.add(prod);
                }
            } 
        }
    }
    // Buscador para hacer el filtrado
    @Command 
    @NotifyChange("productos")
    public void doSearch()  {
        String[] listaFiltro = {filterCodigo, filterNombre, filterProveedor, filterFamilia, filterIva, filterStock, filterFechaalta};
        productos = new ListModelList<>();
        
        if(areNullOrEmpty(listaFiltro)){
            productos.addAll(eventDao.findAll()); 
        }
        else{
            for (Producto prod : eventDao.findAll()) {
                Object[] objetoIva = {prod.getCodigo(), prod.getNombre(), prod.getProveedor().getNombre(), prod.getFamilia().getNombre(), prod.getIva().getValor(), prod.getStock(),  prod.getFecha_alta()};
                if (filtrar(objetoIva, listaFiltro)){
                    productos.add(prod);
                }
            }
        }
    }

    @Command 
    @NotifyChange("prodprov")
    public void doSearchProv() {
       String[] listaFiltro = {filterCodigo, filterNombre, filterProveedor, filterFamilia, filterIva, filterStock, filterFechaalta};
        prodprov = new ListModelList<>();
        
        if(areNullOrEmpty(listaFiltro)){
            prodprov.addAll(eventDao.findAll()); 
        }
        else{
            for (Producto prod : eventDao.findAll()) {
                Object[] objetoIva = {prod.getCodigo(), prod.getNombre(), prod.getProveedor().getNombre(), prod.getFamilia().getNombre(), prod.getIva().getValor(), prod.getStock(),  prod.getFecha_alta()};
                if (filtrar(objetoIva, listaFiltro)){
                    prodprov.add(prod);
                }
            }
        }
    }



    @Command("add")
    @NotifyChange({"events", "productos"})
    public void add(@BindingParam("cmp")  final Window x) {

        //SE CREA UN CODIGO CON FAMILIA NOMBRE Y PROVEEDOR (3 primeras letras de los nombres)
        //this.newEvent = crearCodigo(newEvent);
        if(eventDao.insert(this.newEvent)){
            System.out.println("--->>>>>>Insertado en EVENTS");
            events = new ListModelList<>(eventDao.findAll());
        }

        if(ProductoValidator.validar(newEvent)){
            BindUtils.postGlobalCommand(null, null, "doSearchProducto", null);
            Messagebox.show("Se ha guardado el Producto", "Mensaje", Messagebox.OK, Messagebox.QUESTION,
                new org.zkoss.zk.ui.event.EventListener(){
                    @Override
                    public void onEvent(Event e){
                        if(Messagebox.ON_OK.equals(e.getName())){
                            x.detach();
                        }
                    }
                }
            );
        }


        this.newEvent = new Producto();
    }
        
        
    @Command("update")
    @NotifyChange({"events", "productos"})
    public void update(@BindingParam("cmp")  final Window x) {
        if((this.selectedEvent.getFamilia() == null) ){
            this.selectedEvent.setFamilia(this.auxProdFamilia);
        }
        if((this.selectedEvent.getIva() == null)){
            this.selectedEvent.setIva(auxIva);
        }

        //this.selectedEvent = crearCodigo(selectedEvent);
        
        eventDao.update(this.selectedEvent);


        Messagebox.show("Se ha guardado el Producto", "Mensaje", Messagebox.OK, Messagebox.QUESTION,
            new org.zkoss.zk.ui.event.EventListener(){
                @Override
                public void onEvent(Event e){
                    if(Messagebox.ON_OK.equals(e.getName())){
                        x.detach();
                    }
                }
            }
        );

    }
	
    @Command("delete")
    @NotifyChange({"events", "productos"})
    public void delete() {
        if(this.selectedEvent != null) {
            Messagebox.show("¿Desea elimimar " + selectedEvent.getNombre() + " ?", "Advertencia", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION,
                new org.zkoss.zk.ui.event.EventListener(){
                @Override
                public void onEvent(Event e){
                    if(Messagebox.ON_OK.equals(e.getName())){
                        eventDao.delete(selectedEvent);
                        selectedEvent = null;
                        BindUtils.postGlobalCommand(null, null, "doSearchProducto", null);
                    }
                }
            }
            );
        }else
             Messagebox.show("Seleccione un producto", "Advertencia", Messagebox.OK, Messagebox.INFORMATION);
          
    }
        
    
//    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Command
    public void send() {
        Map args = new HashMap();
        args.put("returnProducto", this.selectedItems);
        BindUtils.postGlobalCommand(null, null, "refreshvalues", args);
    }
    
    @Command
    public void crearPDF(){
        FirstPdf.main();
        MyFirstTable.main();
            
    }
    
    public List<Producto> getAvisoProducto(){
        return eventDao.findAll("SELECT * FROM zk_producto WHERE stock < 3");
    }
    
    
    @Command
    @NotifyChange({"media", "events"})
    public void uploadFile(@ContextParam(ContextType.TRIGGER_EVENT) UploadEvent event, 
            @BindingParam("cmp") org.zkoss.zul.Image img) throws FileNotFoundException, IOException {
        Media oMedia = event.getMedia();
        String name = oMedia.getName();
        if (oMedia instanceof org.zkoss.image.Image) {
            String realpath = Executions.getCurrent().getDesktop().getWebApp().getRealPath("/uploads/productos");
            
            
            String PATH = realpath + "/" + name;
            File baseDir = new File(realpath + "/");
                
            if (!baseDir.exists()) {
                baseDir.mkdirs();
            }
           

            Files.copy(new File(PATH), oMedia.getStreamData());
            new UploadFile().imagenProducto(PATH);

            if(img != null){
                img.setWidth("100px");
                img.setHeight("100px");
                img.setSrc("/uploads/productos/" + name);
            }
            if(this.selectedEvent != null){
                this.selectedEvent.setFotografia(name); //"/uploads/productos/" + name);
            }
            else{
                this.newEvent.setFotografia(name);//"/uploads/productos/" + name);
            }
        }
    }
    
//    BufferedInputStream in = null;
//    BufferedOutputStream out = null;
//    byte[] fi = media.getByteData();
//
//    InputStream fin = media.getStreamData();
//    in = new BufferedInputStream(fin);
//
//    FileOutputStream fout = new FileOutputStream(file);
//    out = new BufferedOutputStream(fout);
//    byte buffer[] = fi;// 
//    int ch = in.read(buffer);
//    while (ch != -1) {
//        out.write(buffer, 0, ch);
//        ch = in.read(buffer);
//    }
        
    @Command
    public void downloadFile() {
        if (media != null) {
            Filedownload.save(media);
        }
    }
    
    @GlobalCommand
    @NotifyChange({"selectedProveedor", "events", "prodprov"})
    public void refreshProveedor(@BindingParam("returnProveedor") Proveedor prov){
        this.selectedProveedor = prov;
    }
    
    @Command("modificarProducto")
    @NotifyChange({"events", "productos"})
    public void modificarProducto() {
        final HashMap<String, Object> map = new HashMap<>();
        map.put("selectedProducto", selectedEvent );
        Executions.createComponents("/productos/producto-modificar.zul", null, map);
    }
    
    @Command("addproducto")
    @NotifyChange({"events", "productos"})
    public void addproducto() {
        Executions.createComponents("/productos/producto-nuevo.zul", null, null);
    }
    
    
    public Validator getValidarModificado(){
        return new AbstractValidator(){
            @Override
            public void validate(ValidationContext ctx) {
                String codigo = (String)ctx.getProperties("codigo")[0].getValue();


                String nombre = (String)ctx.getProperties("nombre")[0].getValue();
                Object pvp = ctx.getProperties("pvp")[0].getValue();
                Object stock = ctx.getProperties("stock")[0].getValue();
                Object iva = ctx.getProperties("iva")[0].getValue();

                if(nombre == null || "".equals(nombre))
                        this.addInvalidMessage(ctx, "nombre", "Debes introducir un nombre");		

                if(codigo == null || "".equals(codigo)){}

                else if(new ProductoDAO().existCodigo(codigo) && !codigo.equals(selectedEvent.getCodigo()))
                        this.addInvalidMessage(ctx, "codigo", "El codigo ya existe");
                if(stock == null )
                        this.addInvalidMessage(ctx, "stock", "Debes introducir el stock");
                else if(((Integer)stock)<0)
                        this.addInvalidMessage(ctx, "stock", "El stock tiene que ser positivo");

                if(pvp == null)
                        this.addInvalidMessage(ctx, "pvp", "Debes introducir un precio");

                if(iva == null)
                        this.addInvalidMessage(ctx, "iva", "Debes seleccionar un iva");
            }
        };
    }
}

