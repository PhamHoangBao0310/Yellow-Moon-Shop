/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package baoph.servlet;

import baoph.utils.PaypalPaymentService;
import com.paypal.api.payments.PayerInfo;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.ShippingAddress;
import com.paypal.api.payments.Transaction;
import com.paypal.base.rest.PayPalRESTException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ResourceBundle;
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
public class ReviewPaymentServlet extends HttpServlet {

    static Logger logger = Logger.getLogger(ReviewPaymentServlet.class);
    private final String ERROR_PAGE = "error";
    private final String REVIEW_PAGE = "reviewPaypal";

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
        ResourceBundle bundle = (ResourceBundle) getServletContext().getAttribute("map");
        logger.info("ReviewPaymentServlet is called");
        String url = bundle.getString(ERROR_PAGE);
        try {
            /* TODO output your page here. You may use following sample code. */
            // Get payment ID and payer ID from request parameter.
            String paymentId = request.getParameter("paymentId");
            String payerId = request.getParameter("PayerID");

            PaypalPaymentService paypalService = new PaypalPaymentService();
            // Get payment detail from payment ID.
            Payment payment = paypalService.getPaymentDetails(paymentId);
            // Get payer info
            PayerInfo payerInfo = payment.getPayer().getPayerInfo();
            // Get transaction info
            Transaction transaction = payment.getTransactions().get(0);
            // Get shipping address
            ShippingAddress shippingAddress = transaction.getItemList().getShippingAddress();

            logger.info("Payment info : " + payment.toJSON());
            logger.info("Transaction Info : " + transaction.toJSON());
            logger.info("Payer Info : " + payerInfo.getPhone() + "-" + payerInfo.getFirstName() + "-" + payerInfo.getLastName());

            request.setAttribute("payer", payerInfo);
            request.setAttribute("transaction", transaction);
            request.setAttribute("shippingAddress", shippingAddress);

            url = bundle.getString(REVIEW_PAGE) + "?paymentId=" + paymentId + "&PayerID=" + payerId;
        } catch (PayPalRESTException ex) {
            logger.error("ReviewPaymentServlet-PayPalRESTException : " + ex.getMessage());
        } catch (Exception ex) {
            logger.error("ReviewPaymentServlet-Exception : " + ex.getMessage());
        } finally {
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
