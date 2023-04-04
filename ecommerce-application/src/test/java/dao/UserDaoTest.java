package dao;

import com.h12.ecommerce.dao.UserDao;
import com.h12.ecommerce.exceptions.InternalServerException;
import com.h12.ecommerce.exceptions.UserInputException;
import com.h12.ecommerce.models.User;
import com.h12.ecommerce.models.UserId;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class UserDaoTest {
    private final static String selectUsersSql = "SELECT * FROM USERS;";
    private final static String selectUserByIdSql ="SELECT * FROM USERS WHERE ID = ?;";
    private final static String insertUserSql = "INSERT INTO USERS(ID,NAME) VALUES(?,?);";
    private final static String deleteUserSql = "DELETE FROM USERS WHERE ID = ?";
    private final static String updateUserSql = "UPDATE USERS SET NAME = ? WHERE ID = ?";
    Connection mockConnection;
    Statement statement;
    ResultSet resultSet;
    PreparedStatement preparedStatement;
    UserDao userDao;
    MockedStatic<DriverManager> mockedDriver;

    @BeforeEach
    void mockAll() {
        mockConnection = mock(Connection.class);
        userDao = new UserDao();
        statement = mock(Statement.class);
        resultSet = mock(ResultSet.class);
        preparedStatement = mock(PreparedStatement.class);
        try {
            mockedDriver = Mockito.mockStatic(DriverManager.class);
        }
        catch (Exception e){
//            System.out.println(e.getMessage());
        }
        Mockito.reset();
    }

    @Test
    public void getUsersTest() throws SQLException, InternalServerException {
        Mockito.when(DriverManager.getConnection(anyString(),anyString(),anyString())).thenReturn(mockConnection);
        when(mockConnection.createStatement()).thenReturn(statement);
        when(statement.executeQuery(selectUsersSql)).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true,false);
        when(resultSet.getInt(anyString())).thenReturn(1);
        when(resultSet.getString(anyString())).thenReturn("harish");
        List<User> actualListOfUsers = userDao.getUsers();
        assertEquals(1,actualListOfUsers.size());
    }
    @Test
    public void getUsersNegativeTest() throws SQLException, InternalServerException {
        Mockito.when(DriverManager.getConnection(anyString(),anyString(),anyString())).thenReturn(mockConnection);
        when(mockConnection.createStatement()).thenReturn(statement);
        doThrow(SQLException.class).when(statement).executeQuery(selectUsersSql);
        assertThrows(InternalServerException.class,userDao::getUsers);
    }

    @Test
    public void getUsersByIdTest() throws SQLException, UserInputException, InternalServerException {
        Mockito.when(DriverManager.getConnection(anyString(),anyString(),anyString())).thenReturn(mockConnection);
        when(mockConnection.prepareStatement(selectUserByIdSql)).thenReturn(preparedStatement);
        doNothing().when(preparedStatement).setInt(anyInt(),anyInt());
        doNothing().when(preparedStatement).setString(anyInt(),anyString());
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getString(anyString())).thenReturn("harish");
        User user = userDao.getUserById(1);
        assertEquals("harish",user.getName());
    }
    @Test
    public void getUsersByIdNegativeTest() throws SQLException {
        Mockito.when(DriverManager.getConnection(anyString(),anyString(),anyString())).thenReturn(mockConnection);
        when(mockConnection.prepareStatement(selectUserByIdSql)).thenReturn(preparedStatement);
        doThrow(SQLException.class).when(preparedStatement).executeQuery();
        assertThrows(InternalServerException.class,()->{
            userDao.getUserById(1);
        });
    }
    @Test
    public void createTest() throws SQLException, InternalServerException {
        User user = new User(1,"harish");
        Mockito.when(DriverManager.getConnection(anyString(),anyString(),anyString())).thenReturn(mockConnection);
        when(mockConnection.prepareStatement(insertUserSql)).thenReturn(preparedStatement);
        doNothing().when(preparedStatement).setInt(anyInt(),anyInt());
        doNothing().when(preparedStatement).setString(anyInt(),anyString());
        userDao.create(user);
        verify(preparedStatement,times(1)).execute();
    }

    @Test
    public void createNegativeTest() throws SQLException, InternalServerException {
        User user = new User(1,"harish");
        Mockito.when(DriverManager.getConnection(anyString(),anyString(),anyString())).thenReturn(mockConnection);
        when(mockConnection.prepareStatement(insertUserSql)).thenReturn(preparedStatement);
        doNothing().when(preparedStatement).setInt(anyInt(),anyInt());
        doNothing().when(preparedStatement).setString(anyInt(),anyString());
        doThrow(SQLException.class).when(preparedStatement).execute();
        assertThrows(InternalServerException.class,()->{
            userDao.create(user);
        });
    }
    @Test
    public void deleteUserTest() throws SQLException, InternalServerException {
        UserId userId = new UserId();
        userId.setId(1);
        Mockito.when(DriverManager.getConnection(anyString(),anyString(),anyString())).thenReturn(mockConnection);
        when(mockConnection.prepareStatement(deleteUserSql)).thenReturn(preparedStatement);
        doNothing().when(preparedStatement).setInt(anyInt(),anyInt());
        doNothing().when(preparedStatement).setString(anyInt(),anyString());
        userDao.deleteUser(userId);
        verify(preparedStatement,times(1)).executeUpdate();
    }

    @Test
    public void deleteUserNegativeTest() throws SQLException, InternalServerException {
        UserId userId = new UserId();
        userId.setId(1);
        Mockito.when(DriverManager.getConnection(anyString(),anyString(),anyString())).thenReturn(mockConnection);
        when(mockConnection.prepareStatement(deleteUserSql)).thenReturn(preparedStatement);
        doNothing().when(preparedStatement).setInt(anyInt(),anyInt());
        doNothing().when(preparedStatement).setString(anyInt(),anyString());
        doThrow(SQLException.class).when(preparedStatement).executeUpdate();
        assertThrows(InternalServerException.class,()->{
            userDao.deleteUser(userId);
        });
    }
    @Test
    public void updateUserTest() throws SQLException, InternalServerException {
        User user = new User(1,"harish");
        Mockito.when(DriverManager.getConnection(anyString(),anyString(),anyString())).thenReturn(mockConnection);
        when(mockConnection.prepareStatement(updateUserSql)).thenReturn(preparedStatement);
        doNothing().when(preparedStatement).setInt(anyInt(),anyInt());
        doNothing().when(preparedStatement).setString(anyInt(),anyString());
        userDao.updateUser(user);
        verify(preparedStatement,times(1)).executeUpdate();
    }
    @Test
    public void updateUserNegativeTest() throws SQLException, InternalServerException {
        User user = new User(1,"harish");
        Mockito.when(DriverManager.getConnection(anyString(),anyString(),anyString())).thenReturn(mockConnection);
        when(mockConnection.prepareStatement(updateUserSql)).thenReturn(preparedStatement);
        doNothing().when(preparedStatement).setInt(anyInt(),anyInt());
        doNothing().when(preparedStatement).setString(anyInt(),anyString());
        doThrow(SQLException.class).when(preparedStatement).executeUpdate();
        assertThrows(InternalServerException.class,()->{
            userDao.updateUser(user);
        });
    }
}
