package com.h12.ecommerce.exceptions;

/**
 * Product not found exception.
 */
public class UserInputException extends  Exception{
    /**
     * Constructor to initialize values.
     * @param msg message of the exception.
     */
    public UserInputException(String msg){
        super(msg);
    }
}
