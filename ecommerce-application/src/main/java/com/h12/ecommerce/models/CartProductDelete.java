package com.h12.ecommerce.models;

public class CartProductDelete {
    int id;
    int userId;

    public CartProductDelete() {
    }

    public CartProductDelete(int id, int userId) {
        this.id = id;
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
