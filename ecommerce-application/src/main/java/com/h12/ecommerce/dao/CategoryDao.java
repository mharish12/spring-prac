package com.h12.ecommerce.dao;


import com.h12.ecommerce.exceptions.InternalServerException;
import com.h12.ecommerce.models.Category;
import com.h12.ecommerce.models.ProductId;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Category Dao.
 */
public class CategoryDao extends MainDao{

    private Connection connection;

    private final static String selectAllCategoriesSql = "SELECT * FROM CATEGORIES;";
    private final static String insertIntoCategoriesSql = "INSERT INTO CATEGORIES(ID,CATEGORY_NAME) VALUES(?,?);";
    private final static String updateCategorySql = "UPDATE CATEGORIES SET CATEGORY_NAME= ? WHERE ID= ?;";
    private final static String deleteCategorySql = "DELETE FROM CATEGORIES WHERE ID = ?;";
    private final static String createCategoryTableSql = "CREATE TABLE IF NOT EXISTS CATEGORIES(ID INT PRIMARY KEY, CATEGORY_NAME VARCHAR(40));";
    private final static String selectCategoryByIdSql = "SELECT * FROM CATEGORIES WHERE ID=?";
    /**
     * Default constructor.
     */
    public CategoryDao(){

    }

    /**
     *
     * @param connection to initialize the connection object.
     */
    public CategoryDao(Connection connection){
        this.connection = connection;
    }

    /**
     *
     * @return list of all categories.
     * @throws InternalServerException when connection issues or sql statement execution fails.
     */
    public List<Category> selectAllCategories() throws InternalServerException {
        List<Category> listOfAllCategories = new ArrayList<>();
        try {
            connection = super.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(selectAllCategoriesSql);
            while (resultSet.next()){
                int id = resultSet.getInt("ID");
                String name = resultSet.getString("CATEGORY_NAME");
                listOfAllCategories.add(new Category(id,name));
            }
            statement.close();
            connection.close();
            return listOfAllCategories;
        } catch (SQLException | InternalServerException e) {
            throw new InternalServerException(e.getMessage());
        }
    }

    /**
     *
     * @param category category class object.
     * @throws InternalServerException when connection issues or sql statement execution fails.
     */
    public void insertCategory(Category category) throws InternalServerException {
        try {
            connection = super.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(insertIntoCategoriesSql);
            preparedStatement.setInt(1,category.getId());
            preparedStatement.setString(2,category.getCategoryName());
            preparedStatement.execute();
            preparedStatement.close();
            connection.close();
        } catch (SQLException | InternalServerException e ){
            throw new InternalServerException(e.getMessage());
        }
    }

    /**
     *
     * @param category category class object.
     * @throws InternalServerException when connection issues or sql statement execution fails.
     */
    public void updateCategory(Category category) throws InternalServerException {

        try{
            connection = super.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(updateCategorySql);
            preparedStatement.setString(1,category.getCategoryName());
            preparedStatement.setInt(2,category.getId());
            preparedStatement.executeUpdate();
            preparedStatement.close();
            connection.close();

        } catch (SQLException | InternalServerException e) {
            throw new InternalServerException(e.getMessage());
        }
    }

    /**
     *
     * @param productIdObject category.
     * @throws InternalServerException when connection issues or sql statement execution fails.
     */
    public void deleteCategory(ProductId productIdObject) throws InternalServerException {
        try {
            connection = super.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(deleteCategorySql);
            preparedStatement.setInt(1,productIdObject.getId());;
            preparedStatement.executeUpdate();
            preparedStatement.close();
            connection.close();
        } catch (SQLException | InternalServerException e) {
            throw new InternalServerException(e.getMessage());
        }
    }
    /**
     * To find category.
     * @param id category product id.
     * @return true if cart product found else false.
     * @throws InternalServerException when connection issues or sql statement execution fails.
     */
    public boolean findCategoryById(int id) throws InternalServerException {
        try{
            connection = super.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(selectCategoryByIdSql);
            preparedStatement.setInt(1,id);
            ResultSet resultSet = preparedStatement.executeQuery();
            boolean result = resultSet.next();
            preparedStatement.close();
            connection.close();
            return result;
        } catch (SQLException e) {
            throw new InternalServerException(e.getMessage());
        } catch (InternalServerException e) {
            throw e;
        }
    }
}
