package com.clinica.veterinaria.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * 
 */
public class Fecha {
    public final static SimpleDateFormat dateFormato = new SimpleDateFormat("yyyy-MM-dd");
    
    public static Date toDate(String fecha){       
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");          
        try {
            return formato.parse(fecha);            
        } catch (ParseException ex) {
            return null;
        }
            
        
    }
    
    public static Date toDate(String fecha, String frm){       
        SimpleDateFormat formato = new SimpleDateFormat(frm);          
        try {
            return formato.parse(fecha);            
        } catch (ParseException ex) {
            return null;
        }
            
        
    }
    
    public static String toDateFormat(Date fecha, String frm){       
        
        return new SimpleDateFormat(frm).format((Date)fecha);
            
        
    }
}
