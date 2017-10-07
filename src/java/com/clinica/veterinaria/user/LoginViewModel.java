package com.clinica.veterinaria.user;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

/**
 *
 * 
 */
public class LoginViewModel extends GenericForwardComposer {//SelectorComposer<Window> {
    
    @Wire
    private Window loginWin;
    @Wire
    private Textbox nameTxb, passwordTxb;
     
    @Wire
    private Label mesgLbl;
    
    private boolean esperar = true;
    
    @Listen("onClick=#confirmBtn")
    public void confirm() {
        if(esperar){
        doLogin();
        esperar = false;
        }
    }
    
   
    public void onFocus$nameTxb(){
        nameTxb.setStyle(null);
        nameTxb.setErrorMessage(null);
    }
    
   
    public void onFocus$passwordTxb(){
        passwordTxb.setStyle(null);
        passwordTxb.setErrorMessage(null);
    }
    
    //ESTE NO SIRVE CREO ESTA EL OTRO!
    public void onClick$confirmBtn() {
		doLogin();
	}
    
    public void onOK() {
		doLogin();
	}
    
    public void onCancel() {
        nameTxb.setValue("");
        passwordTxb.setValue("");
        nameTxb.setFocus(true);
    }
   
    private void doLogin() {
        if(!validar()) return;
        
        UserCredentialManager usrLogin = UserCredentialManager.getIntance(Sessions.getCurrent());
        usrLogin.login(nameTxb.getValue(), passwordTxb.getValue());
        if (usrLogin.isAuthenticated()) {
                execution.sendRedirect("principal.zul");
        } else {
            esperar = true;
//                mesgLbl.setValue("Nombre de usuario o contraseña incorrecto");
        }
    }
    
    
    public void redireccionar(){
        if (UserCredentialManager.getIntance(session).isAuthenticated())                   
            execution.sendRedirect("principal.zul");
        else
            loginWin.setVisible(true);
    }
    
    
    
    @Override
    public void doAfterCompose(Component comp) throws Exception {
            super.doAfterCompose(comp);
            redireccionar();
            
            nameTxb.setFocus(true);
    }
    
    public boolean validar(){
        boolean valida = true;
        if(nameTxb.getValue().isEmpty()){
            nameTxb.setErrorMessage("Debe ingresar un usuario.");
             nameTxb.setStyle("-webkit-box-shadow: 0 1px 1px #f94a4a inset, 0 0 10px #f94a4a;  box-shadow: 0 1px 1px #f94a4a inset, 0 0 10px #f94a4a;  "
                    + "color: #070707;  border-color: #f20505;  outline: none;");
            valida = false;
        }
        if(passwordTxb.getValue().isEmpty()){
            passwordTxb.setErrorMessage("Debe ingresar una contraseña.");
            passwordTxb.setStyle("-webkit-box-shadow: 0 1px 1px #f94a4a inset, 0 0 10px #f94a4a;  box-shadow: 0 1px 1px #f94a4a inset, 0 0 10px #f94a4a;  "
                    + "color: #070707;  border-color: #f20505;  outline: none;");
            valida = false;
        }
        return valida;
    }
}
