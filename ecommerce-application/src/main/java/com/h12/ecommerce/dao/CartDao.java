package com.h12.ecommerce.dao;

import com.h12.ecommerce.exceptions.InternalServerException;
import com.h12.ecommerce.models.Cart;
import com.h12.ecommerce.models.CartProductDelete;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Cart Dao.
 */
@Repository
public class CartDao extends MainDao{

    private Connection connection;
    private final static String selectCartByUserSql = "SELECT * FROM CART WHERE USERID = ?;";
//    private final static String createCartTableSql = """
//                                                    CREATE TABLE IF NOT EXISTS CART
//                                                    (PRODUCT_ID INT PRIMARY KEY,
//                                                    QUANTITY INT NOT NULL,
//                                                    USERID INT NOT NULL);""";
    private final static String insertIntoCartSql = """
                INSERT INTO CART(PRODUCT_ID,QUANTITY,USERID)
                VALUES( ?, ?, ?);""";
    private final static String deleteProductSql = """
                DELETE FROM CART
                WHERE PRODUCT_ID = ? AND USERID = ?;""";
    private final static String updateCartSql = """
                UPDATE CART
                SET QUANTITY = ?
                WHERE PRODUCT_ID = ? AND USERID = ?;""";
    private final static String selectCartProductByIdSql = """
            SELECT * FROM CART
            WHERE PRODUCT_ID=?;""";

    /**
     * Default constructor.
     */
    public CartDao(){

    }

    /**
     *
     * @param connection connection object to database.
     */
    public CartDao(Connection connection){
        this.connection = connection;
    }

//    /**
//     * To create table if not exists.
//     * @throws InternalServerException when connection issues or sql statement execution fails.
//     */
//    private void createCartTable() throws InternalServerException {
//        try {
//            connection = super.getConnection();
//            Statement statement = connection.createStatement();
//            statement.execute(createCartTableSql);
//            statement.close();
//            connection.close();
//        } catch (SQLException | InternalServerException e) {
//            throw new InternalServerException(e.getMessage());
//        }
//    }


    /**
     * TO get cart details.
     * @return cart product details in list of lists.
     * @throws InternalServerException when connection issues or sql statement execution fails.
     */
    public List<List<Integer>> getCartDetails(int userId) throws InternalServerException {
        try {
            connection = super.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(selectCartByUserSql);
            preparedStatement.setInt(1,userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<List<Integer>> cartList = new ArrayList<>();
            while (resultSet.next()) {
                int productId = resultSet.getInt("PRODUCT_ID");
                int quantity = resultSet.getInt("QUANTITY");
                List<Integer> cartProductDetails = new ArrayList<>();
                cartProductDetails.add(productId);
                cartProductDetails.add(quantity);
                cartProductDetails.add(userId);
                cartList.add(cartProductDetails);
            }
            preparedStatement.close();
            connection.close();
            return cartList;
        } catch (SQLException | InternalServerException e) {
            throw new InternalServerException(e.getMessage());
        }
    }

    /**
     * To add product into database.
     * @param cart cart product.
     * @return true if product added into cart else false.
     * @throws InternalServerException when connection issues or sql statement execution fails.
     */
    public boolean addProduct(Cart cart) throws InternalServerException {
        try {
            connection = super.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(insertIntoCartSql);
            preparedStatement.setInt(1, cart.getProductId());
            preparedStatement.setInt(2, cart.getQuantity());
            preparedStatement.setInt(3,cart.getUserId());
            boolean status = preparedStatement.execute();
            preparedStatement.close();
            connection.close();
            return status;
        } catch (SQLException | InternalServerException e) {
            throw new InternalServerException(e.getMessage());
        }


    }

    /**
     * To delete product.
     * @param productIdObject product id.
     * @return delete status.
     * @throws InternalServerException when connection issues or sql statement execution fails.
     */
    public int deleteProduct(CartProductDelete productIdObject) throws InternalServerException {
        try {
            connection = super.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(deleteProductSql);
            preparedStatement.setInt(1, productIdObject.getId());
            preparedStatement.setInt(2,productIdObject.getUserId());
            int status = preparedStatement.executeUpdate();
            preparedStatement.close();
            connection.close();
            return status;
        } catch (SQLException | InternalServerException e) {
            throw new InternalServerException(e.getMessage());
        }
    }

    /**
     * Updates product in cart.
     * @param cart cart product.
     * @return update status.
     * @throws InternalServerException when connection issues or sql statement execution fails.
     */
    public int updateProduct(Cart cart) throws InternalServerException {
        try{
            connection = super.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(updateCartSql);
            preparedStatement.setInt(1,cart.getQuantity());
            preparedStatement.setInt(2,cart.getProductId());
            preparedStatement.setInt(3,cart.getUserId());
            int status = preparedStatement.executeUpdate();
            preparedStatement.close();
            connection.close();
            return status;
        } catch (InternalServerException | SQLException e) {
            throw new InternalServerException(e.getMessage());
        }
    }

    /**
     * To find category.
     * @param productId cart product Id.
     * @return true if cart product found else false.
     * @throws InternalServerException when connection issues or sql statement execution fails.
     */
    public boolean findCartProductById(int productId) throws InternalServerException {
        try{
            connection = super.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(selectCartProductByIdSql);
            preparedStatement.setInt(1,productId);
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
