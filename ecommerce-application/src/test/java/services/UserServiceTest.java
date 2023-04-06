package services;

import com.h12.ecommerce.dao.UserDao;
import com.h12.ecommerce.exceptions.InternalServerException;
import com.h12.ecommerce.exceptions.UserInputException;
import com.h12.ecommerce.models.User;
import com.h12.ecommerce.models.UserId;
import com.h12.ecommerce.services.UserService;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class UserServiceTest {
    private UserDao mockUserDao = mock(UserDao.class);
    private UserService userService = new UserService(mockUserDao);
    @Test
    public void getUsersTest() throws UserInputException, InternalServerException {
        List<User> expectedUserList = new ArrayList<>();
        expectedUserList.add(new User(1,"harish"));
        doReturn(expectedUserList).when(mockUserDao).getUsers();
        List<User> actualUsersList = userService.getUsers();
        assertEquals(expectedUserList.size(),actualUsersList.size());
    }
    @Test
    public void getUserByIdTest() throws UserInputException, InternalServerException {
        User expectedUser = new User(1,"harish");
        doReturn(expectedUser).when(mockUserDao).getUserById(anyInt());
        User actualUser = userService.getUserById(1);
        assertEquals(expectedUser.getId(),actualUser.getId());
        assertEquals(expectedUser.getName(),actualUser.getName());
    }
    @Test
    public void getUserByIdNegativeTest(){
        int id = -1;
        assertThrows(UserInputException.class,()->{
           userService.getUserById(id);
        });
    }
    @Test
    public void createUserTest() throws UserInputException, InternalServerException {
        User expectedUser = new User(1,"harish");
        userService.createUser(expectedUser);
        verify(mockUserDao,times(1)).create(expectedUser);
    }
    @Test
    public void createUserNegativeTest(){
        User expectedUser1 = new User(-1,"harish");
        assertThrows(UserInputException.class,()->{
            userService.createUser(expectedUser1);
        });
        User expectedUser2 = new User(1,"");
        assertThrows(UserInputException.class,()->{
            userService.createUser(expectedUser2);
        });
    }

    @Test
    public void deleteUserTest() throws UserInputException, InternalServerException {
        UserId userId = new UserId();
        userId.setId(1);
        userService.deleteUser(userId);
        verify(mockUserDao,times(1)).deleteUser(userId);
    }

    @Test
    public void deleteUserNegativeTest(){
        UserId userId = new UserId();
        userId.setId(-1);
        assertThrows(UserInputException.class,()->{
            userService.deleteUser(userId);
        });
    }
    @Test
    public void updateUserTest() throws UserInputException, InternalServerException {
        User user = new User(1,"harish");
        userService.updateUser(user);
        verify(mockUserDao,times(1)).updateUser(user);
    }
    @Test
    public void updateUserNegativeTest(){
        User expectedUser1 = new User(-1,"harish");
        assertThrows(UserInputException.class,()->{
            userService.updateUser(expectedUser1);
        });
        User expectedUser2 = new User(1,"");
        assertThrows(UserInputException.class,()->{
            userService.updateUser(expectedUser2);
        });
    }
}
