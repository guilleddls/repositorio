package com.clinica.veterinaria.util;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Path;
import org.zkoss.zul.Div;

/**
 *
 * 
 */
public class NewMain {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
      /*  Cliente guille = new Cliente();
        guille.setNombre("Guillermo");
        guille.setUsuario("Guille");
        guille.setPassword(GeneradorPass.getNext());
        guille.setEmail("guillermodiyenno@gmail.com");
        
        Mail.mailPassword(guille);*/
        String asd = String.valueOf(null);
        System.out.println(asd);
        
        //Borderlayout bl = (Borderlayout) Path.getComponent("/main");
        Div bloque = (Div) Path.getComponent("/contenido");
        //Center center = bl.getCenter();
        //center.getChildren().clear();
        bloque.getChildren().clear();
        Executions.createComponents("/estadisticas/estadisticas.zul", bloque, null);
    }
    
}
