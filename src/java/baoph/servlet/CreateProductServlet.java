/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package baoph.servlet;

import baoph.tblCategory.TblCategoryDTO;
import baoph.tblProduct.TblProductCreateError;
import baoph.tblProduct.TblProductDAO;
import baoph.tblProduct.TblProductDTO;
import baoph.utils.FileHelpers;
import baoph.utils.MultipartHelper;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.ResourceBundle;
import javax.naming.NamingException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.FileItem;
import org.apache.log4j.Logger;

/**
 *
 * @author DELL
 */
public class CreateProductServlet extends HttpServlet {

    static Logger logger = Logger.getLogger(CreateProductServlet.class);
    private final String ERROR_PAGE = "error";
    private final String CREATE_PRODUCT_PAGE = "create";
    private final String ADMIN_HOME = "adminHome";

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
        String url = bundle.getString(ERROR_PAGE);
        try {
            /* TODO output your page here. You may use following sample code. */
            MultipartHelper multiHelper = new MultipartHelper();
            boolean isMultipart = multiHelper.operateMultipartConfig(request);
            if (isMultipart) {
                // Get list of parameter here
                Hashtable params = multiHelper.getParams();
                // Get image file item
                FileItem imageFile = multiHelper.getFileItem();

                // Get all parameters which is form field
                String productName = (String) params.get("txtProductName");
                String productPrice = (String) params.get("txtPrice");
                String productQuantity = (String) params.get("txtQuantity");
                String productCreationDate = (String) params.get("txtCreationDate");
                String productExpirationDate = (String) params.get("txtExpirationDate");
                String productCategoryID = (String) params.get("optionIndex");
                int price = 0;
                int quantity = 0;
                Date creationDate = null;
                Date expirationDate = null;
                
                boolean checkError = false;
                TblProductCreateError createError = new TblProductCreateError();

                // Product Name validation.
                if (productName.trim().isEmpty()) {
                    createError.setNameError("Product name is not empty");
                    checkError = true;
                } else {
                    productName = new String(productName.getBytes("iso-8859-1"), "UTF-8");
                }
                logger.info(productName + "-" + productPrice + "-" + productQuantity + "-"
                        + productCreationDate + "-" + productExpirationDate + productCategoryID);

                // Image link validation
                if (imageFile == null || imageFile.getName().trim().isEmpty()) {
                    createError.setImageError("You must input image");
                    checkError = true;
                }
                // Price validation
                try {
                    price = Integer.parseInt(productPrice);
                    if (price <= 0) {
//                        logger.info("Price must be integer > = 0");
                        createError.setPriceError("Price must be integer > = 0");
                        checkError = true;
                    }
                } catch (NumberFormatException e) {
//                    logger.info("Price must be integer >  0");
                    createError.setPriceError("Price must be integer > 0");
                    checkError = true;
                }
                // Quantity Validation
                try {
                    quantity = Integer.parseInt(productQuantity);
                    if (quantity < 0) {
//                        logger.info("quantity must be integer > = 0");
                        createError.setQuantityError("quantity must be integer > = 0");
                        checkError = true;
                    }
                } catch (NumberFormatException e) {
//                    logger.info("quantity must be integer");
                    createError.setQuantityError("quantity must be integer");
                    checkError = true;
                }
                // Date Validation
                try {
                    creationDate = Date.valueOf(productCreationDate);
                } catch (IllegalArgumentException e) {
//                    logger.info("Createtion Date is invalid");
                    createError.setCreateDateError("Createtion Date is invalid");
                    checkError = true;
                }
                // Date validation
                try {
                    expirationDate = Date.valueOf(productExpirationDate);
                } catch (IllegalArgumentException e) {
//                    logger.info("Expiration Date is invalid");
                    createError.setExpirationDateError("Expiration Date is invalid");
                    checkError = true;
                }
                // Invalid date validation
                if (creationDate != null && expirationDate != null) {
                    if (creationDate.after(expirationDate)) {
                        checkError = true;
                        createError.setInvalidDate("Creation Date must before Expiration Date");
                    }
                }

                if (checkError) { // Check if any error occured
                    request.setAttribute("createError", createError);
                    url = bundle.getString(CREATE_PRODUCT_PAGE);
                } else { // If no error.
                    TblProductDAO proDao = new TblProductDAO();
                    int newestIndex = proDao.getMaxIndex();
                    String imagePath = "image\\MoonCake_" + (newestIndex + 1) + ".jpg"; // Image link to save.

                    TblProductDTO newProduct = new TblProductDTO();
                    newProduct.setProductName(productName);
                    newProduct.setCreateDate(creationDate);
                    newProduct.setExprirationDate(expirationDate);
                    newProduct.setPrice(price);
                    newProduct.setQuantity(quantity);
                    newProduct.setStatus(true);
                    TblCategoryDTO category = new TblCategoryDTO();
                    category.setCategoryID(Integer.parseInt(productCategoryID));
                    newProduct.setCategory(category);
                    newProduct.setImage(imagePath);
                    // create product
                    boolean check = proDao.createProduct(newProduct);
                    if (check) { // if product is created
                        if (imageFile != null) {
                            if (!imageFile.getName().isEmpty()) {
                                String imageLink = getServletContext().getRealPath("/") + imagePath;
                                FileHelpers.writeFile(imageFile, imageLink);
                            }
                        }
                    }
                    url = bundle.getString(ADMIN_HOME);
                }
            }
        } catch (NamingException e) {
            logger.error("CreateProductServlet-NamingException : " + e.getMessage());
        } catch (SQLException e) {
            logger.error("CreateProductServlet-SQLException : " + e.getMessage());
        } catch (Exception e) {
            logger.error("CreateProductServlet-Exception : " + e.getMessage());
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
