/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.mil.he1.portal.servlets;

import ec.mil.he1.portal.LoginJSFManagedBean;
import he1.seguridades.entities.SegUrls;
import he1.seguridades.entities.nuevos.VUsuariosClasif;
import he1.seguridades.sessions.SegUrlsFacade;
import he1.seguridades.sessions.VUsuariosClasifFacade;
import he1.utilities.SesionSeguridades;
import java.io.IOException;
import java.io.PrintWriter;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author christian_ruiz
 */
@WebServlet(name = "ServletReenviaMenu", urlPatterns = {"/reenme"})
public class ServletReenviaMenu extends HttpServlet {
    @EJB
    private SegUrlsFacade segUrlsFacade;

    @EJB
    private SesionSeguridades sesionSeguridades;

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @EJB
    private VUsuariosClasifFacade vUsuariosClasifFacade;


    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            //aca debe llegar la cedula , no id de usuario
            String usuario = sesionSeguridades.decyrpDinamico(request.getParameter("xphsumf"));
            VUsuariosClasif findByCedulaLogin = vUsuariosClasifFacade.findByCedulaLogin(usuario);
            //se creala session de usuario
            HttpSession session = request.getSession();
            //se llama al managedbean de login
            LoginJSFManagedBean login = (LoginJSFManagedBean) session.getAttribute("login");
            //a el mbean login se le asigna el usuario logueado
            login.setFindByCedulaLogin(findByCedulaLogin);
            //menu portal jsf
            SegUrls segUrls = segUrlsFacade.findURL("menu portal jsf");
            response.sendRedirect(segUrls.getUrl());
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

}
