
package com.clinica.veterinaria.citas;

import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.validator.AbstractValidator;

/**
 *
 * 
 */
public class CitaValidator extends AbstractValidator {

        public static boolean validar(Cita c){
           String mensaje;
           mensaje = NullOrEmpty(c.getHora());
           mensaje += NullOrEmpty(c.getFecha().toString());

           mensaje += c.getServicio() == null ? "X":"";
           mensaje += c.getMascota() == null ? "X":"";                               
           return mensaje.isEmpty();
           
       }
    
       private static String NullOrEmpty(String texto){
           if(texto == null || texto.isEmpty()){
               return "falso";
           }
           return "";
       }
	public void validate(ValidationContext ctx) {
                    
           try{
            Object mascota = ctx.getProperties("mascota")[0].getValue();
		if(mascota == null)
			this.addInvalidMessage(ctx, "mascota", "Selecciona una Mascota");
           }catch(Exception e){
                    
                    }
            
                String fecha = String.valueOf(ctx.getProperties("fecha")[0].getValue());
		if(fecha == null || "".equals(fecha))
			this.addInvalidMessage(ctx, "fecha", "Ingresa una fecha");		
		
                Object hora = ctx.getProperties("hora")[0].getValue();
                if(hora == null)
			this.addInvalidMessage(ctx, "hora", "Debes introducir una hora");
                
                Object servicio = ctx.getProperties("servicio")[0].getValue();
                if(servicio == null)
			this.addInvalidMessage(ctx, "servicio", "Selecciona un Servicio");
		//if(ctx.getProperties("fecha_alta")[0].getValue() == null)
		//	this.addInvalidMessage(ctx, "fecha_alta", "You must specify a date");
		
		//if(priority == null || priority < 1 || priority > 10)
		//	this.addInvalidMessage(ctx, "priority", "You must give a priority > 0 && < 10");
	}
}
