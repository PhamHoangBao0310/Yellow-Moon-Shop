/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package baoph.servlet;

import baoph.cart.CartObject;
import baoph.tblProduct.TblProductDAO;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
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
public class AddToCartServlet extends HttpServlet {

    static Logger logger = Logger.getLogger(AddToCartServlet.class);

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
        logger.info("AddToCartServlet is called");
        try {
            /* TODO output your page here. You may use following sample code. */
            HttpSession session = request.getSession();
            CartObject cart = (CartObject) session.getAttribute("CART");
            if (cart == null) {
                logger.info("User dont have cart yet . We will create for them.");
                cart = new CartObject();
            }

            String productID = request.getParameter("txtProductID");
            // Validate product has more quantity or not
            TblProductDAO proDao = new TblProductDAO();
            boolean hasMoreProduct = proDao.hasMoreProduct(Integer.parseInt(productID));
            if (hasMoreProduct) {
                boolean isExist = proDao.checkStatus(Integer.parseInt(productID)); // validate product is exist or not
                if (isExist) {
                    cart.addProductToCart(Integer.parseInt(productID));
                    session.setAttribute("CART", cart);
                    out.write("Add to cart successfully");
                } else {
                    out.write("Sorry , this product is not available now so we can not update your cart");
                }
            } else {
                out.write("Sorry , the quantity of this product is 0 so we can not update your cart");
            }

        } catch (SQLException ex) {
            logger.error("AddToCartServlet-SQLException " + ex.getMessage());
        } catch (NamingException ex) {
            logger.error("AddToCartServlet-NamingException " + ex.getMessage());
        } catch (NullPointerException ex) {
            logger.error("AddToCartServlet-NullPointerException " + ex.getMessage());
        } catch (Exception ex) {
            logger.error("AddToCartServlet-Exception " + ex.getMessage());
        } finally {
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
