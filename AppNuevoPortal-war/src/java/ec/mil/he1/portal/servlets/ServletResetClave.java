/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.mil.he1.portal.servlets;

import he1.seguridades.entities.SegUrls;
import he1.seguridades.sessions.SegUrlsFacade;
import he1.seguridades.sessions.SegUsuarioFacade;
import he1.utilities.SesionSeguridades;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author christian_ruiz
 */
@WebServlet(name = "ServletResetClave", urlPatterns = {"/kdDidmklo"})
public class ServletResetClave extends HttpServlet {
    
    @PersistenceContext(unitName = "AppNuevoPortal-warPU")
    private EntityManager em;
    @Resource
    private javax.transaction.UserTransaction utx;
    
    @EJB
    private SesionSeguridades sesionSeguridades;
    
    @EJB
    private SegUrlsFacade segUrlsFacade;

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        try {
            String cedulaencr = request.getParameter("klisnu");
            
            String ceduladecrypt = sesionSeguridades.decyrpDinamico(cedulaencr);
            System.out.println("cedulaencr 777777777777777777777777777777 = " + ceduladecrypt);
            
            sesionSeguridades.p_resetea_clave(ceduladecrypt);
            System.out.println("Se ha reseteado su clave************************");
            
            SegUrls findURL = segUrlsFacade.findURL("salir");
            String url = findURL.getUrl();
            response.sendRedirect(url);
            
        } catch (Exception e) {
        }
        
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    public void persist(Object object) {
        try {
            utx.begin();
            em.persist(object);
            utx.commit();
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", e);
            throw new RuntimeException(e);
        }
    }
    
}
