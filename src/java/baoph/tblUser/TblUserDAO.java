/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package baoph.tblUser;

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
public class TblUserDAO implements Serializable {

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

    public TblUserDAO() {
    }

    public TblUserDTO checkLogin(String userId, String password) throws SQLException, NamingException {
        TblUserDTO result = null;
        try {
            String sql = "Select Name , Status , RoleID , Phone , Address "
                    + "from tbl_User "
                    + "where UserID = ? and Password = ? ";
            con = DBHelpers.makeConnection();
            stm = con.prepareStatement(sql);
            stm.setString(1, userId);
            stm.setString(2, password);
            rs = stm.executeQuery();
            if (rs.next()) {
                int status = rs.getInt("Status");
                if (status == 1) { // If user is active
                    String name = rs.getString("Name");
                    int roleID = rs.getInt("RoleID");
                    String phone = rs.getString("Phone");
                    String address = rs.getString("Address");
                    result = new TblUserDTO();
                    result.setName(name);
                    result.setRoleID(roleID);
                    result.setUserID(userId);
                    result.setPhoneNumber(phone);
                    result.setAddress(address);
                }
            }
        } finally {
            closeConnection();
        }
        return result;
    }

    public boolean addNewGoogleUser(GoogleUser user) throws SQLException, NamingException {
        boolean result = false;
        try {
            String sql = "insert into tbl_User (UserID , Name , RoleID , Status , Password ) "
                    + "values(? ,?, ?, ?, ?) ";
            con = DBHelpers.makeConnection();
            stm = con.prepareStatement(sql);
            stm.setString(1, user.getId());
            stm.setString(2, user.getEmail());
            stm.setInt(3, 1);
            stm.setInt(4, 1);
            stm.setString(5, "");

            result = stm.executeUpdate() > 0;
        } finally {
            closeConnection();
        }
        return result;
    }

    public List<TblUserDTO> getAllUser() throws SQLException, NamingException {
        List<TblUserDTO> list = null;
        try {
            String sql = "Select UserID , Name , Status , RoleID , Phone , Address "
                    + "from tbl_User ";
            con = DBHelpers.makeConnection();
            stm = con.prepareStatement(sql);
            rs = stm.executeQuery();
            list = new ArrayList<>();
            while (rs.next()) {
                String userId = rs.getString("UserID");
                String name = rs.getString("Name");
                String phone = rs.getString("Phone");
                String address = rs.getString("Address");
                int status = rs.getInt("Status");
                int roleID = rs.getInt("RoleID");
                TblUserDTO user = new TblUserDTO();
                user.setAddress(address);
                user.setName(name);
                user.setUserID(userId);
                user.setPhoneNumber(phone);
                user.setRoleID(roleID);
                user.setStatus(status);
                System.out.println(user.getUserID());
                list.add(user);
            }
        } finally {
            closeConnection();
        }
        return list;

    }
}
