/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package baoph.servlet;

import baoph.tblProduct.TblProductDAO;
import baoph.tblProduct.TblProductDTO;
import baoph.tblRole.TblRoleDAO;
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
public class SearchServlet extends HttpServlet {

    static Logger logger = Logger.getLogger(SearchServlet.class);
    private final String ERROR_PAGE = "error";
    private final String USER_HOME_PAGE = "userHome";
    private final String ADMMIN_HOME_PAGE = "adminHome";
    private final int ELEMENT_IN_PAGE = 2;

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    private boolean isAdmin(HttpServletRequest req) throws SQLException, NamingException {
        boolean res = false;
        HttpSession session = req.getSession(false);
        if (session != null) {
            TblUserDTO user = (TblUserDTO) session.getAttribute("USER");
            if (user != null) {
                TblRoleDAO roleDao = new TblRoleDAO();
                String userRole = roleDao.getRole(user.getRoleID());
                return userRole.equals("Admin");
            }
        }
        return res;
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        logger.info("SearchServlet is called");
        String url = ERROR_PAGE;
        try {
            /* TODO output your page here. You may use following sample code. */
            String searchValue = request.getParameter("txtSearch");
            String optionIndex = request.getParameter("optionIndex");
            String minPrice = request.getParameter("txtMinPrice");
            String maxPrice = request.getParameter("txtMaxPrice");
            String index = request.getParameter("index");
            if (searchValue != null) {
//                logger.info("Search Value " + searchValue + " Length : " + searchValue.length());
                TblProductDAO productDao = new TblProductDAO();
                int categoryIndex = -1;
                int minimumPrice = -1;
                int maximumPrice = -1;
                boolean status = true;
                if (!optionIndex.trim().isEmpty()) {
                    categoryIndex = Integer.parseInt(optionIndex);
                }
                if (!minPrice.trim().isEmpty()) {
                    minimumPrice = Integer.parseInt(minPrice);
                }
                if (!maxPrice.trim().isEmpty()) {
                    maximumPrice = Integer.parseInt(maxPrice);
                }
                List<TblProductDTO> list;
                boolean isAdmin = isAdmin(request);
                if (isAdmin) { // If user is admin
                    status = false;
                    list = productDao.searchProduct(searchValue, categoryIndex,
                            minimumPrice, maximumPrice, Integer.parseInt(index), ELEMENT_IN_PAGE, status);
                    url = ADMMIN_HOME_PAGE;
                } else { // If user is customer or guest.
                    list = productDao.searchProduct(searchValue, categoryIndex,
                            minimumPrice, maximumPrice, Integer.parseInt(index), ELEMENT_IN_PAGE, status);
                    url = USER_HOME_PAGE;
                }
//                logger.info("Search List : " + list.size() + " Is Empty : " + list.isEmpty());
                request.setAttribute("searchList", list);
//                logger.debug("hahhaha");
            } else {
                url = USER_HOME_PAGE;
            }
        } catch (SQLException ex) {
            logger.error("SearchServlet-SQLException : " + ex.getMessage());
        } catch (NamingException ex) {
            logger.error("SearchServlet-NamingException : " + ex.getMessage());
        } catch (Exception ex) {
            logger.error("SearchServlet-Exception : " + ex.getMessage());
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
