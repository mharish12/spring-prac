package dao;

import com.h12.ecommerce.dao.ProductDao;
import com.h12.ecommerce.exceptions.InternalServerException;
import com.h12.ecommerce.exceptions.UserInputException;
import com.h12.ecommerce.models.Product;
import com.h12.ecommerce.models.ProductId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class ProductDaoTest {
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
    private final static String deleteProductByIdSql = "DELETE FROM PRODUCT WHERE ID = ?";

    Connection mockConnection;
    Statement statement;
    ResultSet resultSet;
    PreparedStatement preparedStatement;
    ProductDao productDao;
    MockedStatic<DriverManager> mockedDriver;

    @BeforeEach
    void mockAll() {
        mockConnection = mock(Connection.class);
        productDao = new ProductDao(mockConnection);
        statement = mock(Statement.class);
        resultSet = mock(ResultSet.class);
        preparedStatement = mock(PreparedStatement.class);
        try {
            mockedDriver = Mockito.mockStatic(DriverManager.class);
        }
        catch (Exception ignored){
        }
        Mockito.reset();
    }

    @Test
    public void constructorTest(){
        ProductDao productDao =new ProductDao();
    }

    @Test
    public void insertProductTest() throws SQLException, InternalServerException {
        Product product = new Product(1,"apple",100,1);
        Mockito.when(DriverManager.getConnection(anyString(),anyString(),anyString())).thenReturn(mockConnection);
        Mockito.when(mockConnection.prepareStatement(insertProductTableSql)).thenReturn(preparedStatement);
        doNothing().when(preparedStatement).setInt(anyInt(),anyInt());
//        Mockito.when(resultSet).getInt(anyString()).;
        doNothing().when(preparedStatement).setString(anyInt(),anyString());
        productDao.insertProduct(product);
        verify(preparedStatement,times(1)).execute();
    }
    @Test
    public void insertProductNegativeTest() throws InternalServerException, SQLException {
        Product product = new Product(1,"apple",100,1);
        Mockito.when(DriverManager.getConnection(anyString(),anyString(),anyString())).thenReturn(mockConnection);
        Mockito.when(mockConnection.prepareStatement(insertProductTableSql)).thenReturn(preparedStatement);
        doNothing().when(preparedStatement).setInt(anyInt(),anyInt());
//        Mockito.when(resultSet).getInt(anyString()).;
        doNothing().when(preparedStatement).setString(anyInt(),anyString());
        doThrow(SQLException.class).when(preparedStatement).execute();
        assertThrows(InternalServerException.class,()->{
            productDao.insertProduct(product);
        });
    }

    @Test
    public void updateRowTest() throws SQLException, InternalServerException {
        Product product = new Product(1,"apple",100,1);
        Mockito.when(DriverManager.getConnection(anyString(),anyString(),anyString())).thenReturn(mockConnection);
        Mockito.when(mockConnection.prepareStatement(updateProductTableSql)).thenReturn(preparedStatement);
        doNothing().when(preparedStatement).setInt(anyInt(),anyInt());
        doNothing().when(preparedStatement).setString(anyInt(),anyString());
        productDao.updateRow(product);
        verify(preparedStatement,times(1)).executeUpdate();
        doThrow(SQLException.class).when(preparedStatement).executeUpdate();
        assertThrows(InternalServerException.class,()->{
            productDao.updateRow(product);
        });
    }
    @Test
    public void updateRowNegativeTest() throws SQLException {
        Product product = new Product(1,"apple",100,1);
        Mockito.when(DriverManager.getConnection(anyString(),anyString(),anyString())).thenReturn(mockConnection);
        Mockito.when(mockConnection.prepareStatement(updateProductTableSql)).thenReturn(preparedStatement);
        doNothing().when(preparedStatement).setInt(anyInt(),anyInt());
        doNothing().when(preparedStatement).setString(anyInt(),anyString());
        doThrow(SQLException.class).when(preparedStatement).executeUpdate();
        assertThrows(InternalServerException.class,()->{
            productDao.updateRow(product);
        });
    }

    @Test
    public void deleteRowTest() throws SQLException, InternalServerException {
        int id =1;
        ProductId productId = new ProductId(id);
        Mockito.when(DriverManager.getConnection(anyString(),anyString(),anyString())).thenReturn(mockConnection);
        Mockito.when(mockConnection.prepareStatement(deleteProductByIdSql)).thenReturn(preparedStatement);
        doNothing().when(preparedStatement).setInt(anyInt(),anyInt());
        productDao.deleteProduct(productId);
        verify(preparedStatement,times(1)).executeUpdate();
        doThrow(SQLException.class).when(preparedStatement).executeUpdate();
        assertThrows(InternalServerException.class,()->{
            productDao.deleteProduct(productId);
        });
    }

    @Test
    public void deleteRowNegativeTest() throws SQLException {
        int id =1;
        ProductId productId = new ProductId(id);
        Mockito.when(DriverManager.getConnection(anyString(),anyString(),anyString())).thenReturn(mockConnection);
        Mockito.when(mockConnection.prepareStatement(deleteProductByIdSql)).thenReturn(preparedStatement);
        doNothing().when(preparedStatement).setInt(anyInt(),anyInt());
        doThrow(SQLException.class).when(preparedStatement).executeUpdate();
        assertThrows(InternalServerException.class,()->{
            productDao.deleteProduct(productId);
        });
    }

    @Test
    public void  getProductDetailsByIdTest() throws SQLException, InternalServerException, UserInputException {
        int id =1;
        Mockito.when(DriverManager.getConnection(anyString(),anyString(),anyString())).thenReturn(mockConnection);
        Mockito.when(mockConnection.prepareStatement(selectProductByIdSql)).thenReturn(preparedStatement);
        Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);
        doNothing().when(preparedStatement).setInt(anyInt(),anyInt());
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt(anyString())).thenReturn(100,1);
        when(resultSet.getString(anyString())).thenReturn("apple");
        Product actualProduct = productDao.getProductDetailsById(id);
        verify(preparedStatement,times(1)).executeQuery();
        assertEquals(id,actualProduct.getProductId());
    }

    @Test
    public void getProductDetailsByIdNegativeTest() throws SQLException {
        int id =1;
        Mockito.when(DriverManager.getConnection(anyString(),anyString(),anyString())).thenReturn(mockConnection);
        Mockito.when(mockConnection.prepareStatement(selectProductByIdSql)).thenReturn(preparedStatement);
        doNothing().when(preparedStatement).setInt(anyInt(),anyInt());
        assertThrows(InternalServerException.class,()->{
            productDao.getProductDetailsById(id);
        });
        doThrow(SQLException.class).when(preparedStatement).executeQuery();
        assertThrows(InternalServerException.class,()->{
            productDao.getProductDetailsById(id);
        });
    }

    @Test
    public void selectAllProductsTest() throws SQLException, InternalServerException {
        Mockito.when(DriverManager.getConnection(anyString(),anyString(),anyString())).thenReturn(mockConnection);
        Mockito.when(mockConnection.createStatement()).thenReturn(statement);
        Mockito.when(statement.executeQuery(selectAllProductsSql)).thenReturn(resultSet);
        List<Product> actualListOfProducts = productDao.selectAllProducts();
        assertEquals(0,actualListOfProducts.size());
        doThrow(SQLException.class).when(statement).executeQuery(selectAllProductsSql);
        assertThrows(InternalServerException.class,()->{
            productDao.selectAllProducts();
        });
    }

    @Test
    public void selectAllProductsNegativeTest() throws SQLException, InternalServerException {
        Mockito.when(DriverManager.getConnection(anyString(),anyString(),anyString())).thenReturn(mockConnection);
        Mockito.when(mockConnection.createStatement()).thenReturn(statement);
        Mockito.when(statement.executeQuery(selectAllProductsSql)).thenReturn(resultSet);
        doThrow(SQLException.class).when(statement).executeQuery(selectAllProductsSql);
        assertThrows(InternalServerException.class,()->{
            productDao.selectAllProducts();
        });
    }

}
