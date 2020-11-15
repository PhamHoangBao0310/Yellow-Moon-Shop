/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package baoph.cart;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.apache.log4j.Logger;

/**
 *
 * @author DELL
 */
public class CartObject implements Serializable {

    static Logger logger = Logger.getLogger(CartObject.class);
    private Map<Integer, Integer> items;

    /**
     * @return the items
     */
    public Map<Integer, Integer> getItems() {
        return items;
    }

    public void addProductToCart(int productID) {
        if (this.items == null) {
            this.items = new HashMap<>();
        }
        int quantity = 1;
        for (Integer proID : items.keySet()) {
            if (proID == productID) {
                quantity = this.items.get(proID) + 1;
                this.items.put(proID, quantity);
                logger.info("Increase quantity of product in cart");
                return;
            }
        }
        this.items.put(productID, quantity);
        logger.info("Add more product to cart");
    }

    public void removeProductFromCart(int productID) {
        if (this.items != null) {
            Iterator<Integer> iter = this.items.keySet().iterator();
            while (iter.hasNext()) {
                Integer proID = iter.next();
                if (productID == proID) {
                    iter.remove();
                    logger.info("Remove the product");
                    if (this.items.isEmpty()) {
                        this.items = null;
                        logger.info("Cart is destroyed");
                        return;
                    }
                }
            }
        }
    }

}
