/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package baoph.servlet;

import baoph.tblUser.GoogleUser;
import baoph.tblUser.TblUserDAO;
import baoph.tblUser.TblUserDTO;
import baoph.utils.GoogleUltils;
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
public class LoginGoogleServlet extends HttpServlet {

    static Logger logger = Logger.getLogger(LoginGoogleServlet.class);
    private final String LOGIN_PAGE = "letLogin";
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
        logger.info("LoginGoogleServlet is called");
        String url = ERROR_PAGE;
        try {
            /* TODO output your page here. You may use following sample code. */
            String code = request.getParameter("code");
            if (code == null || code.isEmpty()) {
                url = LOGIN_PAGE;
            } else {
                TblUserDAO userDao = new TblUserDAO();
                // Get access token from google
                String accessToken = GoogleUltils.getToken(code);
                logger.info("AccessToken : " + accessToken);
                // Get user info  from access token
                GoogleUser user = GoogleUltils.getUserInfo(accessToken);
                // Check if this user ever login or not
                TblUserDTO userDTO = userDao.checkLogin(user.getId(), "");
                logger.info(user.getId() + "-" + user.getEmail() + "-" + user.getFamily_name());
                if (userDTO != null) { // If user had logined before.
                    HttpSession session = request.getSession();
                    session.setAttribute("USER", userDTO);
                    url = USERHOME;
                } else { // If user is not existed in DB. Create new user .s
                    boolean addNewUser = userDao.addNewGoogleUser(user);
                    if (addNewUser) {
                        userDTO = userDao.checkLogin(user.getId(), "");
                        HttpSession session = request.getSession();
                        session.setAttribute("USER", userDTO);
                        url = USERHOME;
                    } else {
                        url = LOGIN_PAGE;
                    }
                }
            }
        } catch (SQLException ex) {
            logger.error("LoginGoogleServlet - SQLException :" + ex.getMessage());
        } catch (NamingException ex) {
            logger.error("LoginGoogleServlet - NamingException :" + ex.getMessage());
        } catch (Exception ex) {
            logger.error("LoginGoogleServlet - Exception :" + ex.getMessage());
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
