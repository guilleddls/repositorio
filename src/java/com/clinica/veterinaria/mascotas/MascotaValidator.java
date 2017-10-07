package com.clinica.veterinaria.mascotas;

import java.util.Date;
import java.util.Map;

import org.zkoss.bind.Property;
import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.validator.AbstractValidator;


public class MascotaValidator extends AbstractValidator {
       
       public static boolean validar(Mascota m){
           String mensaje = "";
           //mensaje = NullOrEmpty(m.getChip());
           //mensaje += (new MascotaDAO().existChip(m.getChip()))?"x":"";
           mensaje += NullOrEmpty(m.getNombre());
           mensaje += (m.getNacimiento() == null)? "X":"";
           mensaje += NullOrEmpty(m.getSexo());
           mensaje += m.getEspecie() == null ? "X":"";
           mensaje += m.getEspecie() == null ? "X":""; 
           System.out.println("Mensaje:" + mensaje);
           return mensaje.isEmpty();
           
       }
    
       public static String NullOrEmpty(String texto){
           if(texto == null || texto.isEmpty()){
               return "falso";
           }
           return "";
       }
       @Override
	public void validate(ValidationContext ctx) {
          

            Map<String,Property> beanProps = ctx.getProperties(ctx.getProperty().getBase());
            validarChip(ctx , (String)beanProps.get("chip").getValue());
            validarNombre(ctx , (String)beanProps.get("nombre").getValue());
            validarFecha(ctx , (Date)(beanProps.get("nacimiento").getValue()));
            validarSexo(ctx , (String)beanProps.get("sexo").getValue());
            validarEspecie(ctx , beanProps.get("especie").getValue());
            validarRaza(ctx, beanProps.get("raza").getValue());
            
	}
        
        private void validarChip(ValidationContext ctx, String chip){
            //System.out.println("Chip: "+ chip);
            //if(chip == null || chip.isEmpty()) addInvalidMessage(ctx, "chip", "Debes ingresar un nombre");
            if(chip != null && !chip.isEmpty()){ 
                    if(new MascotaDAO().existChip(chip)){
                    addInvalidMessage(ctx, "chip", "El código ya existe");
                    }
            }
        }
        
        
        private void validarNombre(ValidationContext ctx, String nombre){
            if(nombre == null || nombre.isEmpty())
                addInvalidMessage(ctx, "nombre", "Debes ingresar un nombre");            
        }
        
        private void validarFecha(ValidationContext ctx, Date fecha){
            
            if(fecha == null )
                addInvalidMessage(ctx, "nacimiento", "Debes ingresar una fecha");
            //else if(!esDate(fecha))                addInvalidMessage(ctx, "nacimiento", "Formato invalido de fecha");
        }
        
        private void validarSexo(ValidationContext ctx, String sexo){
            if(sexo == null || sexo.isEmpty())
                addInvalidMessage(ctx, "sexo", "Debes seleccionar un sexo");
        }
        
        private void validarEspecie(ValidationContext ctx, Object o){
            if(o == null )
                addInvalidMessage(ctx, "especie", "Debes seleccionar una especie");
        }
        
        private void validarRaza(ValidationContext ctx, Object o){
            if(o == null )
                addInvalidMessage(ctx, "raza", "Debes seleccionar una raza");
        }
        
        
    
}
