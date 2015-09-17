/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.mil.he1.portal;

import he1.seguridades.entities.SegUrls;
import he1.seguridades.entities.nuevos.VUsuariosClasif;
import he1.seguridades.sessions.SegUrlsFacade;
import he1.seguridades.sessions.SegUsuarioFacade;
import he1.seguridades.sessions.VUsuariosClasifFacade;
import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.naming.NamingException;
import javax.servlet.http.HttpSession;

/**
 *
 * @author christian_ruiz
 */
@ManagedBean(name = "login")
@SessionScoped
public class LoginJSFManagedBean implements Serializable {

    @EJB
    private SegUrlsFacade segUrlsFacade;

    private String existeFoto = "0";

    @EJB
    private VUsuariosClasifFacade vUsuariosClasifFacade;
    private VUsuariosClasif findByCedulaLogin = new VUsuariosClasif();

    @EJB
    private SegUsuarioFacade segUsuarioFacade;

    private String usuario = "";
    private String password1 = "";
    private String usuarioEncriptado = "";
    private String existefoto = "0";
    private String correctoLogin = "0";

    private String urlFoto = "";

    @PostConstruct
    private void init() {

    }

    public String getUsuarioEncriptado() {
        return usuarioEncriptado;
    }

    public void setUsuarioEncriptado(String usuarioEncriptado) {
        this.usuarioEncriptado = usuarioEncriptado;
    }

    String urlSiguiente = "";

    public String getUrlSiguiente() {
        return urlSiguiente;
    }

    public void setUrlSiguiente(String urlSiguiente) {
        this.urlSiguiente = urlSiguiente;
    }

    /**
     * Creates a new instance of LoginJSFManagedBean
     */
    public LoginJSFManagedBean() {

    }

    public List<String> completeText(String query) {
        List<String> results = new ArrayList<String>();
//        for (int i = 0; i < 10; i++) {
//            results.add(query + i);
//        }

        return results;
    }

    public void buttonAction(ActionEvent actionEvent) throws SQLException {
        try {

            urlSiguiente = "";
        } catch (Exception e) {
            System.out.println("e = " + e.getLocalizedMessage());
        }

    }

    public void buttonActionPersonal(ActionEvent actionEvent) throws SQLException {


        try {

//            String myContextParam =
//        request.getSession()
//               .getServletContext()
//               .getInitParameter("myParam");
            urlSiguiente = "";
            //si retorna 1 debe ingresar
            //si debuelve 1 esta ok
            if (segUsuarioFacade.pLogin(usuario, password1).equalsIgnoreCase("1")) {
                urlSiguiente = "/faces/Login/TemplateClientSelOpcion";
                correctoLogin = "1";
                //llamada a la función de encrypcion de usuario
//                addMessage("OK");
                findByCedulaLogin = vUsuariosClasifFacade.findByCedulaLogin(usuario);
                //
                try {
                    List<Map> listabuscaautocomplete = new ArrayList<>();
                    existefoto = vUsuariosClasifFacade.findExisteFoto(findByCedulaLogin.getId().toString());
                } catch (Exception e) {
                }

            } else if (segUsuarioFacade.pLogin(usuario, password1).equalsIgnoreCase("0")) {
                urlSiguiente = "";
                addMessage("Usuario o Clave Mal Ingresados");
            } else {
                if (segUsuarioFacade.pLogin(usuario, password1).equalsIgnoreCase("-1")) {
                    addMessage("El usuario no tiene perfil asignado, comuniquese con DTIC");
                }
            }
            if (segUsuarioFacade.pLogin(usuario, password1).equalsIgnoreCase("-2")) {
                addMessage("El usuario tiene cedulas diferentes entre los sistemas de nómina, sgh y pacientes");
            }

        } catch (Exception e) {
            System.out.println("e = " + e.getLocalizedMessage());
        }

    }

    public void addMessage(String summary) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, null);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    /**
     * @return the password1
     */
    public String getPassword1() {
        return password1;
    }

    /**
     * @param password1 the password1 to set
     */
    public void setPassword1(String password1) {
        this.password1 = password1;
    }

    public String accionIngreso() throws IOException, NamingException, SQLException {
//
//        if (urlSiguiente.equalsIgnoreCase("/Login/portal.xhtml")) {
//            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
//            externalContext.redirect(datosDinamicos.getModuloPAcientes() + usuarioEncriptado);
//        }
        return "/faces/welcomePrimefaces";
    }

    public String accionIngresoPersonal() throws IOException, NamingException, SQLException {

//        usuario, password1
        if (usuario.equalsIgnoreCase(password1) && correctoLogin.equalsIgnoreCase("1")) {
            return "/faces/Login/TemplateClientCambioClave";
        }
        return urlSiguiente;

//        return "";
    }

    /**
     * @return the findByCedulaLogin
     */
    public VUsuariosClasif getFindByCedulaLogin() {
        return findByCedulaLogin;
    }

    /**
     * @param findByCedulaLogin the findByCedulaLogin to set
     */
    public void setFindByCedulaLogin(VUsuariosClasif findByCedulaLogin) {
        this.findByCedulaLogin = findByCedulaLogin;
    }

    public String logout() throws IOException {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            HttpSession session = (HttpSession) context.getExternalContext().getSession(true);

            String initParameter = session.getServletContext().getInitParameter("Ambiente");
            System.out.println("*************************** = " + initParameter);

            Enumeration<String> attributeNames = session.getAttributeNames();
            String nextElement = "";
            while (attributeNames.hasMoreElements()) {
                try {
                    nextElement = attributeNames.nextElement();
                    session.removeAttribute(nextElement);
                } catch (Exception e) {
                }
            }
            session = null;
            ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();

            SegUrls findBySalir = segUrlsFacade.findBySalir("salirnp");
            ec.redirect(findBySalir.getUrl());
        } catch (Exception e) {
            System.out.println("e = " + e.getLocalizedMessage());
        }
        return "";
    }

    /**
     * @return the existeFoto
     */
    public String getExisteFoto() {
        return existeFoto;
    }

    /**
     * @param existeFoto the existeFoto to set
     */
    public void setExisteFoto(String existeFoto) {
        this.existeFoto = existeFoto;
    }

    /**
     * @return the existefoto
     */
    public String getExistefoto() {
        return existefoto;
    }

    /**
     * @param existefoto the existefoto to set
     */
    public void setExistefoto(String existefoto) {
        this.existefoto = existefoto;
    }

    /**
     * @return the urlFoto
     */
    public String getUrlFoto() {
        SegUrls segUrls;
        try {
            segUrls = segUrlsFacade.findURL("foto servlet");
            urlFoto = segUrls.getUrl();
        } catch (Exception e) {
        }
        return urlFoto;
    }

    /**
     * @param urlFoto the urlFoto to set
     */
    public void setUrlFoto(String urlFoto) {
        this.urlFoto = urlFoto;
    }
}
