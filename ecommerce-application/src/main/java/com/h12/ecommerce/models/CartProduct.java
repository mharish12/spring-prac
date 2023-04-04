package com.h12.ecommerce.models;

/**
 * Cart product entity.
 */
public class CartProduct {
    private final int productId;
    private final String name;
    private final int cost;
    private final int categoryId;
    private final int quantity;

    /**
     * Constructor to initialize values.
     * @param productId product id.
     * @param productName product name.
     * @param cost product cost.
     * @param categoryId product category id.
     * @param quantity product quantity.
     */
    public CartProduct(int productId, String productName,int cost,int categoryId,int quantity){
        this.productId = productId;
        this.name = productName;
        this.cost = cost;
        this.categoryId = categoryId;
        this.quantity = quantity;
    }

    /**
     * To return product id.
     * @return product id.
     */
    public int getProductId() {
        return productId;
    }

    /**
     * To return product name.
     * @return product name.
     */
    public String getName() {
        return name;
    }

    /**
     * To return product cost.
     * @return product cost.
     */
    public int getCost() {
        return cost;
    }

    /**
     * To return product category id.
     * @return product category id.
     */
    public int getCategoryId() {
        return categoryId;
    }

    /**
     * To return product quantity.
     * @return product quantity.
     */
    public int getQuantity() {
        return quantity;
    }
}
