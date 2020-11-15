/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package baoph.listener;

import baoph.tblCategory.TblCategoryDAO;
import baoph.tblCategory.TblCategoryDTO;
import baoph.tblPayment.TblPaymentDAO;
import baoph.tblPayment.TblPaymentDTO;
import java.sql.SQLException;
import java.util.List;
import javax.naming.NamingException;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import org.apache.log4j.Logger;

/**
 * Web application lifecycle listener.
 *
 * @author DELL
 */
public class MySessionListener implements HttpSessionListener {

    static Logger logger = Logger.getLogger(MySessionListener.class);

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        logger.info("MySessionListener is called ");
        try {
            TblCategoryDAO categoryDAO = new TblCategoryDAO();
            List<TblCategoryDTO> list = categoryDAO.getAllCategory();
            HttpSession session = se.getSession();
            session.setAttribute("CATEGORY_LIST", list);
            
            TblPaymentDAO paymentDAO = new TblPaymentDAO();
            List<TblPaymentDTO> paymentList = paymentDAO.getListPaymentMethod();
            session.setAttribute("PAYMENT_LIST", paymentList);
        } catch (SQLException ex) {
            logger.error("MySessionListener-SQLException : " + ex.getMessage());
        } catch (NamingException ex) {
            logger.error("MySessionListener-NamingException : " + ex.getMessage());
        }
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        logger.info("MySessionListener is closed");
    }
}
