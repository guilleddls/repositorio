package com.clinica.veterinaria.proveedores;

import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.validator.AbstractValidator;

public class ProveedorValidator extends AbstractValidator {
    public static boolean validar(Proveedor pro){
        String mensaje;
        mensaje = NullOrEmpty(pro.getNombre());
        //mensaje += NullOrEmpty(pro.getTelefono());
        mensaje += NullOrEmpty(pro.getNif());
        mensaje += NullOrEmpty(pro.getDireccion());

        mensaje += NullOrEmpty(pro.getEmail());
        mensaje += (pro.getPoblacion()== null)? "x":"";
        mensaje += (pro.getProvincia()== null )? "X":""; 
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
        Object nif = ctx.getProperties("nif")[0].getValue();
        String nombre = (String)ctx.getProperties("nombre")[0].getValue();
        String email = (String)ctx.getProperties("email")[0].getValue();
        String direccion = (String)ctx.getProperties("direccion")[0].getValue();
        Object poblacion = ctx.getProperties("poblacion")[0].getValue();
        Object provincia = ctx.getProperties("provincia")[0].getValue();
        Object telefono = ctx.getProperties("telefono")[0].getValue();
        
        
        if(nif == null || nif.equals("__-________-_"))
                this.addInvalidMessage(ctx, "nif", "Ingrese un CUIT");
        if(nombre == null || "".equals(nombre))
                this.addInvalidMessage(ctx, "nombre", "Ingrese un nombre");	
        if(email == null || "".equals(email))
                this.addInvalidMessage(ctx, "email", "Ingrese un email");
        if(!(email == null || email.isEmpty()) && !email.matches(".+@.+\\.[a-z]+")) {
                this.addInvalidMessage(ctx, "email", "Email invalido ");
        }
        
        
        
        if(direccion == null || "".equals(direccion))
                this.addInvalidMessage(ctx, "direccion", "Ingrese una dirección");
        
        if(poblacion == null)
                this.addInvalidMessage(ctx, "poblacion", "Seleccione una Localidad");
        if(provincia == null)
                this.addInvalidMessage(ctx, "provincia", "Seleccione una Provincia");
        if(telefono == null)
                this.addInvalidMessage(ctx, "telefono", "Ingrese una Telefono");


    }
}
