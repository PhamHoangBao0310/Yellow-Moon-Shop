/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package baoph.servlet;

import baoph.cart.CartObject;
import baoph.tblProduct.TblProductDAO;
import baoph.tblProduct.TblProductDTO;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import javax.naming.NamingException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;

/**
 *
 * @author DELL
 */
public class ViewCartServlet extends HttpServlet {

    static Logger logger = Logger.getLogger(ViewCartServlet.class);
    private final String ERROR_PAGE = "error";
    private final String CART_PAGE = "cart";

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
        logger.info("ViewCartServlet is called");
        String url = ERROR_PAGE;
        try {
            /* TODO output your page here. You may use following sample code. */
            HttpSession session = request.getSession(false);
            if (session != null) {
                CartObject cart = (CartObject) session.getAttribute("CART");
                if (cart != null) {
                    if (cart.getItems() != null) {
                        List<TblProductDTO> productInCart = new ArrayList<>();
                        TblProductDAO proDao = new TblProductDAO();
                        Iterator<Integer> iter = cart.getItems().keySet().iterator();
                        while (iter.hasNext()) {
                            int id = iter.next();
                            if (cart.getItems().get(id) > 0) { // check if quantity in cart is > 0
                                TblProductDTO product = proDao.getProductWithIDForCart(id);
                                if (product != null) { // check product is exist in DB
                                    logger.info("product with ID " + id + " is available in DB");
                                    productInCart.add(product);
                                } else {// check product is not  exist in DB. Remove this item if is no exist
                                    logger.info("product with ID " + id + " is not available in DB");
                                    iter.remove();
                                }
                            } else {// check if quantity in cart is <= 0. Remove this item if is <= 0
                                logger.info("This item is 0. So we will remove it");
                                iter.remove();
                            }
                        }
                        request.setAttribute("productInCart", productInCart);
                    }
                }
            }
            url = CART_PAGE;
        } catch (NamingException ex) {
            logger.error("ViewCartServlet-NamingException :" + ex.getMessage());
        } catch (SQLException ex) {
            logger.error("ViewCartServlet-SQLException :" + ex.getMessage());;
        } catch (Exception ex) {
            logger.error("ViewCartServlet-Exception :" + ex.getMessage());;
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
