package org.semanticwb.security.auth;

import java.io.IOException;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.TextInputCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.semanticwb.Logger;
import org.semanticwb.SWBPlatform;
import org.semanticwb.SWBUtils;
import org.semanticwb.model.SWBContext;
import org.semanticwb.servlet.internal.DistributorParams;

/**
 * CallbackHandler for login password credentials
 * CallbackHandler para credenciales login password
 * @author Sergio Martínez  sergio.martinez@acm.org
 */
public class SWB4CallbackHandlerLoginPasswordImp extends SWB4CallbackHandler {
private static Logger log = SWBUtils.getLogger(SWB4CallbackHandlerLoginPasswordImp.class);
    private HttpServletRequest request;
    private HttpServletResponse response;
    private String authType;
    private DistributorParams dparams;

    public SWB4CallbackHandlerLoginPasswordImp() {
        this.request = null;
        this.response = null;
        this.dparams = null;
        this.authType = (String) SWBPlatform.getServletContext().getAttribute("authType");
    }

    public SWB4CallbackHandlerLoginPasswordImp(HttpServletRequest request, HttpServletResponse response, String authType, DistributorParams dparams) {
        this.request = request;
        this.response = response;
        this.authType = authType;
        this.dparams = dparams;
    }

    /**
     * Manejo de los parámetros para enviarlos al JAAS
     * parameter management as required by JAAS
     * @param callbacks
     * @throws java.io.IOException
     * @throws javax.security.auth.callback.UnsupportedCallbackException
     */
    public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
        log.trace("Tipo de Autenticacion: " + authType);
        if ("BASIC".equalsIgnoreCase(authType)) {
            getBasicCredentials(callbacks);
        }
        if ("FORM".equalsIgnoreCase(authType)) {
            getFormCredentials(callbacks);
        }
    }

    private void getBasicCredentials(Callback[] callbacks) {
        String login = "";
        String password = "";
        String header = request.getHeader("Authorization");
        if (null != header && !"".equals(header)) {
            header = header.substring(6).trim();
            String[] datos = SWBUtils.TEXT.decodeBase64(header).split(":");
            try {
                login = datos[0].trim();
                password = datos[1].trim();
            } catch (Exception ie) {
            } //Ignored Exception, asume no login - password

        }
        for (int i = 0; i < callbacks.length; i++) {
            if (callbacks[i] instanceof NameCallback) {
                NameCallback nameCallback = (NameCallback) callbacks[i];
                nameCallback.setName(login);

            } else if (callbacks[i] instanceof PasswordCallback) {
                PasswordCallback passwordCallback = (PasswordCallback) callbacks[i];
                passwordCallback.setPassword(password.toCharArray());
            } else if (callbacks[i] instanceof TextInputCallback) {
                TextInputCallback textInputCallback = (TextInputCallback) callbacks[i];
                if (null != dparams) {
                    textInputCallback.setText(dparams.getWebPage().getWebSiteId());
                } else {
                    textInputCallback.setText(SWBContext.getGlobalWebSite().getId());
                }
            }
        }
    }

    private void getFormCredentials(Callback[] callbacks) {
        String login = request.getParameter("wb_username");
        String password = request.getParameter("wb_password");
        for (int i = 0; i < callbacks.length; i++) {
            if (callbacks[i] instanceof NameCallback) {
                NameCallback nameCallback = (NameCallback) callbacks[i];
                nameCallback.setName(login);

            } else if (callbacks[i] instanceof PasswordCallback) {
                PasswordCallback passwordCallback = (PasswordCallback) callbacks[i];
                passwordCallback.setPassword(password == null ? null : password.toCharArray());
            } else if (callbacks[i] instanceof TextInputCallback) {
                TextInputCallback textInputCallback = (TextInputCallback) callbacks[i];
                textInputCallback.setText(dparams.getWebPage().getWebSiteId());
            }
        }
    }
}
