package com.clinica.veterinaria.productos;

import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.validator.AbstractValidator;

public class ProductoValidator extends AbstractValidator {

    public static boolean validar(Producto pro){
        String mensaje;
        mensaje = NullOrEmpty(pro.getNombre());
        mensaje += (new ProductoDAO().existCodigo(pro.getCodigo()))?"":"x";

        mensaje += (pro.getIva() == null)? "X":"";

        mensaje += (pro.getStock() == 0 || pro.getStock() <0)? "X":"";
        mensaje += (pro.getPvp() == 0 )? "X":""; 
        System.out.println("Mensaje:" + mensaje);
        return mensaje.isEmpty();
           
    }
    
   private static String NullOrEmpty(String texto){
       if(texto == null || texto.isEmpty()){
           return "falso";
       }
       return "";
    }
   
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
   
        else if(new ProductoDAO().existCodigo(codigo))
                this.addInvalidMessage(ctx, "codigo", "El codigo ya existe");
        
        if(ctx.getProperties("familia")[0].getValue() == null )
                this.addInvalidMessage(ctx, "familia", "Debes seleccionar una familia");
        if(ctx.getProperties("proveedor")[0].getValue() == null )
                this.addInvalidMessage(ctx, "proveedor", "Debes seleccionar un proveedor");
        
        if(stock == null )
                this.addInvalidMessage(ctx, "stock", "Debes introducir el stock");
        else if(((Integer)stock)<0)
                this.addInvalidMessage(ctx, "stock", "El stock tiene que ser positivo");

        if(pvp == null)
                this.addInvalidMessage(ctx, "pvp", "Debes introducir un precio");

        if(iva == null)
                this.addInvalidMessage(ctx, "iva", "Debes seleccionar un iva");
        
    }
}
