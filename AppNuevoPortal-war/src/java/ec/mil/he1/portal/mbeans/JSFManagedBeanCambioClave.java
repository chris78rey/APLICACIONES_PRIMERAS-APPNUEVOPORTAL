/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.mil.he1.portal.mbeans;

import ec.mil.he1.portal.LoginJSFManagedBean;
import he1.seguridades.entities.SegUsuario;
import he1.seguridades.entities.nuevos.VUsuariosClasif;
import he1.seguridades.sessions.SegUsuarioFacade;
import he1.utilities.SesionSeguridades;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeSupport;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

/**
 *
 * @author christian_ruiz
 */
@ManagedBean(name = "cclave")
@ViewScoped
public class JSFManagedBeanCambioClave implements Serializable {

    private String cedularecuperaclave = "";
    @EJB
    private SegUsuarioFacade segUsuarioFacade;

    private String correo1 = "";
    private String correo2 = "";

    @EJB
    private SesionSeguridades sesionSeguridades;

    public static final String PROP_CLAVE = "PROP_CLAVE";
    public static final String PROP_CLAVENUEVA = "PROP_CLAVENUEVA";
    public static final String PROP_CLAVENUEVA2 = "PROP_CLAVENUEVA2";

    private String clave = "";
    private String clavenueva = "";
    private String clavenueva2 = "";
    private final transient PropertyChangeSupport propertyChangeSupport = new java.beans.PropertyChangeSupport(this);
    private final transient VetoableChangeSupport vetoableChangeSupport = new java.beans.VetoableChangeSupport(this);

    VUsuariosClasif findByCedulaLogin = new VUsuariosClasif();

    /**
     * Creates a new instance of JSFManagedBeanCambioClave
     */
    public JSFManagedBeanCambioClave() {

    }

    @PostConstruct
    private void init() {
        try {

            FacesContext facesContext = FacesContext.getCurrentInstance();
            HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(true);
            LoginJSFManagedBean login = (LoginJSFManagedBean) session.getAttribute("login");

            //aca se encuentran los datos de las personas
            findByCedulaLogin = login.getFindByCedulaLogin();
        } catch (Exception e) {
        }

    }

    /**
     * @return the clave
     */
    public String getClave() {
        return clave;
    }

    /**
     * @param clave the clave to set
     */
    public void setClave(String clave) throws PropertyVetoException {
        java.lang.String oldClave = this.clave;
        vetoableChangeSupport.fireVetoableChange(PROP_CLAVE, oldClave, clave);
        this.clave = clave;
        propertyChangeSupport.firePropertyChange(PROP_CLAVE, oldClave, clave);
    }

    /**
     * @return the clavenueva
     */
    public String getClavenueva() {
        return clavenueva;
    }

    /**
     * @param clavenueva the clavenueva to set
     */
    public void setClavenueva(String clavenueva) throws PropertyVetoException {
        java.lang.String oldClavenueva = this.clavenueva;
        vetoableChangeSupport.fireVetoableChange(PROP_CLAVENUEVA, oldClavenueva, clavenueva);
        this.clavenueva = clavenueva;
        propertyChangeSupport.firePropertyChange(PROP_CLAVENUEVA, oldClavenueva, clavenueva);
    }

    /**
     * @return the clavenueva2
     */
    public String getClavenueva2() {
        return clavenueva2;
    }

    /**
     * @param clavenueva2 the clavenueva2 to set
     */
    public void setClavenueva2(String clavenueva2) throws PropertyVetoException {
        java.lang.String oldClavenueva2 = this.clavenueva2;
        vetoableChangeSupport.fireVetoableChange(PROP_CLAVENUEVA2, oldClavenueva2, clavenueva2);
        this.clavenueva2 = clavenueva2;
        propertyChangeSupport.firePropertyChange(PROP_CLAVENUEVA2, oldClavenueva2, clavenueva2);
    }

    public void cambiarClave() {
//        System.out.println("findByCedulaLogin.getCedulaLogin()  void cambiarClave()  void cambiarClave() = " + findByCedulaLogin.getCedulaLogin());
        String pEstado = "";

        if (clavenueva.length() < 5) {
            addMessage("Las claves deben tener una longitud mínima de 5 letras", "");
            return;
        }

        if (clavenueva2.equalsIgnoreCase(clavenueva)) {

            if (!findByCedulaLogin.getCedulaLogin().equalsIgnoreCase(clavenueva)) {
                pEstado = sesionSeguridades.pCambiarClavePaciente(findByCedulaLogin.getCedulaLogin(), clave, clavenueva);
                if (pEstado.equalsIgnoreCase("1")) {
                    addMessage("Contraseña cambiada satisfactoriamente", "");
                    if (correo1.equalsIgnoreCase(correo2) && correo2.length() > 0) {
                        try {
                            SegUsuario findSegUsuario = segUsuarioFacade.find(findByCedulaLogin.getId());
                            findSegUsuario.setEmail(correo1);
                            segUsuarioFacade.edit(findSegUsuario);
                        } catch (Exception e) {
                        }

                    }
                }
            } else {
                addMessage("Las claves anterior y nueva no deben coincidir", "");
            }

        } else {
            //en el caso de que no coincidan las claves
            pEstado = "-1";
            addMessage("Las nuevas claves no coinciden", "");
        }

    }

    public void addMessage(String summary, String detail) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, detail);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public String loginRedirect() {
//        return "/Login/loginAdm";
        return null;
    }

    /**
     * @return the correo1
     */
    public String getCorreo1() {
        return correo1;
    }

    /**
     * @param correo1 the correo1 to set
     */
    public void setCorreo1(String correo1) {
        this.correo1 = correo1;
    }

    /**
     * @return the correo2
     */
    public String getCorreo2() {
        return correo2;
    }

    /**
     * @param correo2 the correo2 to set
     */
    public void setCorreo2(String correo2) {
        this.correo2 = correo2;
    }

    /**
     * @return the cedularecuperaclave
     */
    public String getCedularecuperaclave() {
        return cedularecuperaclave;
    }

    /**
     * @param cedularecuperaclave the cedularecuperaclave to set
     */
    public void setCedularecuperaclave(String cedularecuperaclave) {
        this.cedularecuperaclave = cedularecuperaclave;
    }
}
