package com.clinica.veterinaria.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * 
 */
public class Conversion {
    public static String getMes(int numero) {
    String descmes = "";
    switch (numero) {
        case 1: descmes = "ENERO";      break;
        case 2: descmes = "FEBRERO";    break;
        case 3: descmes = "MARZO";      break;
        case 4: descmes = "ABRIL";      break;
        case 5: descmes = "MAYO";       break;
        case 6: descmes = "JUNIO";      break;
        case 7: descmes = "JULIO";      break;
        case 8: descmes = "AGOSTO";     break;
        case 9: descmes = "SEPTIEMBRE"; break;
        case 10: descmes = "OCTUBRE";   break;
        case 11: descmes = "NOVIEMBRE"; break;
        case 12: descmes = "DICIEMBRE"; break;
    }
    return descmes;
    }
    
    private static final String regExp = "[\\x00-\\x20]*[+-]?(((((\\p{Digit}+)(\\.)?((\\p{Digit}+)?)([eE][+-]?(\\p{Digit}+))?)|(\\.((\\p{Digit}+))([eE][+-]?(\\p{Digit}+))?)|(((0[xX](\\p{XDigit}+)(\\.)?)|(0[xX](\\p{XDigit}+)?(\\.)(\\p{XDigit}+)))[pP][+-]?(\\p{Digit}+)))[fFdD]?))[\\x00-\\x20]*";
    private static final Pattern pattern = Pattern.compile(regExp);
    
    private boolean IsInt_Parse(String str){
        try{
            Integer.parseInt(str);
            return true;
        }
        catch(NumberFormatException e)  {
            return false;
        }
    }

    private boolean IsInt_Regex(String str){
        return str.matches("^-?\\d+$");
    }

    public static boolean IsInteger(String str){
        if (str == null)  return false;
        
        int length = str.length();
        if (length == 0) return false;
        
        int i = 0;
        if (str.charAt(0) == '-') {
            if (length == 1) return false;            
            i = 1;
        }
        for (; i < length; i++) {
            char c = str.charAt(i);
            if (c <= '/' || c >= ':') return false;            
        }
        return true;
    }
    
    public static int stringToInt(String text){
        return IsInteger(text)? Integer.parseInt(text): 0;
        
    }
    
    
    public static boolean isDouble(String str) {
        Matcher m = pattern.matcher(str);
        return m.matches();
    }
    
    public static boolean esDouble(String cadena) {
        if(cadena != null){
        try {
            Double.parseDouble(cadena);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
        }
        return false;
    }
    
    public static boolean esDate(String fecha){       
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");          
        try {
            formato.setLenient(false);
            formato.parse(fecha);
           return true;
        } catch (ParseException ex) {
            return false;
        }  
    }
    
    public static boolean esDate(String fecha, String frm){       
        SimpleDateFormat formato = new SimpleDateFormat(frm);          
        try {
            formato.setLenient(false);
            formato.parse(fecha);
           return true;
        } catch (ParseException ex) {
            return false;
        }  
    }
    
}
