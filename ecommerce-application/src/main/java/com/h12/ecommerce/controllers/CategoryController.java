package com.h12.ecommerce.controllers;

import com.h12.ecommerce.models.Category;
import com.h12.ecommerce.models.ErrorEntity;
import com.h12.ecommerce.models.ProductId;
import com.h12.ecommerce.exceptions.InternalServerException;
import com.h12.ecommerce.exceptions.UserInputException;
import com.h12.ecommerce.services.CategoryService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;

/**
 * Category controller.
 */
@RestController
@ResponseBody
@RequestMapping("/category")
public class CategoryController extends MainController{
    @Autowired
    private final CategoryService categoryService;
    private final Logger logger;

    public CategoryController(){
        categoryService = new CategoryService();
        logger = super.getLogger();
    }
    /**
     *
     * @return response entity with List of categories.
     */
    @GetMapping("/read")
    public ResponseEntity<Object> getCategory() {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(categoryService.getAllCategories());
        } catch (InternalServerException e) {
            logger.error(e.getMessage());
            ErrorEntity error = new ErrorEntity(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     *
     * @param category category body.
     * @return response entity.
     */
    @PostMapping("/create")
    public ResponseEntity<Object> createCategory(@RequestBody Category category) {
        try {
            categoryService.createCategory(category);
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
     * @param category category body.
     * @return response entity.
     */
    @PutMapping("/update")
    public ResponseEntity<Object> updateCategory(@RequestBody  Category category) {
        try {
            categoryService.updateCategory(category);
            logger.info("Category updated.");
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
     * @param productId product id to delete.
     * @return response entity.
     */
    @DeleteMapping("/delete")
    public ResponseEntity<Object> deleteCategory(@RequestBody ProductId productId)  {
        try {
            categoryService.deleteCategory(productId);
            logger.info("Category deleted.");
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
     * @param categoryId category Id.
     * @return response entity.
     */
    @GetMapping("/products")
    public ResponseEntity<Object> getProductsByCategory(@RequestParam int categoryId){
        try{
            return ResponseEntity.status(HttpStatus.OK).body(categoryService.getProductsByCategory(categoryId));
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
