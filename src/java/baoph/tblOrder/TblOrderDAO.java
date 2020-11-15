/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package baoph.tblOrder;

import baoph.utils.DBHelpers;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import javax.naming.NamingException;

/**
 *
 * @author DELL
 */
public class TblOrderDAO implements Serializable {

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

    public TblOrderDAO() {
    }

    public String createNewOrder(TblOrderDTO dto) throws SQLException, NamingException {
        String result = null;
        try {
            con = DBHelpers.makeConnection();

            String sql = "insert into tbl_Order(UserID , Total , CreateDate , CustomerName , Phone , CustomerAddress , PaymentID , Status) "
                    + "output inserted.OrderID "
                    + "values( ? ,? ,? ,? ,? ,? ,? ,?) ";
            stm = con.prepareStatement(sql);

            stm.setString(1, dto.getUserID());
            stm.setInt(2, dto.getTotal());
            stm.setTimestamp(3, dto.getCreateDate());
            stm.setNString(4, dto.getCustomerName());
            stm.setString(5, dto.getPhone());
            stm.setNString(6, dto.getCustomerAddress());
            stm.setInt(7, dto.getPaymentID());
            stm.setBoolean(8, false);

            rs = stm.executeQuery();
            if (rs.next()){
                result = rs.getString("OrderID");
            }
        } finally {
            closeConnection();
        }
        return result;
    }

    public TblOrderDTO getOrderByOrderID(String orderID , String userID) throws SQLException, NamingException {
        TblOrderDTO dto = null;
        try {
            con = DBHelpers.makeConnection();
            
            String sql = "Select CustomerName , Phone , CustomerAddress , PaymentID , Status , CreateDate , Total "
                    + "from tbl_Order "
                    + "where OrderID = ? "
                    + "and  UserID = ? ";
            stm = con.prepareStatement(sql);
            stm.setString(1, orderID);
            stm.setString(2, userID);
            
            rs = stm.executeQuery();
            if (rs.next()){
                String customerName = rs.getString("CustomerName");
                String phone = rs.getString("Phone");
                String customerAddress = rs.getString("CustomerAddress");
                int paymentID = rs.getInt("PaymentID");
                boolean status = rs.getBoolean("Status");
                int total = rs.getInt("Total");
                Timestamp  createDate = rs.getTimestamp("CreateDate");
                
                dto = new TblOrderDTO();
                
                dto.setCustomerName(customerName);
                dto.setCreateDate(createDate);
                dto.setPaymentID(paymentID);
                dto.setPhone(phone);
                dto.setStatus(status);
                dto.setCustomerAddress(customerAddress);
                dto.setTotal(total);
                
            }
        } finally {
            closeConnection();
        }
        return dto;
    }
}
