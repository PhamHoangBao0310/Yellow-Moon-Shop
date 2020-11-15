/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package baoph.servlet;

import baoph.tblOrder.TblOrderDAO;
import baoph.tblOrder.TblOrderDTO;
import baoph.tblOrderDetails.TblOrderDetailsDAO;
import baoph.tblOrderDetails.TblOrderDetailsDTO;
import baoph.tblPayment.TblPaymentDAO;
import baoph.tblUser.TblUserDTO;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
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
public class SearchOrderServlet extends HttpServlet {

    static Logger logger = Logger.getLogger(SearchOrderServlet.class);
    private final String ORDER_PAGE = "trackOrder";
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
        logger.info("SearchOrderServlet is called");
        String url = ERROR_PAGE;
        try {
            /* TODO output your page here. You may use following sample code. */
            String txtOrderID = request.getParameter("txtOrderID");
            if (txtOrderID.trim().length() == 0) {
                url = ORDER_PAGE;
            } else {
                TblOrderDAO orderDao = new TblOrderDAO();
                TblOrderDetailsDAO detailDao = new TblOrderDetailsDAO();
                TblPaymentDAO paymentDAO = new TblPaymentDAO();
                TblOrderDTO order = null;
                List<TblOrderDetailsDTO> orderDetailList = null;
                String paymentMethod = null;
                HttpSession session = request.getSession(false);
                if (session != null) {
                    TblUserDTO user = (TblUserDTO) session.getAttribute("USER");
                    order = orderDao.getOrderByOrderID(txtOrderID, user.getUserID());
                }

                if (order != null) {
                    orderDetailList = detailDao.getDetailsWithOrderID(txtOrderID);
                    paymentMethod = paymentDAO.getPaymentMethod(order.getPaymentID());
                }

                request.setAttribute("order", order);
                request.setAttribute("orderDetails", orderDetailList);
                request.setAttribute("paymentMethod", paymentMethod);
                url = ORDER_PAGE;
            }
        } catch (SQLException ex) {
            logger.error("SearchOrderServlet-SQLException :" + ex.getMessage());
        } catch (NamingException ex) {
            logger.error("SearchOrderServlet-NamingException :" + ex.getMessage());
        } catch (Exception ex) {
            logger.error("SearchOrderServlet-Exception :" + ex.getMessage());
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
