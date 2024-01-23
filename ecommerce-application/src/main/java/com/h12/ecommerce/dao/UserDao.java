package com.h12.ecommerce.dao;

import com.h12.ecommerce.exceptions.InternalServerException;
import com.h12.ecommerce.exceptions.UserInputException;
import com.h12.ecommerce.models.User;
import com.h12.ecommerce.models.UserId;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * User Dao.
 */
public class UserDao extends MainDao {
    private final static String selectUsersSql = "SELECT * FROM USERS;";
    private final static String selectUserByIdSql ="SELECT * FROM USERS WHERE ID = ?;";
    private final static String insertUserSql = "INSERT INTO USERS(ID,NAME) VALUES(?,?);";
    private final static String deleteUserSql = "DELETE FROM USERS WHERE ID = ?";
    private final static String updateUserSql = "UPDATE USERS SET NAME = ? WHERE ID = ?";
    private Connection connection;


    /**
     * Default constructor.
     */
    public UserDao() {

    }

    /**
     *
     * @return list of users.
     * @throws InternalServerException when connection issues or sql statement execution fails.
     */
    public List<User> getUsers() throws InternalServerException {
        try{
            connection = super.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(selectUsersSql);
            List<User> userList = new ArrayList<>();
            while (resultSet.next()){
                int id = resultSet.getInt("ID");
                String name = resultSet.getString("NAME");
                User currentUser = new User(id,name);
                userList.add(currentUser);
            }
            statement.close();
            connection.close();
            return userList;
        } catch (SQLException e) {
            throw new InternalServerException(e.getMessage());
        }
    }



    /**
     *
     * @param id user id.
     * @return user object.
     * @throws UserInputException when user input is not correct.
     * @throws InternalServerException when connection issues or sql statement execution fails.
     */
    public User getUserById(int id) throws InternalServerException, UserInputException {
        try {
            connection = super.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(selectUserByIdSql);
            preparedStatement.setInt(1,id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                String name = resultSet.getString("NAME");
                return new User(id,name);
            }
            preparedStatement.close();
            connection.close();
            throw new UserInputException("User not found.");
        } catch (SQLException e) {
            throw new InternalServerException(e.getMessage());
        }
    }

    /**
     *
     * @param user user object.
     * @throws InternalServerException when connection issues or sql statement execution fails.
     */
    public void create(User user) throws InternalServerException {
        try {
            connection = super.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(insertUserSql);
            preparedStatement.setInt(1,user.getId());
            preparedStatement.setString(2,user.getName());
            preparedStatement.execute();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            throw new InternalServerException(e.getMessage());
        }
    }

    /**
     *
     * @param userId user id.
     * @throws InternalServerException when connection issues or sql statement execution fails.
     */
    public void deleteUser(UserId userId) throws InternalServerException {
        try {
            connection = super.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(deleteUserSql);
            preparedStatement.setInt(1,userId.getId());
            preparedStatement.executeUpdate();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            throw new InternalServerException(e.getMessage());
        }
    }

    /**
     *
     * @param user user object.
     * @throws InternalServerException when connection issues or sql statement execution fails.
     */
    public void updateUser(User user) throws InternalServerException {
        try {
            connection = super.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(updateUserSql);
            preparedStatement.setInt(2,user.getId());
            preparedStatement.setString(1,user.getName());
            preparedStatement.executeUpdate();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            throw new InternalServerException(e.getMessage());
        }
    }
}
