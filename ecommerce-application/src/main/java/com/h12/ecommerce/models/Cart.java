package com.h12.ecommerce.models;

/**
 * Cart entity.
 */
public class Cart {
    int userId;
    int productId;
    int quantity;

    /**
     *
     * @param productId cart product id.
     * @param quantity product quantity.
     */
    public Cart(int productId,int quantity,int userId){
        this.productId = productId;
        this.quantity = quantity;
        this.userId = userId;
    }

    public int getProductId() {
        return this.productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
