/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.mil.he1.portal.mbeans;

import ec.mil.he1.portal.LoginJSFManagedBean;
import he1.seguridades.entities.SegUsuario;
import he1.seguridades.entities.nuevos.VProvCanParr;
import he1.seguridades.entities.nuevos.VUsuariosClasif;
import he1.seguridades.sessions.SegUsuarioFacade;
import he1.seguridades.sessions.VUsuariosClasifFacade;
import he1.sis.entities.Cantones;
import he1.sis.entities.CantonesPK;
import he1.sis.entities.Parroquias;
import he1.sis.entities.ParroquiasPK;
import he1.sis.entities.Provincias;
import he1.sis.sessions.CantonesFacade;
import he1.sis.sessions.ParroquiasFacade;
import he1.sis.sessions.ProvinciasFacade;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.servlet.http.HttpSession;

/**
 *
 * @author christian_ruiz
 */
@ManagedBean
@ViewScoped
public class JSFManagedBeanActualizaDAtos implements Serializable {
    
    @EJB
    private VUsuariosClasifFacade vUsuariosClasifFacade;
    private VUsuariosClasif findByCedulaLogin = new VUsuariosClasif();
    private String parr = "";
    @EJB
    private ParroquiasFacade parroquiasFacade;
    Parroquias parroquias = new Parroquias();
    private List<Parroquias> lparr = new ArrayList<Parroquias>();
    
    @EJB
    private ProvinciasFacade provinciasFacade;
    private Provincias provincias = new Provincias();
    private List<Provincias> provinciases = new ArrayList<>();
    
    @EJB
    private CantonesFacade cantonesFacade;
    Cantones cantones = new Cantones();
    private List<Cantones> lcantones = new ArrayList<Cantones>();
    

    @EJB
    private SegUsuarioFacade segUsuarioFacade;
    
    private SegUsuario segUsuario = new SegUsuario();
    private String can = "";
    
    @PostConstruct
    private void init() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(true);
        LoginJSFManagedBean login = (LoginJSFManagedBean) session.getAttribute("login");

        //aca se encuentran los datos de las personas
        findByCedulaLogin = login.getFindByCedulaLogin();

        //
        segUsuario = segUsuarioFacade.find(findByCedulaLogin.getId());
        provinciases = provinciasFacade.findProvinciasOrdenadas();
    }
    
    public void recuperaCantones() {
        provincias = provinciasFacade.find(provincias.getCodigo());
        lcantones = (cantonesFacade.findByProvincia(provincias));
    }
    Cantones canto = new Cantones();
    
    public void recuperaParroquias() {
        
        CantonesPK cantonesPK = new CantonesPK(provincias.getCodigo(), can);
        
        canto = cantonesFacade.find(cantonesPK);
        lparr = parroquiasFacade.findParrporCanton(canto);
    }
    
    public void listen1(AjaxBehaviorEvent event) {
        parr = "";
        recuperaCantones();
        
    }
    
    public void listen2(AjaxBehaviorEvent event) {
        lparr.clear();
        recuperaParroquias();
        
    }
    Parroquias p = new Parroquias();
    
    public void listen3(AjaxBehaviorEvent event) {
        ParroquiasPK parroquiasPK = new ParroquiasPK(provincias.getCodigo(), can, parr);
        parroquias = parroquiasFacade.find(parroquiasPK);
        System.out.println("parroquias.getParroquia() = " + parroquias.getParroquia());
    }

    /**
     * Creates a new instance of JSFManagedBeanActualizaDAtos
     */
    public JSFManagedBeanActualizaDAtos() {
    }

    /**
     * @return the segUsuario
     */
    public SegUsuario getSegUsuario() {
        return segUsuario;
    }

    /**
     * @param segUsuario the segUsuario to set
     */
    public void setSegUsuario(SegUsuario segUsuario) {
        this.segUsuario = segUsuario;
    }

    /**
     * @return the provinciases
     */
    public List<Provincias> getProvinciases() {
        return provinciases;
    }

    /**
     * @param provinciases the provinciases to set
     */
    public void setProvinciases(List<Provincias> provinciases) {
        this.provinciases = provinciases;
    }

    /**
     * @return the provincias
     */
    public Provincias getProvincias() {
        return provincias;
    }

    /**
     * @param provincias the provincias to set
     */
    public void setProvincias(Provincias provincias) {
        
        this.provincias = provincias;
    }

    /**
     * @return the lcantones
     */
    public List<Cantones> getLcantones() {
        return lcantones;
    }

    /**
     * @param lcantones the lcantones to set
     */
    public void setLcantones(List<Cantones> lcantones) {
        this.lcantones = lcantones;
    }

    /**
     * @return the lparr
     */
    public List<Parroquias> getLparr() {
        return lparr;
    }

    /**
     * @param lparr the lparr to set
     */
    public void setLparr(List<Parroquias> lparr) {
        this.lparr = lparr;
    }

    /**
     * @return the can
     */
    public String getCan() {
        return can;
    }

    /**
     * @param can the can to set
     */
    public void setCan(String can) {
        this.can = can;
    }

    /**
     * @return the parr
     */
    public String getParr() {
        return parr;
    }

    /**
     * @param parr the parr to set
     */
    public void setParr(String parr) {
        this.parr = parr;
    }
    
    public String cambiarPCP() {
        
        segUsuario.setParroquias(parroquias);
        segUsuarioFacade.edit(segUsuario);
        segUsuario = segUsuarioFacade.find(segUsuario.getId());
        return null;
        
    }
    
    public String cambiarDatos() {
        
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(true);
        LoginJSFManagedBean login = (LoginJSFManagedBean) session.getAttribute("login");
        //aca se encuentran los datos de las personas
        findByCedulaLogin = vUsuariosClasifFacade.findByCedulaLogin(segUsuario.getCedulaLogin());
        login.setFindByCedulaLogin(findByCedulaLogin);
        System.out.println("segUsuario.getDireccionDomicilio() = " + segUsuario.getDireccionDomicilio());
        segUsuarioFacade.edit(segUsuario);
        
        return null;
        
    }
    
}
