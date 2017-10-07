package com.clinica.veterinaria.inicio;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Bandbox;

/**
 *
 * 
 */
public class Busqueda extends GenericForwardComposer {
    @Wire
    private Bandbox busqueda;
    
    @Listen("onOpen=#busqueda")
    public void abrir(){
        Executions.getCurrent().sendRedirect("https://www.google.com.ar/#q="+busqueda.getValue(),"_blank"); 
    }
    @Listen("onOk=#busqueda")
    public void ok(){
        Executions.getCurrent().sendRedirect("https://www.google.com.ar/#q="+busqueda.getValue(),"_blank"); 
    }
}
