package dao;

import com.h12.ecommerce.dao.CartDao;
import com.h12.ecommerce.dao.ProductDao;
import com.h12.ecommerce.exceptions.InternalServerException;
import com.h12.ecommerce.models.Cart;
import com.h12.ecommerce.models.CartProductDelete;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class CartDaoTest {
    private final static String selectCartByUserSql = "SELECT * FROM CART WHERE USERID = ?;";
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
    Connection mockConnection;
    Statement statement;
    ResultSet resultSet;
    PreparedStatement preparedStatement;
    ProductDao productDao;
    MockedStatic<DriverManager> mockedDriver;
    CartDao cartDao;

    @BeforeEach
    void mockAll() {
        mockConnection = mock(Connection.class);
        productDao = new ProductDao();
        statement = mock(Statement.class);
        resultSet = mock(ResultSet.class);
        preparedStatement = mock(PreparedStatement.class);
        cartDao = new CartDao(mockConnection);
        try {
            mockedDriver = Mockito.mockStatic(DriverManager.class);
        }
        catch (Exception e){
//            System.out.println(e.getMessage());
        }
        Mockito.reset();
    }

    @Test
    public void constructorTest(){
        CartDao cartDao = new CartDao();
    }
    @Test
    public void getCartDetailsPositiveTest() throws SQLException, InternalServerException {
        List<List<Integer>> expectedListOfCartDetails = new ArrayList<>();
//        expectedListOfCartDetails.add(Arrays.asList(1,2));
        Mockito.when(DriverManager.getConnection(anyString(),anyString(),anyString())).thenReturn(mockConnection);
        Mockito.when(mockConnection.prepareStatement(selectCartByUserSql)).thenReturn(preparedStatement);
        doNothing().when(preparedStatement).setInt(anyInt(),anyInt());
        doNothing().when(preparedStatement).setString(anyInt(),anyString());
        Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);
        List<List<Integer>> actualListOfCartDetails = cartDao.getCartDetails(1);
        assertEquals(expectedListOfCartDetails.size(),actualListOfCartDetails.size());
    }
    @Test
    public void getCartDetailsNegativeTest() throws SQLException {
        Mockito.when(DriverManager.getConnection(anyString(),anyString(),anyString())).thenReturn(mockConnection);
        Mockito.when(mockConnection.prepareStatement(selectCartByUserSql)).thenReturn(preparedStatement);
        doNothing().when(preparedStatement).setInt(anyInt(),anyInt());
        doNothing().when(preparedStatement).setString(anyInt(),anyString());
        doThrow(SQLException.class).when(preparedStatement).executeQuery();
        assertThrows(InternalServerException.class,()->{
            cartDao.getCartDetails(1);
        });
    }
    @Test
    public void addProductPositiveTest() throws SQLException, InternalServerException {
        Cart cart = new Cart(1,1,1);
        Mockito.when(DriverManager.getConnection(anyString(),anyString(),anyString())).thenReturn(mockConnection);
        Mockito.when(mockConnection.prepareStatement(insertIntoCartSql)).thenReturn(preparedStatement);
        doNothing().when(preparedStatement).setInt(anyInt(),anyInt());
        Mockito.when(preparedStatement.execute()).thenReturn(true);
        boolean actualStatus = cartDao.addProduct(cart);
        assertTrue(actualStatus);
    }
    @Test
    public void addProductFailureTest() throws SQLException {
        Cart cart = new Cart(1,1,1);
        Mockito.when(DriverManager.getConnection(anyString(),anyString(),anyString())).thenReturn(mockConnection);
        Mockito.when(mockConnection.prepareStatement(insertIntoCartSql)).thenReturn(preparedStatement);
        doNothing().when(preparedStatement).setInt(anyInt(),anyInt());
        doThrow(SQLException.class).when(preparedStatement).execute();
        assertThrows(InternalServerException.class,()->{
            cartDao.addProduct(cart);
        });
    }
    @Test
    public void deleteProductPositiveTest() throws SQLException, InternalServerException {
        Mockito.when(DriverManager.getConnection(anyString(),anyString(),anyString())).thenReturn(mockConnection);
        Mockito.when(mockConnection.prepareStatement(deleteProductSql)).thenReturn(preparedStatement);
        doNothing().when(preparedStatement).setInt(anyInt(),anyInt());
        Mockito.when(preparedStatement.executeUpdate()).thenReturn(1);
        CartProductDelete productIdObject = new CartProductDelete(1,1);
        int actualStatus = cartDao.deleteProduct(productIdObject);
        assertEquals(1,actualStatus);
    }
    @Test
    public void deleteProductNegativeTest() throws SQLException, InternalServerException {
        Mockito.when(DriverManager.getConnection(anyString(), anyString(), anyString())).thenReturn(mockConnection);
        Mockito.when(mockConnection.prepareStatement(deleteProductSql)).thenReturn(preparedStatement);
        doNothing().when(preparedStatement).setInt(anyInt(), anyInt());
        doThrow(SQLException.class).when(preparedStatement).executeUpdate();
        CartProductDelete productIdObject = new CartProductDelete(1,1);
        assertThrows(InternalServerException.class,()->{
           cartDao.deleteProduct(productIdObject);
        });
    }
    @Test
    public void updateProductPositiveTest() throws SQLException, InternalServerException {
        Cart cart = new Cart(1,1,1);
        Mockito.when(DriverManager.getConnection(anyString(),anyString(),anyString())).thenReturn(mockConnection);
        Mockito.when(mockConnection.prepareStatement(updateCartSql)).thenReturn(preparedStatement);
        doNothing().when(preparedStatement).setInt(anyInt(),anyInt());
        Mockito.when(preparedStatement.executeUpdate()).thenReturn(1);
        int actualStatus = cartDao.updateProduct(cart);
        assertEquals(1,actualStatus);
    }
    @Test
    public void updateProductNegativeTest() throws SQLException {
        Cart cart = new Cart(1,1,1);
        Mockito.when(DriverManager.getConnection(anyString(),anyString(),anyString())).thenReturn(mockConnection);
        Mockito.when(mockConnection.prepareStatement(updateCartSql)).thenReturn(preparedStatement);
        doNothing().when(preparedStatement).setInt(anyInt(),anyInt());
        doThrow(SQLException.class).when(preparedStatement).executeUpdate();
        assertThrows(InternalServerException.class,()->{
            cartDao.updateProduct(cart);
        });
    }
    @Test
    public void findCartProductByIdTest() throws SQLException, InternalServerException {
        Mockito.when(DriverManager.getConnection(anyString(),anyString(),anyString())).thenReturn(mockConnection);
        Mockito.when(mockConnection.prepareStatement(selectCartProductByIdSql)).thenReturn(preparedStatement);
        doNothing().when(preparedStatement).setInt(anyInt(),anyInt());
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        assertTrue(cartDao.findCartProductById(1));
    }

    @Test
    public void findCartProductByIdNegativeTest() throws SQLException {
        Mockito.when(DriverManager.getConnection(anyString(),anyString(),anyString())).thenReturn(mockConnection);
        Mockito.when(mockConnection.prepareStatement(selectCartProductByIdSql)).thenReturn(preparedStatement);
        doNothing().when(preparedStatement).setInt(anyInt(),anyInt());
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        doThrow(SQLException.class).when(preparedStatement).executeQuery();
        assertThrows(InternalServerException.class,()->{
            cartDao.findCartProductById(1);
        });
    }
}
