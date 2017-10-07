package com.clinica.veterinaria.clientes;

import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.validator.AbstractValidator;

public class ClienteValidator extends AbstractValidator {

    public static boolean validar(Cliente cliente){
        boolean valida;
        if(cliente != null){
        
            valida = (cliente.getNif() != null) 

            &&(!cliente.getNombre().isEmpty())

            && (!cliente.getApellidos().isEmpty())

            && (!cliente.getDireccion().isEmpty()) 

         

            && (cliente.getProvincia() != null) 

            && (cliente.getCiudad() != null) ;
        }
        else 
            valida = false;
        return  valida;
        
    }
    
        @Override
	public void validate(ValidationContext ctx) {
            
		String nif = String.valueOf(ctx.getProperties("nif")[0].getValue());
                String nombre = (String)ctx.getProperties("nombre")[0].getValue();
		String email = (String)ctx.getProperties("email")[0].getValue();
		String apellidos = (String)ctx.getProperties("apellidos")[0].getValue();
                String direccion = (String)ctx.getProperties("direccion")[0].getValue();
                Object provincia = ctx.getProperties("provincia")[0].getValue();
                Object poblacion = ctx.getProperties("ciudad")[0].getValue();
//                Integer telefono = (Integer)ctx.getProperties("telefono")[0].getValue();
		
                if(nif == null || nif.isEmpty() || nif.equals("__-________-_")) {
                    this.addInvalidMessage(ctx, "nif", "Debes introducir un CUIT");
                }
                
                if(nombre == null || nombre.isEmpty()) {
                    this.addInvalidMessage(ctx, "nombre", "Debes introducir un nombre");
                }
                
                if(apellidos == null || apellidos.isEmpty()) {
                    this.addInvalidMessage(ctx, "apellidos", "Debes introducir los apellidos");
                }
                
                if(direccion == null || direccion.isEmpty()) {
                    this.addInvalidMessage(ctx, "direccion", "Debes introducir la dirección");
                }
                
                if(!(email == null || email.isEmpty()) && !email.matches(".+@.+\\.[a-z]+")) {
                    this.addInvalidMessage(ctx, "email", "Email invalido ");
                }
                
                if(provincia == null) {
                    this.addInvalidMessage(ctx, "provincia", "Seleccione una provincia");
                }
                
                if(poblacion == null) {
                    this.addInvalidMessage(ctx, "poblacion", "Seleccione una ciudad");
                }
                
        
	}
}
