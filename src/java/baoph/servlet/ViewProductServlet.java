/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package baoph.servlet;

import baoph.tblProduct.TblProductDAO;
import baoph.tblProduct.TblProductDTO;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javax.naming.NamingException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

/**
 *
 * @author DELL
 */
public class ViewProductServlet extends HttpServlet {

    static Logger logger = Logger.getLogger(ViewProductServlet.class);
    private final String ERROR_PAGE = "error";
    private final String CAKE_UPDATE_PAGE = "editPage";

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
        PrintWriter out = response.getWriter();
        logger.info("ViewProductServlet is called");
        String url = ERROR_PAGE;
        try {
            /* TODO output your page here. You may use following sample code. */
            String productID = request.getParameter("txtProductID");
            TblProductDAO productDao = new TblProductDAO();
            TblProductDTO product = productDao.getProductWithID(Integer.parseInt(productID), false);
            request.setAttribute("product", product);
            url = CAKE_UPDATE_PAGE;
        } catch (NamingException ex) {
            logger.error("UpdateProductServlet-NamingException : " + ex.getMessage());
        } catch (SQLException ex) {
            logger.error("UpdateProductServlet-SQLException : " + ex.getMessage());
        } catch (Exception ex) {
            logger.error("UpdateProductServlet-Exception : " + ex.getMessage());
        } finally {
            ResourceBundle bundle = (ResourceBundle) getServletContext().getAttribute("map");
            url = bundle.getString(url);
            RequestDispatcher rd = request.getRequestDispatcher(url);
            rd.forward(request, response);
            out.close();
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
