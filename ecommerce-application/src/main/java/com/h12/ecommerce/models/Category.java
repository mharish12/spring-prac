package com.h12.ecommerce.models;

/**
 * Category class.
 */
public class Category {
    private  int id;
    private String categoryName;

    /**
     * Constructor to initialize values.
     * @param categoryName category name.
     */
    public Category(int id,String categoryName){
        this.id = id;
        this.categoryName = categoryName;
    }

    /**
     * To return the category name.
     * @return category name.
     */
    public String getCategoryName(){
        return this.categoryName;
    }

    /**
     *
     * @return category id.
     */
    public int getId() {
        return this.id;
    }

    /**
     * To set category id.
     * @param id category id.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * To set category name.
     * @param categoryName category name.
     */
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
