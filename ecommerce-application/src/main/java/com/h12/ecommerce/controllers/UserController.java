package com.h12.ecommerce.controllers;

import com.h12.ecommerce.exceptions.InternalServerException;
import com.h12.ecommerce.exceptions.UserInputException;
import com.h12.ecommerce.models.User;
import com.h12.ecommerce.models.UserId;
import com.h12.ecommerce.services.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * User controller.
 */
@RestController
@RequestMapping(value = "/user")
@ResponseBody
public class UserController extends MainController{
    private final Logger logger = Logger.getLogger(UserController.class);
    private final UserService userService;

    /**
     * constructor.
     */
    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
    }

    /**
     *
     * @return response entity.
     */
    @GetMapping(value = "/all")
    public ResponseEntity<Object> getUsers(){
        try {
           return ResponseEntity.status(HttpStatus.FOUND).body(userService.getUsers());
        } catch (InternalServerException e) {
            logger.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     *
     * @param id user id.
     * @return response entity.
     */
    @GetMapping(value = "/user")
    public ResponseEntity<Object> getUserById(@RequestParam int id){
        try{
            return ResponseEntity.status(HttpStatus.FOUND).body(userService.getUserById(id));
        } catch (InternalServerException e) {
            logger.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (UserInputException e) {
            logger.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     *
     * @param user user object.
     * @return response entity.
     */
    @PutMapping(value = "/create")
    private ResponseEntity<Object> createUser(@RequestBody User user){
        try{
            userService.createUser(user);
            logger.info("User created.");
            return new ResponseEntity<>(HttpStatus.CREATED);
        }catch (InternalServerException e) {
            logger.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (UserInputException e) {
            logger.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     *
     * @param userId user id.
     * @return response entity.
     */
    @DeleteMapping(value = "/delete")
    public ResponseEntity<Object> deleteUser(@RequestBody UserId userId){
        try {
            userService.deleteUser(userId);
            logger.info("User deleted.");
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        } catch (InternalServerException e) {
            logger.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (UserInputException e) {
            logger.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     *
     * @param user user object.
     * @return response entity.
     */
    @PostMapping(value = "/update")
    public ResponseEntity<Object> updateUser(@RequestBody User user){
        try{
            userService.updateUser(user);
            logger.info("User updated");
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (InternalServerException e) {
            logger.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (UserInputException e) {
            logger.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
