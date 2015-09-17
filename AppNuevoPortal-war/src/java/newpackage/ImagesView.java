/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package newpackage;

import ec.mil.he1.portal.LoginJSFManagedBean;
import he1.seguridades.entities.SegUrls;
import he1.seguridades.entities.nuevos.VUsuariosClasif;
import he1.seguridades.sessions.SegModulosFacade;
import he1.seguridades.sessions.SegUrlsFacade;
import he1.seguridades.sessions.SegUsuarioFacade;
import he1.utilities.SesionSeguridades;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpSession;

/**
 *
 * @author christian_ruiz
 */
@ManagedBean
@ViewScoped
public class ImagesView  implements Serializable{

    @EJB
    private SesionSeguridades encryptFacade;

    @EJB
    private SegModulosFacade segModulosFacade;

    @EJB
    private SegUsuarioFacade segUsuarioFacade;

    @EJB
    private SegUrlsFacade segUrlsFacade;

    private List<String> images;
    private int tamanio = 0;

    private String url = "";
    private Object segModulos;
    private List data = new ArrayList<HashMap>();
    //aca se crea una variable para poder enviar en el URL de manera encritada el id de la tabla segusuario
    private String usuId = "";

    @PostConstruct
    public void init() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(true);
        LoginJSFManagedBean login;
        login = (LoginJSFManagedBean) session.getAttribute("login");
        VUsuariosClasif findByCedulaLogin = login.getFindByCedulaLogin();

        //aca se debe encryptar el usu_del seg_usuario
        BigDecimal id = findByCedulaLogin.getId();

        usuId = encryptFacade.encyrpDinamico(id.toString());

    }

    public List<String> getImages() {
        return images;
    }

    /**
     * @return the url
     */
    public String getUrl() {
        return url;
    }

    /**
     * @param url the url to set
     */
    public void setUrl(String url) {
        this.url = url;
    }

    public String listen(String img) {
        System.out.println("this = " + img);
        return null;
    }

    /**
     * @return the data
     */
    //esto se coloca en el Managed Bean  
    List<Map> listabuscaautocomplete = new ArrayList<>();

    public List<Map> getListaMenuHabilita() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(true);
        LoginJSFManagedBean login;
        login = (LoginJSFManagedBean) session.getAttribute("login");
        VUsuariosClasif findByCedulaLogin = login.getFindByCedulaLogin();

        List data = new ArrayList<HashMap>();
        data = segModulosFacade.listaMenuHabilita(findByCedulaLogin.getId().toString());
        return data;
    }

    public List getData() throws NamingException, SQLException {
        data = getListaMenuHabilita();
        return data;
    }

    /**
     * @param data the data to set
     */
    public void setData(List data) {
        this.data = data;
    }

    /**
     * @return the usuId
     */
    public String getUsuId() {
        return usuId;
    }

    /**
     * @param usuId the usuId to set
     */
    public void setUsuId(String usuId) {
        this.usuId = usuId;
    }

    /**
     * @return the tamanio
     * @throws javax.naming.NamingException
     * @throws java.sql.SQLException
     */
    public int getTamanio() throws NamingException, SQLException {
        List data1 = getData();
        tamanio = data1.size();
        return tamanio;
    }

    /**
     * @param tamanio the tamanio to set
     */
    public void setTamanio(int tamanio) {
        this.tamanio = tamanio;
    }

}
