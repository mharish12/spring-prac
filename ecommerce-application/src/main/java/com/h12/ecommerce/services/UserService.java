package com.h12.ecommerce.services;

import com.h12.ecommerce.dao.UserDao;
import com.h12.ecommerce.exceptions.InternalServerException;
import com.h12.ecommerce.exceptions.UserInputException;
import com.h12.ecommerce.models.User;
import com.h12.ecommerce.models.UserId;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserDao userDao;

    public UserService(){
        userDao = new UserDao();
    }
    public UserService(UserDao userDao){
        this.userDao = userDao;
    }

    /**
     *
     * @return users list.
     * @throws InternalServerException when connection issues or sql statement execution fails.
     */
    public List<User> getUsers() throws InternalServerException {
        return userDao.getUsers();
    }

    /**
     *
     * @param id user id.
     * @return user object.
     * @throws UserInputException when user input is not correct.
     * @throws InternalServerException when connection issues or sql statement execution fails.
     */
    public User getUserById(int id) throws InternalServerException, UserInputException {
        if(id<=0){
            throw new UserInputException("Id cannot be negative");
        }
        return userDao.getUserById(id);
    }

    /**
     *
     * @param user user object.
     * @throws UserInputException when user input is not correct.
     * @throws InternalServerException when connection issues or sql statement execution fails.
     */
    public void createUser(User user) throws UserInputException, InternalServerException {
        if(user.getId()<=0){
            throw new UserInputException("User id cannot be negative");
        }
        if(user.getName().equals("")){
            throw new UserInputException("User name cannot be empty");
        }
        userDao.create(user);
    }

    /**
     *
     * @param userId user id.
     * @throws UserInputException when user input is not correct.
     * @throws InternalServerException when connection issues or sql statement execution fails.
     */
    public void deleteUser(UserId userId) throws UserInputException, InternalServerException {
        if(userId.getId()<=0){
            throw new UserInputException("User id cannot be negative");
        }
        userDao.deleteUser(userId);
    }

    /**
     *
     * @param user user object.
     * @throws UserInputException when user input is not correct.
     * @throws InternalServerException when connection issues or sql statement execution fails.
     */
    public void updateUser(User user) throws UserInputException, InternalServerException {
        if(user.getId()<=0){
            throw new UserInputException("User id cannot be negative");
        }
        if(user.getName().equals("")){
            throw new UserInputException("User name cannot be empty");
        }
        userDao.updateUser(user);
    }
}
