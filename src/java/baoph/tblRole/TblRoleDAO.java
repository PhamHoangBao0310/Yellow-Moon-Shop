/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package baoph.tblRole;

import baoph.utils.DBHelpers;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.naming.NamingException;

/**
 *
 * @author DELL
 */
public class TblRoleDAO implements Serializable {

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

    public TblRoleDAO() {
    }

    public String getRole(int roleID) throws SQLException, NamingException {
        String result = "User";
        try {
            String sql = "select Name "
                    + "from tbl_Role "
                    + "where RoleID = ? ";
            con = DBHelpers.makeConnection();
            stm = con.prepareStatement(sql);
            stm.setInt(1, roleID);
            rs = stm.executeQuery();
            if (rs.next()){
                result = rs.getString("Name");
            }
        } finally {
            closeConnection();
        }
        return result;
    }
}
