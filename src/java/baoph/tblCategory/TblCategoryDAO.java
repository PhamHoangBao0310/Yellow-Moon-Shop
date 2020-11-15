/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package baoph.tblCategory;

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
public class TblCategoryDAO implements Serializable {

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

    public TblCategoryDAO() {
    }

    public List<TblCategoryDTO> getAllCategory() throws SQLException, NamingException {
        List<TblCategoryDTO> result = null;
        try {
            String sql = "select CategoryID , Name "
                    + "from tbl_Category ";
            con = DBHelpers.makeConnection();
            stm = con.prepareStatement(sql);
            rs = stm.executeQuery();
            result = new ArrayList<>();
            while (rs.next()) {
                int categoryID = rs.getInt("CategoryID");
                String name = rs.getString("Name");
                TblCategoryDTO dto = new TblCategoryDTO();
                dto.setCategoryID(categoryID);
                dto.setName(name);
                result.add(dto);
            }
        } finally {
            closeConnection();
        }
        return result;
    }
}
