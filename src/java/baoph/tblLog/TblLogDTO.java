/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package baoph.tblLog;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 *
 * @author DELL
 */
public class TblLogDTO implements Serializable {

    private int logID;
    private String userID;
    private int productID;
    private Timestamp date;

    public TblLogDTO() {
    }

    /**
     * @return the logID
     */
    public int getLogID() {
        return logID;
    }

    /**
     * @param logID the logID to set
     */
    public void setLogID(int logID) {
        this.logID = logID;
    }

    /**
     * @return the userID
     */
    public String getUserID() {
        return userID;
    }

    /**
     * @param userID the userID to set
     */
    public void setUserID(String userID) {
        this.userID = userID;
    }

    /**
     * @return the productID
     */
    public int getProductID() {
        return productID;
    }

    /**
     * @param productID the productID to set
     */
    public void setProductID(int productID) {
        this.productID = productID;
    }

    /**
     * @return the date
     */
    public Timestamp getDate() {
        return date;
    }

    /**
     * @param date the date to set
     */
    public void setDate(Timestamp date) {
        this.date = date;
    }

}
