/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package baoph.tblOrderDetails;

import baoph.tblProduct.TblProductDTO;
import baoph.utils.DBHelpers;
import java.io.Serializable;
import java.sql.Connection;
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
public class TblOrderDetailsDAO implements Serializable {

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

    public TblOrderDetailsDAO() {
    }

    public boolean addNewDetails(List<TblProductDTO> list, String orderID, Map<Integer, Integer> items) throws NamingException, SQLException {
        boolean check = false;
        try {
            con = DBHelpers.makeConnection();
            for (TblProductDTO dto : list) {

                String sql = "Insert into tbl_OrderDetails(OrderID , ProductID , Quantity , Price , ProductName ) "
                        + "values(? ,? ,? ,? , ?)";
                stm = con.prepareStatement(sql);
                stm.setString(1, orderID);
                stm.setInt(2, dto.getProductID());
                stm.setInt(3, items.get(dto.getProductID()));
                stm.setInt(4, dto.getPrice());
                stm.setString(5, dto.getProductName());

                check = stm.executeUpdate() > 0;
            }
        } finally {
            closeConnection();
        }
        return check;
    }

    public List<TblOrderDetailsDTO> getDetailsWithOrderID(String orderID) throws NamingException, SQLException {
        List<TblOrderDetailsDTO> list = null;
        try {
            con = DBHelpers.makeConnection();
            String sql = "Select Quantity , Price , ProductName "
                    + "from tbl_OrderDetails "
                    + "where OrderID = ? ";
            stm = con.prepareStatement(sql);
            stm.setString(1, orderID);
            rs = stm.executeQuery();
            list = new ArrayList<>();
            while (rs.next()) {
                TblOrderDetailsDTO dto = new TblOrderDetailsDTO();
                int quantity = rs.getInt("Quantity");
                int price = rs.getInt("Price");
                String productName = rs.getString("ProductName");
                dto.setPrice(price);
                dto.setProductName(productName);
                dto.setQuantity(quantity);

                list.add(dto);
            }
        } finally {
            closeConnection();
        }
        return list;
    }
}
