/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package baoph.tblProduct;

import baoph.tblCategory.TblCategoryDTO;
import baoph.utils.DBHelpers;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.naming.NamingException;

/**
 *
 * @author DELL
 */
public class TblProductDAO implements Serializable {

    Connection con;
    PreparedStatement stm;
    ResultSet rs;

    private void closeConnection() throws SQLException {
        if (rs != null) {
            rs.close();
        }
        if (stm != null) {
            stm.close();
        }
        if (con != null) {
            con.close();
        }
    }

    public TblProductDAO() {
    }

    public List<TblProductDTO> searchProduct(String searchValue, int optionIndex,
            int minPrice, int maxPrice, int index, int elements, boolean status) throws SQLException, NamingException {
        List<TblProductDTO> list = null;
        try {
            String sql = "select A.ProductID , B.Name as 'CategoryName' , A.ProductName , "
                    + "A.Price , A.Quantity, A.Status , A.CreateDate , A.ExpirationDate , A.Image "
                    + "from tbl_Product A join tbl_Category B  on A.CategoryID = B.CategoryID "
                    + "where A.ProductName like ? "
                    + "and ( A.CategoryID = ? or -1 = ? ) "
                    + "and ( A.Price >= ? or -1 = ? ) "
                    + "and ( A.Price <= ? or -1 = ? ) "
                    + "and ( 0 = ? or A.Status = ? ) "
                    + "and ( 0 = ? or A.Quantity > 0 ) "
                    + "and (0 = ? or A.ExpirationDate >= ?) "
                    + "order by A.CreateDate DESC "
                    + "offset ? rows fetch next ? rows only ";
            con = DBHelpers.makeConnection();
            stm = con.prepareStatement(sql);
            stm.setNString(1, "%" + searchValue + "%");
            stm.setInt(2, optionIndex);
            stm.setInt(3, optionIndex);
            stm.setInt(4, minPrice);
            stm.setInt(5, minPrice);
            stm.setInt(6, maxPrice);
            stm.setInt(7, maxPrice);
            stm.setBoolean(8, status);
            stm.setBoolean(9, status);
            stm.setBoolean(10, status);
            stm.setBoolean(11, status);
            stm.setDate(12, new Date(System.currentTimeMillis()));
            stm.setInt(13, (index - 1) * elements);
            stm.setInt(14, elements);

            rs = stm.executeQuery();
            list = new ArrayList<>();
            while (rs.next()) {
                int productId = rs.getInt("ProductID");
                String productName = rs.getString("ProductName");
                int price = rs.getInt("Price");
                int quantity = rs.getInt("Quantity");
                Date createDate = rs.getDate("CreateDate");
                Date expirationDate = rs.getDate("ExpirationDate");
                String image = rs.getString("Image");
                TblCategoryDTO dto = new TblCategoryDTO();
                dto.setCategoryID(optionIndex);
                dto.setName(rs.getString("CategoryName"));

                TblProductDTO product = new TblProductDTO();
                product.setProductID(productId);
                product.setPrice(price);
                product.setQuantity(quantity);
                product.setProductName(productName);
                product.setCreateDate(createDate);
                product.setExprirationDate(expirationDate);
                product.setImage(image);
                product.setCategory(dto);

                list.add(product);
            }
        } finally {
            closeConnection();
        }
        return list;
    }

    public TblProductDTO getProductWithID(int productId, boolean status) throws NamingException, SQLException {
        TblProductDTO result = null;
        try {
            String sql = "Select ProductName , Price , Quantity ,CreateDate , ExpirationDate , Image , CategoryID, Status "
                    + "from tbl_Product "
                    + "where ProductID = ? "
                    + "and (0 = ? or Status = ? )";
            con = DBHelpers.makeConnection();
            stm = con.prepareStatement(sql);
            stm.setInt(1, productId);
            stm.setBoolean(2, status);
            stm.setBoolean(3, status);
            rs = stm.executeQuery();
            if (rs.next()) {
                String productName = rs.getString("ProductName");
                int price = rs.getInt("Price");
                int quantity = rs.getInt("Quantity");
                Date createDate = rs.getDate("CreateDate");
                Date expirationDate = rs.getDate("ExpirationDate");
                String image = rs.getString("Image");
                TblCategoryDTO category = new TblCategoryDTO();
                int categoryID = rs.getInt("CategoryID");
                category.setCategoryID(categoryID);
                boolean proStatus = rs.getBoolean("Status");

                result = new TblProductDTO();
                result.setProductID(productId);
                result.setProductName(productName);
                result.setPrice(price);
                result.setQuantity(quantity);
                result.setCreateDate(createDate);
                result.setExprirationDate(expirationDate);
                result.setImage(image);
                result.setCategory(category);
                result.setStatus(proStatus);
            }
        } finally {
            closeConnection();
        }
        return result;
    }

    public TblProductDTO getProductWithIDForCart(int productId) throws NamingException, SQLException {
        TblProductDTO result = null;
        try {
            String sql = "Select ProductName , Price , Image  "
                    + "from tbl_Product "
                    + "where ProductID = ? "
                    + "and Status = ? ";
            con = DBHelpers.makeConnection();
            stm = con.prepareStatement(sql);
            stm.setInt(1, productId);
            stm.setBoolean(2, true);
            rs = stm.executeQuery();
            if (rs.next()) {
                String productName = rs.getString("ProductName");
                int price = rs.getInt("Price");
                String image = rs.getString("Image");
                result = new TblProductDTO();
                result.setProductID(productId);
                result.setProductName(productName);
                result.setPrice(price);
                result.setImage(image);

            }
        } finally {
            closeConnection();
        }
        return result;
    }

    public boolean updateProduct(TblProductDTO dto) throws SQLException, NamingException {
        boolean result = false;
        try {
            String sql = "Update tbl_Product "
                    + " set ProductName = ? , Price = ?, Quantity = ? "
                    + " , CategoryID = ? , Image = ?, CreateDate = ?, ExpirationDate = ?, Status = ? "
                    + "where ProductID = ? ";
            con = DBHelpers.makeConnection();
            stm = con.prepareStatement(sql);
            stm.setString(1, dto.getProductName());
            stm.setInt(2, dto.getPrice());
            stm.setInt(3, dto.getQuantity());
            stm.setInt(4, dto.getCategory().getCategoryID());
            stm.setString(5, dto.getImage());
            stm.setDate(6, dto.getCreateDate());
            stm.setDate(7, dto.getExprirationDate());
            stm.setBoolean(8, dto.isStatus());
            stm.setInt(9, dto.getProductID());

            result = stm.executeUpdate() > 0;
        } finally {
            closeConnection();
        }
        return result;
    }

    public boolean createProduct(TblProductDTO dto) throws NamingException, SQLException {
        boolean check = false;
        try {
            String sql = "Insert into tbl_Product (ProductName , Price , Quantity , CategoryID , Image , CreateDate , ExpirationDate  , Status ) "
                    + "values(? ,? ,? ,? ,?, ?, ?, ?)";
            con = DBHelpers.makeConnection();
            stm = con.prepareStatement(sql);
            stm.setString(1, dto.getProductName());
            stm.setInt(2, dto.getPrice());
            stm.setInt(3, dto.getQuantity());
            stm.setInt(4, dto.getCategory().getCategoryID());
            stm.setString(5, dto.getImage());
            stm.setDate(6, dto.getCreateDate());
            stm.setDate(7, dto.getExprirationDate());
            stm.setBoolean(8, dto.isStatus());

            check = stm.executeUpdate() > 0;
        } finally {
            closeConnection();
        }
        return check;
    }

    public int getMaxIndex() throws NamingException, SQLException {
        int index = 0;
        try {
            String sql = "Select MAX(ProductID) as Max from tbl_Product ";
            con = DBHelpers.makeConnection();
            stm = con.prepareStatement(sql);

            rs = stm.executeQuery();
            if (rs.next()) {
                index = rs.getInt("Max");
            }

        } finally {
            closeConnection();
        }
        return index;
    }

    public boolean checkStatus(int productID) throws SQLException, NamingException {
        boolean check = false;
        try {
            String sql = "Select Status "
                    + "from tbl_Product "
                    + "where ProductID = ? ";
            con = DBHelpers.makeConnection();
            stm = con.prepareStatement(sql);
            stm.setInt(1, productID);
            rs = stm.executeQuery();
            if (rs.next()) {
                check = rs.getBoolean("Status");
            }
        } finally {
            closeConnection();
        }
        return check;
    }

    public boolean hasMoreProduct(int productID) throws SQLException, NamingException {
        boolean check = false;
        try {
            String sql = "Select Quantity "
                    + "from tbl_Product "
                    + "where ProductID = ? ";
            con = DBHelpers.makeConnection();
            stm = con.prepareStatement(sql);
            stm.setInt(1, productID);
            rs = stm.executeQuery();
            if (rs.next()) {
                check = rs.getInt("Quantity") > 0;
            }
        } finally {
            closeConnection();
        }
        return check;
    }

    public boolean isOutOfStock(int productID, int quantity) throws NamingException, SQLException {
        boolean result = false;
        try {
            String sql = "Select Quantity "
                    + "from tbl_Product "
                    + "where ProductID = ? ";
            con = DBHelpers.makeConnection();
            stm = con.prepareStatement(sql);
            stm.setInt(1, productID);
            rs = stm.executeQuery();
            if (rs.next()) {
                result = rs.getInt("Quantity") < quantity;
            }
        } finally {
            closeConnection();
        }
        return result;
    }

    private List<Integer> productQuantityErrorIDList;

    public boolean updateQuantity(List<TblProductDTO> list, Map<Integer, Integer> items) throws NamingException, SQLException {
        boolean result = true;
        boolean hasSomethingOutOfStock = false;
        try {
            productQuantityErrorIDList = new ArrayList<>();
            con = DBHelpers.makeConnection();
            con.setAutoCommit(false);
            TblProductDAO anotherDAO = new TblProductDAO();
            for (TblProductDTO dto : list) {
                boolean isOutOfStock = anotherDAO.isOutOfStock(dto.getProductID(), items.get(dto.getProductID()));
                if (isOutOfStock) {
                    hasSomethingOutOfStock = true;
                    productQuantityErrorIDList.add(dto.getProductID());
                } else {
                    String sql = "Update tbl_Product "
                            + "set Quantity = Quantity - ? "
                            + "where ProductID = ? ";
                    stm = con.prepareStatement(sql);
                    stm.setInt(1, items.get(dto.getProductID()));
                    stm.setInt(2, dto.getProductID());

                    stm.executeUpdate();
                }
            }
            if (hasSomethingOutOfStock) {
                con.rollback();
                result = false;
            } else {
                con.commit();
            }
        } finally {
            closeConnection();
        }
        return result;
    }

    public boolean updateQuantityAgain(List<TblProductDTO> list, Map<Integer, Integer> items) throws NamingException, SQLException {
        boolean result = true;
        try {
            con = DBHelpers.makeConnection();
            for (TblProductDTO dto : list) {
                String sql = "Update tbl_Product "
                        + "set Quantity = Quantity + ? "
                        + "where ProductID = ? ";
                stm = con.prepareStatement(sql);
                stm.setInt(1, items.get(dto.getProductID()));
                stm.setInt(2, dto.getProductID());

                result = stm.executeUpdate() > 0;
            }
        } finally {
            closeConnection();
        }
        return result;
    }

    /**
     * @return the productQuantityErrorIDList
     */
    public List<Integer> getProductQuantityErrorIDList() {
        return productQuantityErrorIDList;
    }
}
