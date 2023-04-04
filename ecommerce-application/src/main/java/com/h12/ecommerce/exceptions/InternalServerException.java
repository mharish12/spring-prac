package com.h12.ecommerce.exceptions;

/**
 * Internal server exception.
 */
public class InternalServerException extends Exception{
    /**
     * To initialize exception message.
     * @param msg exception message.
     */
    public InternalServerException(String msg){
        super(msg);
    }
}