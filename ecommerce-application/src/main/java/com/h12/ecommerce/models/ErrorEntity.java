package com.h12.ecommerce.models;

/**
 * Error entity class.
 */
public class ErrorEntity {
    String msg;

    /**
     * Constructor to initialize message.
     * @param msg error message.
     */
    public ErrorEntity(String msg){
        this.msg = msg;
    }

    /**
     * To get message.
     * @return message.
     */
    public String getMsg() {
        return this.msg;
    }

    /**
     * To set message.
     * @param msg set message.
     */
    public void setMsg(String msg) {
        this.msg = msg;
    }
}
