package com.h12.ecommerce.models;

import org.springframework.boot.autoconfigure.domain.EntityScan;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Product class.
 */
@EntityScan
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private final int productId;
    private final String name;
    private final int cost;
    private final int categoryId;

    /**
     * Constructor to initialize values.
     * @param productId product id.
     * @param name product name.
     * @param cost product cost.
     * @param categoryId product category id.
     */
    public Product(int productId,String name,int cost,int categoryId){
        this.productId = productId;
        this.name = name;
        this.cost = cost;
        this.categoryId = categoryId;
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
     * To return product id.
     * @return product id.
     */
    public int getProductId() {
        return productId;
    }
}
