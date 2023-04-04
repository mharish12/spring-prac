package com.h12.ecommerce.controllers;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Cart controller.
 */
@RestController
@RequestMapping(value = "/cart")
@ResponseBody
public class CartController extends MainController{
    private final Logger logger = getLogger();

    @Autowired
    private final CartService cartService;

    /**
     * Cart service.
     */
    public CartController() {
        cartService  =  new CartService();
    }

    /**
     * To get cart details.
     * @return response entity.
     */
    @GetMapping(value = "/all")
    public ResponseEntity<Object> getCart(@RequestParam int userId){
        try {
            List<CartProduct> cartProducts = cartService.getCart(userId);
            return ResponseEntity.status(HttpStatus.OK).body(cartProducts);
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

    /**
     *
     * @param cart cart body.
     * @return response entity.
     */
    @PostMapping(value = "/add")
    public ResponseEntity<Object>  addProductToCart(@RequestBody Cart cart){
        try {
            cartService.addProductIntoCart(cart);
            logger.info("Product inserted into cart.");
            return new ResponseEntity<>(HttpStatus.CREATED);
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

    /**
     *
     * @param productIdObject Cart product that need to be deleted.
     * @return response entity.
     */
    @DeleteMapping(value = "/delete")
    public ResponseEntity<Object> deleteProductFromCart(@RequestBody CartProductDelete productIdObject){
        try{
            cartService.deleteProductFromCart(productIdObject);
            logger.error("Product deleted from cart.");
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
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

    /**
     *
     * @param cart cart product.
     * @return response entity.
     */
    @PutMapping(value = "/update")
    public ResponseEntity<Object> updateProductInCart(@RequestBody Cart cart){
        try{
            cartService.updateCartProduct(cart);
            logger.info("Product updated.");
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        }
        catch (InternalServerException e) {
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
