/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package baoph.tblProduct;

import java.io.Serializable;

/**
 *
 * @author DELL
 */
public class TblProductCreateError implements Serializable {

    private String nameError;
    private String priceError;
    private String quantityError;
    private String createDateError;
    private String expirationDateError;
    private String invalidDate;
    private String imageError;

    public TblProductCreateError() {
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
     * @return the priceError
     */
    public String getPriceError() {
        return priceError;
    }

    /**
     * @param priceError the priceError to set
     */
    public void setPriceError(String priceError) {
        this.priceError = priceError;
    }

    /**
     * @return the quantityError
     */
    public String getQuantityError() {
        return quantityError;
    }

    /**
     * @param quantityError the quantityError to set
     */
    public void setQuantityError(String quantityError) {
        this.quantityError = quantityError;
    }

    /**
     * @return the createDateError
     */
    public String getCreateDateError() {
        return createDateError;
    }

    /**
     * @param createDateError the createDateError to set
     */
    public void setCreateDateError(String createDateError) {
        this.createDateError = createDateError;
    }

    /**
     * @return the expirationDateError
     */
    public String getExpirationDateError() {
        return expirationDateError;
    }

    /**
     * @param expirationDateError the expirationDateError to set
     */
    public void setExpirationDateError(String expirationDateError) {
        this.expirationDateError = expirationDateError;
    }

    /**
     * @return the invalidDate
     */
    public String getInvalidDate() {
        return invalidDate;
    }

    /**
     * @param invalidDate the invalidDate to set
     */
    public void setInvalidDate(String invalidDate) {
        this.invalidDate = invalidDate;
    }

    /**
     * @return the imageError
     */
    public String getImageError() {
        return imageError;
    }

    /**
     * @param imageError the imageError to set
     */
    public void setImageError(String imageError) {
        this.imageError = imageError;
    }

}
