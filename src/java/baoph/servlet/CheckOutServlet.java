/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package baoph.servlet;

import baoph.cart.CartObject;
import baoph.tblOrder.TblOrderCreateError;
import baoph.tblOrder.TblOrderDAO;
import baoph.tblOrder.TblOrderDTO;
import baoph.tblOrderDetails.TblOrderDetailsDAO;
import baoph.tblProduct.TblProductDAO;
import baoph.tblProduct.TblProductDTO;
import baoph.tblUser.TblUserDTO;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
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
public class CheckOutServlet extends HttpServlet {

    static Logger logger = Logger.getLogger(CheckOutServlet.class);
    private final String ERROR_PAGE = "error";
    private final String CHECKOUT_PAGE = "checkout";
    private final String USER_HOME = "userHome";

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
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        String url = ERROR_PAGE;
        logger.info("CheckOutServlet Get is called");
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
                            TblProductDTO product = proDao.getProductWithIDForCart(id);
                            if (product != null) { // check product is exist in DB
                                logger.info("product with ID " + id + " is available in DB");
                                productInCart.add(product);
                            } else {// check product is not  exist in DB. Remove this item if is no exist
                                logger.info("product with ID " + id + " is not available in DB");
                                iter.remove();
                            }
                        }
                        request.setAttribute("productForCheckOut", productInCart);
                    }
                }
            }
            url = CHECKOUT_PAGE;
        } catch (NamingException ex) {
            logger.error("CheckOutServlet-NamingException :" + ex.getMessage());
        } catch (SQLException ex) {
            logger.error("CheckOutServlet-SQLException :" + ex.getMessage());
        } catch (Exception ex) {
            logger.error("CheckOutServlet-Exception :" + ex.getMessage());
        } finally {
            ResourceBundle bundle = (ResourceBundle) getServletContext().getAttribute("map");
            url = bundle.getString(url);
            RequestDispatcher rd = request.getRequestDispatcher(url);
            rd.forward(request, response);
            out.close();
        }
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
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        logger.info("CheckOutServlet-POST is called");
        String url = ERROR_PAGE;
        try {
            TblOrderCreateError orderError = new TblOrderCreateError();
            boolean error = false;
            TblOrderDAO orderDao = new TblOrderDAO();

            // UserName validation
            String txtUserName = request.getParameter("txtUserName");
            if (txtUserName.trim().length() == 0) {
                error = true;
                orderError.setNameError("Username is not empty");
            }
            //Phone Validation
            String txtPhone = request.getParameter("txtPhone");
            if (txtPhone.trim().length() != 10) {
                error = true;
                orderError.setPhoneError("Phone is invalid");
            }
            //Address Validation
            String txtAddress = request.getParameter("txtAddress");
            if (txtAddress.trim().length() == 0) {
                error = true;
                orderError.setAddressError("Address is not empty");
            }
            // Get payment method
            String optionIndex = request.getParameter("paymentIndex");

            // Create new Order
            TblOrderDTO newOrder = new TblOrderDTO();
            newOrder.setCreateDate(new Timestamp(System.currentTimeMillis()));
            newOrder.setCustomerAddress(new String(txtAddress.getBytes("iso-8859-1"), "UTF-8"));
            newOrder.setPhone(txtPhone);
            newOrder.setCustomerName(new String(txtUserName.getBytes("iso-8859-1"), "UTF-8"));
            newOrder.setPaymentID(Integer.parseInt(optionIndex));
            int orderTotal = 0;

            HttpSession session = request.getSession(false);
            List<TblProductDTO> productList = new ArrayList(); // ProductList : list contains all product is available in cart and contains information for order details
            if (session != null) { // check if session is not null
                // Retrieve User in session scope to retrieve userID
                TblUserDTO user = (TblUserDTO) session.getAttribute("USER");
                if (user != null) {
                    newOrder.setUserID(user.getUserID());
                }
                // Get Cartobject in session.
                CartObject cart = (CartObject) session.getAttribute("CART");
                TblProductDAO proDao = new TblProductDAO();
                if (cart != null) {
                    // Get items in cart
                    Map<Integer, Integer> items = cart.getItems();
                    if (items != null) { // check is cart is not null
                        Iterator<Integer> iter = cart.getItems().keySet().iterator();
                        while (iter.hasNext()) {
                            int id = iter.next();
                            TblProductDTO product = proDao.getProductWithIDForCart(id);
                            if (product != null) { // check if product is exist in DB 
                                logger.info("product with ID " + id + " is available in DB");
                                productList.add(product);
                                orderTotal = orderTotal + product.getPrice() * items.get(id);
                            } else { // If status of product is InActive or product not exist
                                logger.info("product with ID " + id + " is not available in DB");
                                error = true;
                                orderError.setNullObjectError("Sorry some products are not available now. We just removed it. Please check your cart again");
                                iter.remove();
                            }
                        }
                    }
                    if (error) {
                        request.setAttribute("error", orderError);
                        request.setAttribute("productForCheckOut", productList);
                        url = CHECKOUT_PAGE;
                    } else {
                        boolean isUpdated = proDao.updateQuantity(productList, items);
                        if (isUpdated) { // If every product is not out of stock
                            newOrder.setTotal(orderTotal);
                            // Create order 
                            String orderID = orderDao.createNewOrder(newOrder);
                            logger.info("Create new Order : " + orderID);
                            TblOrderDetailsDAO detailDao = new TblOrderDetailsDAO();
                            // Create order details
                            boolean detailCreated = detailDao.addNewDetails(productList, orderID, items);
                            logger.info("Create Order detail : " + detailCreated);
                            if ((orderID != null) && detailCreated) {
                                session.removeAttribute("CART");
                            }
                            request.setAttribute("orderID", orderID);
                            url = USER_HOME;
                        } else {
                            request.setAttribute("error", orderError);
                            request.setAttribute("productForCheckOut", productList);
                            request.setAttribute("productList_ID_Error", proDao.getProductQuantityErrorIDList());
                            url = CHECKOUT_PAGE;
                        }
                    }
                }
            }
        } catch (SQLException ex) {
            logger.error("CheckOutServlet-SQLException :" + ex.getMessage());
        } catch (NamingException ex) {
            logger.error("CheckOutServlet-NamingException :" + ex.getMessage());
        } catch (Exception ex) {
            logger.error("CheckOutServlet-Exception :" + ex.getMessage());
        } finally {
            ResourceBundle bundle = (ResourceBundle) getServletContext().getAttribute("map");
            url = bundle.getString(url);
            RequestDispatcher rd = request.getRequestDispatcher(url);
            rd.forward(request, response);
            out.close();
        }
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
