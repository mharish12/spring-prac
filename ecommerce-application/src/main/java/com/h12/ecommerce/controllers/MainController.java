package com.h12.ecommerce.controllers;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * Main controller.
 */
@RestController
@RequestMapping("/*")
public class MainController {
    private final static Logger logger = Logger.getLogger(MainController.class);

    public MainController(){

    }

    protected Logger getLogger(){
        return logger;
    }
}
