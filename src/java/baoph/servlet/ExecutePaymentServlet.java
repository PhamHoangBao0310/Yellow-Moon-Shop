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
import baoph.utils.PaypalPaymentService;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.ShippingAddress;
import com.paypal.api.payments.Transaction;
import com.paypal.base.rest.PayPalRESTException;
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
public class ExecutePaymentServlet extends HttpServlet {

    static Logger logger = Logger.getLogger(ExecutePaymentServlet.class);
    private final String ERROR_PAGE = "error";
    private final String CHECKOUT_PAGE = "checkout";
    private final String USER_HOME = "userHome";
    private final int PAYPAL_PAYMENT = 2;

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
        TblProductDAO proDao = new TblProductDAO();
        List<TblProductDTO> productList = new ArrayList();// ProductList : list contains all product is available in cart and contains information for order details
        Map<Integer, Integer> items = null;
        try {
            /* TODO output your page here. You may use following sample code. */
            // Get payment ID and payer ID from request parameter.
            String paymentId = request.getParameter("paymentId");
            String payerId = request.getParameter("PayerID");

            PaypalPaymentService paypalService = new PaypalPaymentService();
            // Get paymentInfo to retrieeve information
            Payment paymentInfo = paypalService.getPaymentDetails(paymentId);
            // Get transaction info
            Transaction transaction = paymentInfo.getTransactions().get(0);
            // Get shipping address info
            ShippingAddress shippingAddress = transaction.getItemList().getShippingAddress();

            TblOrderCreateError orderError = new TblOrderCreateError();
            boolean error = false;
            TblOrderDAO orderDao = new TblOrderDAO();
            TblOrderDTO newOrder = new TblOrderDTO();
            newOrder.setCreateDate(new Timestamp(System.currentTimeMillis()));
            newOrder.setCustomerAddress(shippingAddress.getLine1() + "_"
                    + shippingAddress.getCountryCode() + "_"
                    + shippingAddress.getPostalCode());
            newOrder.setPhone(shippingAddress.getPhone());
            newOrder.setCustomerName(shippingAddress.getRecipientName());
            newOrder.setPaymentID(PAYPAL_PAYMENT);
            int orderTotal = 0;

            HttpSession session = request.getSession(false);
            if (session != null) { // check if session is not null
                // Retrieve User in session scope to retrieve userID
                TblUserDTO user = (TblUserDTO) session.getAttribute("USER");
                if (user != null) {
                    newOrder.setUserID(user.getUserID());
                }
                // Get Cartobject in session.
                CartObject cart = (CartObject) session.getAttribute("CART");
                if (cart != null) {
                    // Get items in cart
                    items = cart.getItems();
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
                            // execute payment
                            paypalService.executePayment(paymentId, payerId);

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
        } catch (PayPalRESTException ex) {
            try {
                logger.error("ExecutePaymentServlet - PayPalRESTException : " + ex.getMessage());
                proDao.updateQuantityAgain(productList, items);
            } catch (NamingException ex1) {
                logger.error("ExecutePaymentServlet - NamingException : " + ex1.getMessage());
            } catch (SQLException ex1) {
                logger.error("ExecutePaymentServlet - NamingException : " + ex1.getMessage());
            }
        } catch (NamingException ex) {
            logger.error("ExecutePaymentServlet - NamingException : " + ex.getMessage());
        } catch (SQLException ex) {
            logger.error("ExecutePaymentServlet - SQLException : " + ex.getMessage());
        } catch (Exception ex) {
            logger.error("ExecutePaymentServlet - Exception : " + ex.getMessage());
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
