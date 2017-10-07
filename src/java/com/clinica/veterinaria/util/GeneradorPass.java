package com.clinica.veterinaria.util;

/**
 *
 * 
 */
public class GeneradorPass {
    public static final int MIN_LENGTH = 10;  
   
 
    protected static java.util.Random random = new java.util.Random();  
   
 
    protected static char[] goodChar = {   
        'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'j', 'k', 'm', 'n',  
        'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',  
        'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'J', 'K', 'M', 'N',  
        'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',  
        '2', '3', '4', '5', '6', '7', '8', '9',  
        '+', '-', '@',  
    };  
   

    public static String getNext() {  
        return getNext(MIN_LENGTH);  
    }  
   
 
    public static String getNext(int length) {  
        if (length < 1) {  
            throw new IllegalArgumentException(  
                    "Longitud muy pequeña " + length);  
        }  
        StringBuilder sb = new StringBuilder();  
        for (int i = 0; i < length; i++) {  
            sb.append(goodChar[random.nextInt(goodChar.length)]);  
        }  
        return sb.toString();  
    }  
}
