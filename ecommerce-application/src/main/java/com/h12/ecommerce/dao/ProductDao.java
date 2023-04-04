package com.h12.ecommerce.dao;

import com.h12.ecommerce.exceptions.InternalServerException;
import com.h12.ecommerce.exceptions.UserInputException;
import com.h12.ecommerce.models.Product;
import com.h12.ecommerce.models.ProductId;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

/**
 * Product dao class.
 */
@Repository
public class ProductDao extends MainDao{
    private Connection connection;
    private Statement statement;
    private PreparedStatement preparedStatement;
//    private final static String createProductTableSql = """
//                    CREATE TABLE IF NOT EXISTS PRODUCT
//                    (ID INT PRIMARY KEY,
//                    NAME VARCHAR(40) NOT NULL,
//                    COST INT,
//                    CATEGORY_ID INT);""";
    private final static String selectProductByIdSql = """
                SELECT * FROM PRODUCT
                WHERE ID=?;""";
    private final static String insertProductTableSql = """
                INSERT INTO PRODUCT
                (ID,NAME,COST,CATEGORY_ID)
                VALUES(?,?,?,?);""";
    private final static String updateProductTableSql = """
                UPDATE PRODUCT
                SET NAME = ?, COST = ?, CATEGORY_ID = ?
                WHERE ID = ?""";
    private final static String selectAllProductsSql = "SELECT * FROM PRODUCT;";
    private final static String deleteProductById = "DELETE FROM PRODUCT WHERE ID = ?";
    private final static String selectProductsByCategory = "SELECT * FROM PRODUCT WHERE CATEGORY_ID=?";

    public ProductDao(){
    }
    public ProductDao(Connection connection){
        this.connection = connection;
    }

//    /**
//     * To create table.
//     * @throws InternalServerException when connection issues or sql statement execution fails.
//     */
//    private void createProductTable() throws InternalServerException {
//        try{
//            connection = super.getConnection();
//            statement = connection.createStatement();
//            statement = connection.createStatement();
//            statement.execute(ProductDao.createProductTableSql);
//            connection.close();
//            statement.close();
//        } catch (SQLException | InternalServerException e) {
//            throw new InternalServerException(e.getMessage());
//        }
//    }

    /**
     *
     * @param product product class object.
     * @throws InternalServerException when connection issues or sql statement execution fails.
     */
    public void insertProduct(Product product) throws InternalServerException {
        try {
            connection = super.getConnection();
            preparedStatement = connection.prepareStatement(insertProductTableSql);
            preparedStatement.setInt(1, product.getProductId());
            preparedStatement.setString(2, product.getName());
            preparedStatement.setInt(3, product.getCost());
            preparedStatement.setInt(4, product.getCategoryId());
            preparedStatement.execute();
            preparedStatement.close();
            connection.close();
        } catch (SQLException | InternalServerException e) {
            throw new InternalServerException(e.getMessage());
        }
    }

    /**
     *
     * @param product product class object.
     * @throws InternalServerException when connection issues or sql statement execution fails.
     */
    public void updateRow(Product product) throws InternalServerException {
        try {
            connection = super.getConnection();
            preparedStatement = connection.prepareStatement(updateProductTableSql);
            preparedStatement.setString(1, product.getName());
            preparedStatement.setInt(2, product.getCost());
            preparedStatement.setInt(3, product.getCategoryId());
            preparedStatement.setInt(4, product.getProductId());
            preparedStatement.executeUpdate();
            preparedStatement.close();
            connection.close();
        } catch (SQLException | InternalServerException e) {
            throw new InternalServerException(e.getMessage());
        }
    }

    /**
     *
     * @param productId product id entity class.
     * @throws InternalServerException when connection issues or sql statement execution fails.
     */
    public void deleteProduct(ProductId productId) throws InternalServerException {
        try {
            connection = super.getConnection();
            preparedStatement = connection.prepareStatement(deleteProductById);
            preparedStatement.setInt(1, productId.getId());
            preparedStatement.executeUpdate();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            throw new InternalServerException(e.getMessage());
        }
    }

    /**
     *
     * @param productId product id.
     * @return product class object.
     * @throws InternalServerException when connection issues or sql statement execution fails.
     */
    public Product getProductDetailsById(int productId) throws InternalServerException, UserInputException {
        try {
            connection = super.getConnection();
            preparedStatement = connection.prepareStatement(selectProductByIdSql);
            preparedStatement.setInt(1, productId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String name = resultSet.getString("NAME");
                int cost = resultSet.getInt("COST");
                int categoryId = resultSet.getInt("CATEGORY_ID");
                preparedStatement.close();
                connection.close();
                return new Product(productId, name, cost, categoryId);
            }
            throw new UserInputException("No product in database.");
        } catch (NullPointerException | SQLException | InternalServerException e) {
            throw new InternalServerException(e.getMessage());
        }
    }

    /**
     *
     * @return list of product class objects.
     * @throws InternalServerException when connection issues or sql statement execution fails.
     */
    public List<Product> selectAllProducts() throws InternalServerException {
        try {
            connection = super.getConnection();
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(selectAllProductsSql);
            List<Product> productList = new ArrayList<>();
            while (resultSet.next()) {
                int productId = resultSet.getInt("ID");
                String name = resultSet.getString("NAME");
                int cost = resultSet.getInt("COST");
                int categoryId = resultSet.getInt("CATEGORY_ID");
                productList.add(new Product(productId, name, cost, categoryId));
            }
            connection.close();
            statement.close();
            return productList;
        } catch (SQLException | InternalServerException e) {
            throw  new InternalServerException(e.getMessage());
        }

    }

    /**
     * To get products by category.
     * @param id category id.
     * @return list of products
     * @throws InternalServerException when connection issues or sql statement execution fails.
     */
    public List<Product> getProductsByCategory(int id) throws InternalServerException {
        List<Product> productList = new ArrayList<>();
        try {
            connection = super.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(selectProductsByCategory);
            preparedStatement.setInt(1,id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                productList.add(new Product(resultSet.getInt("ID"),resultSet.getString("NAME"),resultSet.getInt("COST"),id));
            }
            return productList;
        } catch (SQLException | InternalServerException e) {
            throw new InternalServerException(e.getMessage());
        }
    }
}
