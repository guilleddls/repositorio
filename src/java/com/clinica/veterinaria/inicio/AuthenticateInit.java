package com.clinica.veterinaria.inicio;

import com.clinica.veterinaria.user.UserCredentialManager;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.Sessions;

public class AuthenticateInit extends org.zkoss.zk.ui.util.GenericInitiator {
    @Override
    public void doInit(Page page, Map args) throws Exception {
        if(!UserCredentialManager.getIntance(Sessions.getCurrent()).isAuthenticated()) {
            Execution exec = Executions.getCurrent();
            HttpServletResponse response = (HttpServletResponse)exec.getNativeResponse();
            // /veterinaria
            response.sendRedirect(response.encodeRedirectURL("/")); //assume there is /login
            exec.setVoided(true); //no need to create UI since redirect will take place
        }
    }
}
