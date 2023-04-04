package dao;


import com.h12.ecommerce.dao.CategoryDao;
import com.h12.ecommerce.dao.ProductDao;
import com.h12.ecommerce.exceptions.InternalServerException;
import com.h12.ecommerce.models.Category;
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

public class CategoryDaoTest {
    private final static String selectAllCategoriesSql = "SELECT * FROM CATEGORIES;";
    private final static String insertIntoCategoriesSql = "INSERT INTO CATEGORIES(ID,CATEGORY_NAME) VALUES(?,?);";
    private final static String updateCategorySql = "UPDATE CATEGORIES SET CATEGORY_NAME= ? WHERE ID= ?;";
    private final static String deleteCategorySql = "DELETE FROM CATEGORIES WHERE ID = ?;";
    Connection mockConnection;
    Statement statement;
    ResultSet resultSet;
    PreparedStatement preparedStatement;
    ProductDao productDao;
    MockedStatic<DriverManager> mockedDriver;

    CategoryDao categoryDao;
    @BeforeEach
    void mockAll() {
        mockConnection = mock(Connection.class);
        productDao = new ProductDao();
        statement = mock(Statement.class);
        resultSet = mock(ResultSet.class);
        categoryDao = new CategoryDao(mockConnection);
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
        CategoryDao categoryDao = new CategoryDao();
    }
    @Test
    public void selectAllCategoriesTest() throws SQLException, InternalServerException {
//        List<List<Integer>> expectedListOfCartDetails = new ArrayList<>();
        Mockito.when(DriverManager.getConnection(anyString(),anyString(),anyString())).thenReturn(mockConnection);
        Mockito.when(mockConnection.createStatement()).thenReturn(statement);
        Mockito.when(statement.executeQuery(selectAllCategoriesSql)).thenReturn(resultSet);
        List<Category> actualListOfCategories = categoryDao.selectAllCategories();
        assertEquals(0,actualListOfCategories.size());
    }
    @Test
    public void selectAllCategoriesNegativeTest() throws SQLException {
        Mockito.when(DriverManager.getConnection(anyString(),anyString(),anyString())).thenReturn(mockConnection);
        Mockito.when(mockConnection.createStatement()).thenReturn(statement);
        doThrow(SQLException.class).when(statement).executeQuery(selectAllCategoriesSql);
        assertThrows(InternalServerException.class,()->{
            categoryDao.selectAllCategories();
        });
    }
    @Test
    public void insertCategoryTest() throws SQLException, InternalServerException {
        Category category = new Category(1,"grocery");
        Mockito.when(DriverManager.getConnection(anyString(),anyString(),anyString())).thenReturn(mockConnection);
        Mockito.when(mockConnection.prepareStatement(insertIntoCategoriesSql)).thenReturn(preparedStatement);
        doNothing().when(preparedStatement).setInt(anyInt(),anyInt());
        doNothing().when(preparedStatement).setString(anyInt(),anyString());
        categoryDao.insertCategory(category);
        verify(preparedStatement,times(1)).execute();
        doThrow(SQLException.class).when(preparedStatement).execute();
        assertThrows(InternalServerException.class,()->{
            categoryDao.insertCategory(category);
        });
    }

    @Test
    public void insertCategoryNegativeTest() throws SQLException {
        Category category = new Category(1,"grocery");
        Mockito.when(DriverManager.getConnection(anyString(),anyString(),anyString())).thenReturn(mockConnection);
        Mockito.when(mockConnection.prepareStatement(insertIntoCategoriesSql)).thenReturn(preparedStatement);
        doNothing().when(preparedStatement).setInt(anyInt(),anyInt());
        doNothing().when(preparedStatement).setString(anyInt(),anyString());
        doThrow(SQLException.class).when(preparedStatement).execute();
        assertThrows(InternalServerException.class,()->{
            categoryDao.insertCategory(category);
        });
    }

    @Test
    public void updateCategoryTest() throws SQLException, InternalServerException {
        Category category = new Category(1,"grocery");
        Mockito.when(DriverManager.getConnection(anyString(),anyString(),anyString())).thenReturn(mockConnection);
        Mockito.when(mockConnection.prepareStatement(updateCategorySql)).thenReturn(preparedStatement);
        doNothing().when(preparedStatement).setInt(anyInt(),anyInt());
        doNothing().when(preparedStatement).setString(anyInt(),anyString());
        categoryDao.updateCategory(category);
        verify(preparedStatement,times(1)).executeUpdate();
    }

    @Test
    public void updateCategoryNegativeTest() throws SQLException {
        Category category = new Category(1,"grocery");
        Mockito.when(DriverManager.getConnection(anyString(),anyString(),anyString())).thenReturn(mockConnection);
        Mockito.when(mockConnection.prepareStatement(updateCategorySql)).thenReturn(preparedStatement);
        doNothing().when(preparedStatement).setInt(anyInt(),anyInt());
        doNothing().when(preparedStatement).setString(anyInt(),anyString());
        doThrow(SQLException.class).when(preparedStatement).executeUpdate();
        assertThrows(InternalServerException.class,()->{
           categoryDao.updateCategory(category);
        });
    }

    @Test
    public void deleteCategoryTest() throws SQLException, InternalServerException {
        int id =1;
        ProductId productId = new ProductId(id);
        Mockito.when(DriverManager.getConnection(anyString(),anyString(),anyString())).thenReturn(mockConnection);
        Mockito.when(mockConnection.prepareStatement(deleteCategorySql)).thenReturn(preparedStatement);
        doNothing().when(preparedStatement).setInt(anyInt(),anyInt());
        categoryDao.deleteCategory(productId);
        verify(preparedStatement,times(1)).executeUpdate();
    }

    @Test
    public void  deleteCategoryNegativeTest() throws SQLException {
        int id =1;
        ProductId productId = new ProductId(id);
        Mockito.when(DriverManager.getConnection(anyString(),anyString(),anyString())).thenReturn(mockConnection);
        Mockito.when(mockConnection.prepareStatement(deleteCategorySql)).thenReturn(preparedStatement);
        doNothing().when(preparedStatement).setInt(anyInt(),anyInt());
        doThrow(SQLException.class).when(preparedStatement).executeUpdate();
        assertThrows(InternalServerException.class,()->{
            categoryDao.deleteCategory(productId);
        });
    }
}
