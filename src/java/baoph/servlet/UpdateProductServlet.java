/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package baoph.servlet;

import baoph.tblCategory.TblCategoryDTO;
import baoph.tblLog.TblLogDAO;
import baoph.tblLog.TblLogDTO;
import baoph.tblProduct.TblProductDAO;
import baoph.tblProduct.TblProductDTO;
import baoph.tblProduct.TblProductUpdateError;
import baoph.tblUser.TblUserDTO;
import baoph.utils.FileHelpers;
import baoph.utils.MultipartHelper;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Hashtable;
import java.util.ResourceBundle;
import javax.naming.NamingException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.fileupload.FileItem;
import org.apache.log4j.Logger;

/**
 *
 * @author DELL
 */
public class UpdateProductServlet extends HttpServlet {

    static Logger logger = Logger.getLogger(UpdateProductServlet.class);
    private final String ERROR_PAGE = "error";
    private final String VIEW_PRODUCT_CONTROLLER = "view";

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
        logger.info("UpdateProductServlet - POST is called");
        ResourceBundle bundle = (ResourceBundle) getServletContext().getAttribute("map");
        String url = bundle.getString(ERROR_PAGE);
        try {
            MultipartHelper multiHelper = new MultipartHelper();
            boolean isMultipart = multiHelper.operateMultipartConfig(request);
            if (isMultipart) {
                // Get list of parameter here
                Hashtable params = multiHelper.getParams();
                // Get image file item
                FileItem imageFile = multiHelper.getFileItem();

                TblProductDAO proDao = new TblProductDAO();

                // Check status of product.
                boolean status = false;
                if (((String) params.get("chkStatus")).equals("1")) {
                    status = true;
                }

                TblProductUpdateError updateError = new TblProductUpdateError();
                boolean checkError = false;
                // Get all parameters which is form field
                String productID = (String) params.get("txtProductId");
                String productName = (String) params.get("txtProductName");
                String productPrice = (String) params.get("txtPrice");
                String productQuantity = (String) params.get("txtQuantity");
                String productImageLink = (String) params.get("txtImageResource");
                String productCreationDate = (String) params.get("txtCreationDate");
                String productExpirationDate = (String) params.get("txtExpirationDate");
                String productCategoryID = (String) params.get("optionIndex");
                int price = 0;
                int quantity = 0;
                Date creationDate = null;
                Date expirationDate = null;

                if (status || proDao.checkStatus(Integer.parseInt(productID))) { // 
                    if (productName.trim().isEmpty()) {
                        updateError.setNameError("Product name is not empty");
                        checkError = true;
                    } else {
                        productName = new String(productName.getBytes("iso-8859-1"), "UTF-8");
                    }
                    logger.info(productID + "-" + productName + "-" + productPrice + "-" + productQuantity + "-"
                            + productCreationDate + "-" + productExpirationDate);

                    // Price validation
                    try {
                        price = Integer.parseInt(productPrice);
                        if (price <= 0) {
//                        logger.info("Price must be integer > = 0");
                            updateError.setPriceError("Price must be integer > = 0");
                            checkError = true;
                        }
                    } catch (NumberFormatException e) {
//                    logger.info("Price must be integer >  0");
                        updateError.setPriceError("Price must be integer > 0");
                        checkError = true;
                    }

                    // Quantity Validation
                    try {
                        quantity = Integer.parseInt(productQuantity);
                        if (quantity < 0) {
//                        logger.info("quantity must be integer > = 0");
                            updateError.setQuantityError("quantity must be integer > = 0");
                            checkError = true;
                        }
                    } catch (NumberFormatException e) {
//                    logger.info("quantity must be integer");
                        updateError.setQuantityError("quantity must be integer");
                        checkError = true;
                    }

                    //  CreationDate Validation
                    try {
                        creationDate = Date.valueOf(productCreationDate);
                    } catch (IllegalArgumentException e) {
//                    logger.info("Createtion Date is invalid");
                        updateError.setCreateDateError("Createtion Date is invalid");
                        checkError = true;
                    }

                    // ExpirationDate Validation
                    try {
                        expirationDate = Date.valueOf(productExpirationDate);
                    } catch (IllegalArgumentException e) {
//                    logger.info("Expiration Date is invalid");
                        updateError.setExpirationDateError("Expiration Date is invalid");
                        checkError = true;
                    }
                    if (creationDate != null && expirationDate != null) {
                        if (creationDate.after(expirationDate)) {
                            checkError = true;
                            updateError.setInvalidDate("Creation Date must before Expiration Date");
                        }
                    }
                } else {//If product is deleted and status now is false
                    updateError.setInactiveError("This product is deleted so you can not update this");
                    checkError = true;
                }

                if (checkError) { // If there are some errors
                    request.setAttribute("updateError", updateError);
                } else { // If there are no error. Update cake
                    TblProductDTO newProduct = new TblProductDTO();
                    newProduct.setProductID(Integer.parseInt(productID));
                    newProduct.setProductName(productName);
                    newProduct.setCreateDate(creationDate);
                    newProduct.setExprirationDate(expirationDate);
                    newProduct.setStatus(status);
                    newProduct.setImage(productImageLink);
                    newProduct.setPrice(price);
                    newProduct.setQuantity(quantity);
                    TblCategoryDTO categoryDto = new TblCategoryDTO();
                    categoryDto.setCategoryID(Integer.parseInt(productCategoryID));
                    newProduct.setCategory(categoryDto);

                    boolean check = proDao.updateProduct(newProduct);
                    
                    if (check) { //if update image succesfullly
                        // Rewrite image if image file is exist
                        if (imageFile != null) {
                            if (!imageFile.getName().isEmpty()) {
                                String imageLink = getServletContext().getRealPath("/") + productImageLink;
                                FileHelpers.writeFile(imageFile, imageLink);
                            }
                        }

                        // Get session to create Log
                        HttpSession session = request.getSession(false);
                        if (session != null) {
                            TblUserDTO user = (TblUserDTO) session.getAttribute("USER");
                            if (user != null) {
                                TblLogDAO logDao = new TblLogDAO();
                                TblLogDTO log = new TblLogDTO();
                                log.setProductID(newProduct.getProductID());
                                log.setUserID(user.getUserID());
                                log.setDate(new Timestamp(System.currentTimeMillis()));
                                boolean checkCreateLog = logDao.createLog(log);
                                logger.debug("Create log : " + checkCreateLog);
                            }
                        }
                    }
                    logger.debug("Check update is : " + check);
                }
                url = bundle.getString(VIEW_PRODUCT_CONTROLLER) + "?txtProductID=" + productID + "&btnAction=Edit";
            }
        } catch (SQLException e) {
            logger.error("UpdateProductServlet-SQLException : " + e.getMessage());
        } catch (NamingException e) {
            logger.error("UpdateProductServlet-NamingException : " + e.getMessage());
        } catch (NullPointerException e) {
            logger.error("UpdateProductServlet-NullPointerException : " + e.getMessage());
        } catch (Exception e) {
            logger.error("UpdateProductServlet-Exception : " + e.getMessage());
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
