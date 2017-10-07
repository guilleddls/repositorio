package com.clinica.veterinaria.util;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zkmax.zul.Navbar;
import org.zkoss.zkmax.zul.Navitem;
import org.zkoss.zul.A;
import org.zkoss.zul.Div;
import org.zkoss.zul.Hlayout;

public class SidebarComposer extends SelectorComposer<Component> {
	
	@Wire
	Hlayout main;
	@Wire
	Div sidebar;
	@Wire
	Navbar navbar;
	@Wire
	Navitem calitem;
	@Wire
	A toggler;
	
	
        @Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
	}
	
        @Listen("onClick = #ivaVenta")
        public void ivaVenta(){
            redireccionarIva(1);
        }
        
        @Listen("onClick = #ivaCompra")
        public void ivaCompra(){
            redireccionarIva(0);
        }
        
        public void redireccionarIva(int venta){    
            Div bloque = (Div) Path.getComponent("/contenido");           
            bloque.getChildren().clear();                
            final java.util.HashMap<String, Object> map = new java.util.HashMap<>();
            map.put("venta", venta ); 
            Executions.createComponents("/ivas/iva.zul", bloque, map);
 
        }
        
	// Toggle sidebar to collapse or expand
	@Listen("onClick = #toggler")
	public void toggleSidebarCollapsed() {
		if (navbar.isCollapsed()) {
			sidebar.setSclass("sidebar");
			navbar.setCollapsed(false);
			calitem.setTooltip("calpp, position=end_center, delay=0");
			toggler.setIconSclass("z-icon-angle-double-left");
		} else {
			sidebar.setSclass("sidebar sidebar-min");
			navbar.setCollapsed(true);
			calitem.setTooltip("");
			toggler.setIconSclass("z-icon-angle-double-right");
		}
		// Force the hlayout contains sidebar to recalculate its size
		Clients.resize(sidebar.getRoot().query("#main"));
	}
}