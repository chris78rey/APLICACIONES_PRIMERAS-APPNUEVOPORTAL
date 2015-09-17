/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.mil.he1.portal.mbeans;

import ec.mil.he1.portal.utils.DatosDinamicos;
import he1.seguridades.entities.SegModulos;
import he1.seguridades.sessions.SegModulosFacade;
import he1.utilities.SesionSeguridades;
import java.io.IOException;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.naming.NamingException;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.DefaultSubMenu;
import org.primefaces.model.menu.MenuModel;

/**
 *
 * @author christian_ruiz
 */
@ManagedBean(name = "msis")
@SessionScoped
public class JSFManagedBeanMenuSistemas implements Serializable {

    @EJB
    private SesionSeguridades sesionSeguridades;

    @EJB
    private SegModulosFacade segModulosFacade;

    @Resource(name = "DS_PORTAL")
    private DataSource DS_PORTAL;
    DatosDinamicos datosDinamicos;
    private String valor = "";

    /**
     * Creates a new instance of JSFManagedBeanMenuSistemas
     */
    public JSFManagedBeanMenuSistemas() {
        datosDinamicos = new DatosDinamicos();
    }

    private MenuModel model;

    /**
     *
     * @throws NamingException
     * @throws SQLException
     */
    @PostConstruct
    public void init() {

    }

    public MenuModel getModel() {

//        System.out.println("segModulos.getModId().toString() = " + segModulos.getModId().toString());
        Connection con = null;
        String URL = "";
        String usuarioBaseDatos = "";
        String moduloPAcientes = "";

        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(true);
        String usuarioEncriptado = (String) session.getAttribute("usuarioEncriptado");

//        SELECT UMO_ID, MOD_NOMBRE_MODULO, PER_DESCRIPCION FROM SEG_USUARIO_PERFIL, SEG_MODULOS, SEG_PERFIL WHERE CODIGO_PERSONAL = 'T043' AND SEG_MODULOS.MOD_ID = SEG_PERFIL.MOD_ID AND SEG_PERFIL.PER_ID = SEG_USUARIO_PERFIL.PER_ID ORDER BY MOD_NOMBRE_MODULO, PER_DESCRIPCION
        try {
            //se obtiene el codigo del personal
            String usuariodEncriptado = datosDinamicos.decryptDinamico(usuarioEncriptado);
            System.out.println("usuariodEncriptado = " + usuariodEncriptado);
            String sql = "";

            sql = "SELECT SEG_USUARIO_PERFIL.UMO_ID,"
                    + "       SEG_MODULOS.MOD_NOMBRE_MODULO,"
                    + "       SEG_PERFIL.PER_DESCRIPCION,"
                    + "       SEG_MODULOS.MOD_URL, '' usuario"
                    + "  FROM SEGURIDADES.SEG_USUARIO,"
                    + "       SEGURIDADES.SEG_PERFIL,"
                    + "       SEGURIDADES.SEG_USUARIO_PERFIL,"
                    + "       SEGURIDADES.SEG_MODULOS"
                    + " WHERE     SEG_USUARIO.CEDULA_LOGIN = '1712730132'"
                    + "       AND SEG_PERFIL.PER_ID = SEG_USUARIO_PERFIL.PER_ID"
                    + "       AND SEG_USUARIO.ID = SEG_USUARIO_PERFIL.USU_ID"
                    + "       AND SEG_MODULOS.MOD_ID = SEG_PERFIL.MOD_ID";

            con = DS_PORTAL.getConnection();
            ResultSet rs = con.prepareStatement(sql).executeQuery();
            model = new DefaultMenuModel();
            String nmodulo = "";
            String perfilusuario = "";

            DefaultSubMenu firstSubmenu = null;
            while (rs.next()) {
                moduloPAcientes = rs.getString("MOD_NOMBRE_MODULO");
                usuarioBaseDatos = rs.getString("USUARIO");
                //First submenu
                URL = rs.getString("MOD_URL");
                //se calcula el ide del usuperfil para enviarlo encriptado

                perfilusuario = datosDinamicos.encripta(rs.getString("UMO_ID"));
                usuarioBaseDatos = datosDinamicos.encripta(usuarioBaseDatos);
                //
                if (!nmodulo.equalsIgnoreCase(moduloPAcientes)) {
                    firstSubmenu = new DefaultSubMenu(moduloPAcientes);
                    model.addElement(firstSubmenu);
                }
                DefaultMenuItem item = new DefaultMenuItem(rs.getString("PER_DESCRIPCION"));
                //se envia el perfil y el codigo del personal encriptados
                item.setUrl(URL + "?xphsumf=" + usuarioEncriptado + "&mdtydbcpyrt=" + perfilusuario + "&mnflot=" + usuarioBaseDatos);
                item.setIcon("ui-icon-home");
                firstSubmenu.addElement(item);

                nmodulo = rs.getString("MOD_NOMBRE_MODULO");
            }

            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(JSFManagedBeanMenuSistemas.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NamingException ex) {
            Logger.getLogger(JSFManagedBeanMenuSistemas.class.getName()).log(Level.SEVERE, null, ex);
        }

        return model;
    }

    public void addMessage(String summary, String detail) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, detail);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    private SegModulos segModulos = new SegModulos();

    private HashMap< String, String> cities = new HashMap<String, String>();

    /**
     * @return the segModulos
     */
    public SegModulos getSegModulos() {
        return segModulos;
    }

    /**
     * @param segModulos the segModulos to set
     */
    public void setSegModulos(SegModulos segModulos) {
        System.out.println("segModulos = " + segModulos);
        this.segModulos = segModulos;
    }

    /**
     * @return the cities
     */
    public HashMap<String, String> getCities() {
        cities = new HashMap<String, String>();
        List<SegModulos> segModuloses = segModulosFacade.findAll();
        Iterator<SegModulos> iterator = segModuloses.iterator();

        while (iterator.hasNext()) {
            SegModulos next = iterator.next();
            cities.put(next.getModNombreModulo(), next.getModId().toPlainString());

        }

        return cities;
    }

    /**
     * @param cities the cities to set
     */
    public void setCities(HashMap<String, String> cities) {
        this.cities = cities;
    }

    public void listen1(AjaxBehaviorEvent event) {

    }

    /**
     * @return the valor
     */
    public String getValor() {

        return valor;
    }

    /**
     * @param valor the valor to set
     */
    public void setValor(String valor) {
        System.out.println("valor = " + valor);
        this.valor = valor;
    }

    public String logout() throws IOException {
        try {

            FacesContext context = FacesContext.getCurrentInstance();
            HttpSession session = (HttpSession) context.getExternalContext().getSession(true);
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
            ec.redirect(sesionSeguridades.getPresionarSalir());
        } catch (Exception e) {
        }
        return "";
    }
}
