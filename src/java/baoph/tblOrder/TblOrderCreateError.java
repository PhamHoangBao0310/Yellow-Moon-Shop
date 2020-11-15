/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package baoph.tblOrder;

import java.io.Serializable;

/**
 *
 * @author DELL
 */
public class TblOrderCreateError implements Serializable{
    
    private String nullObjectError;
    private String nameError;
    private String addressError;
    private String phoneError;

    public TblOrderCreateError() {
    }

    /**
     * @return the nullObjectError
     */
    public String getNullObjectError() {
        return nullObjectError;
    }

    /**
     * @param nullObjectError the nullObjectError to set
     */
    public void setNullObjectError(String nullObjectError) {
        this.nullObjectError = nullObjectError;
    }

    /**
     * @return the nameError
     */
    public String getNameError() {
        return nameError;
    }

    /**
     * @param nameError the nameError to set
     */
    public void setNameError(String nameError) {
        this.nameError = nameError;
    }

    /**
     * @return the addressError
     */
    public String getAddressError() {
        return addressError;
    }

    /**
     * @param addressError the addressError to set
     */
    public void setAddressError(String addressError) {
        this.addressError = addressError;
    }

    /**
     * @return the phoneErroe
     */
    public String getPhoneError() {
        return phoneError;
    }

    /**
     * @param phoneError the phoneError to set
     */
    public void setPhoneError(String phoneError) {
        this.phoneError = phoneError;
    }

    
}
