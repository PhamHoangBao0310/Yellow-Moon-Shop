/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package baoph.servlet;

import baoph.cart.CartObject;
import baoph.tblProduct.TblProductDAO;
import baoph.tblProduct.TblProductDTO;
import baoph.utils.PaypalPaymentService;
import com.paypal.base.rest.PayPalRESTException;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.naming.NamingException;
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
public class AuthorizePaymentServlet extends HttpServlet {

    static Logger logger = Logger.getLogger(AuthorizePaymentServlet.class);
    private final String USERHOME = "userHome";
    private final String ERROR_PAGE = "error";

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
        String url = ERROR_PAGE;
        try {
            /* TODO output your page here. You may use following sample code. */
            logger.info("AuthorizePaymentServlet is called");
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
                            TblProductDTO product = proDao.getProductWithIDForCart(id);
                            if (product != null) { // check product is exist in DB
                                logger.info("product with ID " + id + " is available in DB");
                                productInCart.add(product);
                            } else {// check product is not  exist in DB. Remove this item if is no exist
                                logger.info("product with ID " + id + " is not available in DB");
                                iter.remove();
                            }
                        }
                        if (!productInCart.isEmpty()) {
                            // Call PaypalService.
                            PaypalPaymentService paypalService = new PaypalPaymentService();
                            // Authorize payment to redirect to paypal login Page.
                            String approveLink = paypalService.authorizePayment(productInCart, cart.getItems());
                            url = approveLink;
                            logger.info("Url : " + approveLink);
                        } else {
                            url = USERHOME;
                        }
                    }
                }
            } else {
                url = USERHOME;
            }
        } catch (NamingException ex) {
            logger.error("AuthorizePaymentServlet - NamingException : " + ex.getMessage());
        } catch (SQLException ex) {
            logger.error("AuthorizePaymentServlet - SQLException : " + ex.getMessage());
        } catch (PayPalRESTException ex) {
            logger.error("AuthorizePaymentServlet - PayPalRESTException : " + ex.getMessage());
        } catch (Exception ex) {
            logger.error("AuthorizePaymentServlet - Exception : " + ex.getMessage());
        } finally {
            response.sendRedirect(url);
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
