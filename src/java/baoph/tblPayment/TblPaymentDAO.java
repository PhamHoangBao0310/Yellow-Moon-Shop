/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package baoph.tblPayment;

import baoph.utils.DBHelpers;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.naming.NamingException;

/**
 *
 * @author DELL
 */
public class TblPaymentDAO implements Serializable {

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

    public TblPaymentDAO() {
    }

    public List<TblPaymentDTO> getListPaymentMethod() throws SQLException, NamingException {
        List<TblPaymentDTO> list = null;
        try {
            String sql = "select PaymentID , Name "
                    + "from tbl_Payment ";
            con = DBHelpers.makeConnection();
            stm = con.prepareStatement(sql);
            rs = stm.executeQuery();
            list = new ArrayList<>();
            while (rs.next()) {
                int paymentID = rs.getInt("PaymentID");
                String name = rs.getString("Name");
                TblPaymentDTO dto = new TblPaymentDTO();
                dto.setName(name);
                dto.setPaymentID(paymentID);
                list.add(dto);
            }
        } finally {
            closeConnection();
        }
        return list;
    }

    public String getPaymentMethod(int paymentID) throws NamingException, SQLException {
        String result = "";
        try {
            String sql = "select Name "
                    + "from tbl_Payment "
                    + "where PaymentID = ? ";
            con = DBHelpers.makeConnection();
            stm = con.prepareStatement(sql);
            stm.setInt(1, paymentID);
            rs = stm.executeQuery();

            if (rs.next()) {
                result = rs.getString("Name");
            }
        } finally {
            closeConnection();
        }
        return result;
    }
}
