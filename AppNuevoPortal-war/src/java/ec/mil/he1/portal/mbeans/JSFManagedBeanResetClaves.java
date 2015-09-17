/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.mil.he1.portal.mbeans;

import he1.seguridades.entities.SegUrls;
import he1.seguridades.entities.SegUsuario;
import he1.seguridades.sessions.SegUrlsFacade;
import he1.seguridades.sessions.SegUsuarioFacade;
import he1.utilities.SesionSeguridades;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.servlet.http.HttpSession;

/**
 *
 * @author christian_ruiz
 */
@ManagedBean(name = "reiniciaClave")
@SessionScoped
public class JSFManagedBeanResetClaves implements Serializable {
    
    @EJB
    private SesionSeguridades sesionSeguridades;
    
    @EJB
    private SegUrlsFacade segUrlsFacade;
    
    private String cedularecuperaclave2 = "";
    @EJB
    private SegUsuarioFacade segUsuarioFacade;

    /**
     * Creates a new instance of JSFManagedBeanResetClaves
     */
    public JSFManagedBeanResetClaves() {
    }
    
    private String cuentacorreo = "";
    
    @PostConstruct
    private void init() {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            HttpSession session = (HttpSession) context.getExternalContext().getSession(true);
            session.invalidate();
        } catch (Exception e) {
            System.out.println("e.toString() = " + e.toString());
        }
    }
    
    public String resetClave() {
        try {
            
            cuentacorreo = "";
            SegUsuario findByCC = new SegUsuario();
            try {
                
                findByCC = segUsuarioFacade.findByCC(cedularecuperaclave2);
            } catch (Exception e) {
                addMessage("La cédula proporcionada no se encuentra registrada");
            }
            
            if (findByCC.getEmail() != null) {
                cuentacorreo = findByCC.getEmail();
                SegUrls findURL = segUrlsFacade.findURL("resetClave");
                String url = findURL.getUrl();
                String parametro = sesionSeguridades.encyrpDinamico(cedularecuperaclave2);
                String linkurl = "<h1><a href=\"" + url + "\">" + url + "?klisnu=" + parametro + "&amp;kmdtg=17</a></h1>";
                addMessage("Se ha enviado al correo: " + findByCC.getEmail() + " el link de reinicio de clave");
                segUsuarioFacade.p_envia_mail(findByCC.getEmail(), "HE1:   Reseteo de claves", "Usuario :" + findByCC.getCedulaLogin() + " su clave se restituira copiando el link siguiente y pegandolo en un nuevo navegador::" + linkurl, "1");
                
            } else {
                cuentacorreo = "(Sin registro de correo)";
                addMessage("Usted no tiene mail registrado");
            }
        } catch (Exception e) {
            addMessage("La cédula proporcionada no se encuentra registrada");
            
        }
        cedularecuperaclave2 = "";
        return "/Login/loginAdm";
    }

    /**
     * @return the cedularecuperaclave2
     */
    public String getCedularecuperaclave2() {
        return cedularecuperaclave2;
    }

    /**
     * @param cedularecuperaclave2 the cedularecuperaclave2 to set
     */
    public void setCedularecuperaclave2(String cedularecuperaclave2) {
        this.cedularecuperaclave2 = cedularecuperaclave2;
    }
    
    public void addMessage(String summary) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, null);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
    
    public void listen(AjaxBehaviorEvent event) {
        
    }

    /**
     * @return the cuentacorreo
     */
    public String getCuentacorreo() {
        return cuentacorreo;
    }

    /**
     * @param cuentacorreo the cuentacorreo to set
     */
    public void setCuentacorreo(String cuentacorreo) {
        this.cuentacorreo = cuentacorreo;
    }
    
}
