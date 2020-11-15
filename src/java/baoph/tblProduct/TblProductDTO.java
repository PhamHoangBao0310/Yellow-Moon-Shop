/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package baoph.tblProduct;

import baoph.tblCategory.TblCategoryDTO;
import java.io.Serializable;
import java.sql.Date;

/**
 *
 * @author DELL
 */
public class TblProductDTO implements Serializable {

    private int productID;
    private String productName;
    private int price;
    private int quantity;
    private String image;
    private Date createDate;
    private Date exprirationDate;
    private TblCategoryDTO category;
    private boolean status;

    public TblProductDTO() {
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
     * @return the productName
     */
    public String getProductName() {
        return productName;
    }

    /**
     * @param productName the productName to set
     */
    public void setProductName(String productName) {
        this.productName = productName;
    }

    /**
     * @return the price
     */
    public int getPrice() {
        return price;
    }

    /**
     * @param price the price to set
     */
    public void setPrice(int price) {
        this.price = price;
    }

    /**
     * @return the quantity
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * @param quantity the quantity to set
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * @return the image
     */
    public String getImage() {
        return image;
    }

    /**
     * @param image the image to set
     */
    public void setImage(String image) {
        this.image = image;
    }

    /**
     * @return the category
     */
    public TblCategoryDTO getCategory() {
        return category;
    }

    /**
     * @param category the category to set
     */
    public void setCategory(TblCategoryDTO category) {
        this.category = category;
    }

    /**
     * @return the createDate
     */
    public Date getCreateDate() {
        return createDate;
    }

    /**
     * @param createDate the createDate to set
     */
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    /**
     * @return the exprirationDate
     */
    public Date getExprirationDate() {
        return exprirationDate;
    }

    /**
     * @param exprirationDate the exprirationDate to set
     */
    public void setExprirationDate(Date exprirationDate) {
        this.exprirationDate = exprirationDate;
    }

    /**
     * @return the status
     */
    public boolean isStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(boolean status) {
        this.status = status;
    }

}
