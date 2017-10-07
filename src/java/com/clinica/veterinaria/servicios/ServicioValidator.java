package com.clinica.veterinaria.servicios;

import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.validator.AbstractValidator;

/**
 *
 * 
 */
public class ServicioValidator extends AbstractValidator {

    public static boolean validar(Servicio serv){
        String mensaje;
        mensaje = NullOrEmpty(serv.getNombre());
        

        mensaje += (serv.getIva() == null)? "X":"";
        mensaje += (serv.getFamilia()== null)? "z":"";
        
        mensaje += (serv.getPrecio()== 0 )? "y":""; 
        System.out.println("Mensaje:" + mensaje);
        return mensaje.isEmpty();
           
    }
    
    private static String NullOrEmpty(String texto){
       if(texto == null || texto.isEmpty()){
           return "falso";
       }
       return "";
    }
    
    public void validate(ValidationContext ctx) {
        
        String nombre = (String)ctx.getProperties("nombre")[0].getValue();
        Object familia = ctx.getProperties("familia")[0].getValue();
        Object iva = ctx.getProperties("iva")[0].getValue();
        Object precio = ctx.getProperties("precio")[0].getValue();
        
        if(nombre == null || "".equals(nombre)) {
            this.addInvalidMessage(ctx, "nombre", "Ingrese un nombre");
        }		

        if(familia == null) {
            this.addInvalidMessage(ctx, "familia", "Seleccione una familia");
        }
        
        if(iva == null) {
            this.addInvalidMessage(ctx, "iva", "Seleccione un iva");
        }
        if(precio == null) {
            this.addInvalidMessage(ctx, "precio", "Ingrese un precio");
        }
    }
}
