/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package baoph.tblLog;

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
public class TblLogDAO implements Serializable {

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

    public TblLogDAO() {
    }

    public boolean createLog(TblLogDTO dto) throws SQLException, NamingException {
        boolean check = false;
        try {
            String sql = "insert into tbl_Log (UserID , ProductID , Date) "
                    + "values ( ? , ? , ? ) ";
            con = DBHelpers.makeConnection();
            stm = con.prepareStatement(sql);
            stm.setString(1, dto.getUserID());
            stm.setInt(2, dto.getProductID());
            stm.setTimestamp(3, dto.getDate());
            
            check = stm.executeUpdate() > 0 ;
        } finally {
            closeConnection();
        }
        return check;
    }

}
