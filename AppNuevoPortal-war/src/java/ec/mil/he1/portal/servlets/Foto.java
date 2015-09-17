/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.mil.he1.portal.servlets;

import ec.mil.he1.portal.LoginJSFManagedBean;
import he1.seguridades.entities.nuevos.VUsuariosClasif;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.annotation.Resource;
import javax.faces.context.FacesContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

/**
 *
 * @author christian_ruiz
 */
@WebServlet(name = "Foto", urlPatterns = {"/foto"})
public class Foto extends HttpServlet {

    @Resource(name = "jdbche1")
    private DataSource jdbche1;

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
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet rs = null;
        try {

            HttpSession session = request.getSession();
//            try {
//                LoginJSFManagedBean login = new LoginJSFManagedBean();
//                login = (LoginJSFManagedBean) session.getAttribute("login");
//                VUsuariosClasif findByCedulaLogin = login.getFindByCedulaLogin();
//            } catch (Exception e) {
//            }

            String cedula = request.getParameter("cedula");
            // Image bytes
            byte[] imageBytes = null;
            // Create the statement
            // This query is specific to MySQL, it returns only one row (using 'LIMIT 1') - the last uploaded file
            connection = jdbche1.getConnection();
            // Create the statement
            // This query is specific to MySQL, it returns only one row (using 'LIMIT 1') - the last uploaded file
            String sqlm = "SELECT loadBlobFromFile ('" + cedula + "') as archivo FROM DUAL";
            statement = connection.prepareStatement(sqlm);

            rs = statement.executeQuery();

            while (rs.next()) { // This will run only once
                imageBytes = rs.getBytes("archivo");
//                login.setExisteFoto("1");
            }

            response.getOutputStream().write(imageBytes);
            response.getOutputStream().close();

        } catch (Exception e) {
            // Display error message
            response.getWriter().write(e.getMessage());
            response.getWriter().close();
        } finally {
            try {
                rs.close();
                rs = null;
            } catch (Exception e) {
            }

            try {
                statement.close();
                statement = null;
            } catch (Exception e) {
            }
            try {
                connection.close();
                connection = null;
            } catch (Exception e) {
            }
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
