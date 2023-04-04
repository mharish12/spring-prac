package com.h12.ecommerce.models;

/**
 * Product id model.
 */
public class ProductId {
    private int id;
    public ProductId(){

    }
    public ProductId(int id){
        this.id = id;
    }

    public int getId() {
        return id;
    }

    /**
     *
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }
}
