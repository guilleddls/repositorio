package com.clinica.veterinaria.user;

import java.util.HashMap;
import java.util.List;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Messagebox;

public class UserViewModel {
	
        //@Wire        private Textbox txtNueva;
        
	private final UserDAO eventDao = new UserDAO();
	User user = new User();
	private User selectedEvent, newEvent = new User();
	Session session = Sessions.getCurrent();
        
        @Init
        public void init () {
            if(UserCredentialManager.getIntance(session).isAuthenticated()) {
                user = UserCredentialManager.getIntance(session).getUser();
            }
            
        }
        
        
	public User getSelectedEvent() {
		return selectedEvent;
	}

	public void setSelectedEvent(User selectedEvent) {
		this.selectedEvent = selectedEvent;
	}
	
	public User getNewEvent() {
		return newEvent;
	}

	public void setNewEvent(User newEvent) {
		this.newEvent = newEvent;
	}

        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }

	public List<User> getEvents() {
		return eventDao.findAll();
	}
	
	@Command("add")
	@NotifyChange("events")
	public void add() {
		eventDao.insert(this.newEvent);
		this.newEvent = new User();
	}
	
	@Command("update")
	@NotifyChange("events")
	public void update() {
		eventDao.update(this.selectedEvent);
	}
	
        //Funcion para cambiar el usuario su propia contraseña
        @Command("changepass")
	public void changepass(String pass) {
                user.setPassword(pass);
                if(!eventDao.change(this.user)){
                Messagebox.show("No se ha modificado la contraseña", "Error", Messagebox.OK, Messagebox.ERROR);
            }
	}
        
	@Command
	@NotifyChange({"selectedEvent", "events"})
	public void delete() {
            if(selectedEvent.getTipo() == 1){
                Messagebox.show("No se puede eliminar un Administrador", "Warning", Messagebox.OK, Messagebox.EXCLAMATION);
            }
            else if(this.selectedEvent != null) { //Tipo 1)Admin, 2)Veterinario 3)Empleado
                Messagebox.show("¿Desea elimimar " + selectedEvent.getNombre() + " ?", 
                "Advertencia", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION,
                    new org.zkoss.zk.ui.event.EventListener(){
                        @Override
                        public void onEvent(Event e){
                            if(Messagebox.ON_OK.equals(e.getName())){
                                eventDao.delete(selectedEvent);
                                selectedEvent = null;
                                BindUtils.postGlobalCommand(null, null, "refrescar", null);
                            }
                        }
                    }
                );}        
            
            
        }
        
        @GlobalCommand
        @NotifyChange({"selectedEvent", "events"})
        public void refrescar(){
            
        }
        
        @Command("nuevoUsuario")
        @NotifyChange("events")
        public void nuevoUsuario() {
            //final HashMap<String, Object> map = new HashMap<>();            map.put("selectedUsuario", selectedEvent );
            Executions.createComponents("/users/user-nuevo.zul", null, null);
        }
        
        @Command("modificarUsuario")
        @NotifyChange("events")
        public void modificarUsuario() {
            final HashMap<String, Object> map = new HashMap<>();
            map.put("selectedUsuario", selectedEvent );
            Executions.createComponents("/users/user-modificar.zul", null, map);
        }
        
        @GlobalCommand
        @NotifyChange("events")
        public void refreshNewUsuario(@BindingParam("returnNewUsuario") User newuser) {
            if(!eventDao.insert(newuser)){
                Messagebox.show("No se ha añadido el usuario", "Error", Messagebox.OK, Messagebox.ERROR);
            }
            else
                Clients.showNotification("Usuario Agregado", Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 3000);
        }
        
        @GlobalCommand
        @NotifyChange({"selectedEvent", "events"})
        public void refreshUpdateUsuario(@BindingParam("returnUpdateUsuario") User user) {
            if(!eventDao.update(user)){
                Messagebox.show("No se ha modificado los datos del usuario", "Error", Messagebox.OK, Messagebox.ERROR);
            }
             else
                Clients.showNotification("Usuario Actualizado", Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 3000);
        }
       

}
