package com.h12.ecommerce.controllers;

import com.h12.ecommerce.models.ErrorEntity;
import com.h12.ecommerce.models.Product;
import com.h12.ecommerce.models.ProductId;
import com.h12.ecommerce.exceptions.InternalServerException;
import com.h12.ecommerce.exceptions.UserInputException;
import com.h12.ecommerce.services.ProductService;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;



@RestController
@RequestMapping(value = "/product")
public class ProductController extends MainController{

    private final ProductService productService;
    private final Logger logger;

    @Autowired
    public ProductController(ProductService productService){
        logger = super.getLogger();
        this.productService = productService;
    }

    /**
     * @param product product object.
     */
    @PostMapping( value = "/create")
    public ResponseEntity<Object> createProduct(@RequestBody Product product){
        try {
            productService.createProduct(product);
            logger.info("Product created.");
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (InternalServerException e) {
            logger.error(e.getMessage());
            ErrorEntity error = new ErrorEntity(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        } catch (UserInputException e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    /**
     *
     * @return list of products.
     */
    @GetMapping(value = "/read")
    public ResponseEntity<Object> getAllProducts(){
        try {
            return ResponseEntity.status(HttpStatus.OK).body(productService.getAllProducts());
        } catch (InternalServerException e) {
            logger.error(e.getMessage());
            ErrorEntity error = new ErrorEntity(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * For updating the product.
     * @param product product object.
     * @return String of message.
     */
    @PutMapping( value = "/update")
    public ResponseEntity<Object> updateProduct(@RequestBody Product product){
        try {
            productService.updateProduct(product);
            logger.log(Level.INFO,"Product updated.");
            return ResponseEntity.status(HttpStatus.FOUND).body("Product updated.");
        }  catch (InternalServerException e) {
            logger.error(e.getMessage());
            ErrorEntity error = new ErrorEntity(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        } catch (UserInputException e) {
            logger.error(e.getMessage());
            ErrorEntity error = new ErrorEntity(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    /**
     *
     * @param productId product id that need to be deleted.
     */
    @DeleteMapping(value = "/delete")
    public  ResponseEntity<Object> deleteProduct(@RequestBody ProductId productId){
        try {
            productService.deleteProductById(productId);
            logger.log(Level.INFO,"Product deleted.");
            return ResponseEntity.status(HttpStatus.ACCEPTED).body("Product deleted.");
        } catch (InternalServerException e) {
            logger.error(e.getMessage());
            ErrorEntity error = new ErrorEntity(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        } catch (UserInputException e) {
            logger.error(e.getMessage());
            ErrorEntity error = new ErrorEntity(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }
}
