package services;

import com.zs.assignment_final.dao.CartDao;
import com.zs.assignment_final.models.*;
import com.zs.assignment_final.exceptions.UserInputException;
import com.zs.assignment_final.exceptions.InternalServerException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

public class CartServiceTest {

    CartDao mockCartDao;
    @InjectMocks
    ProductService mockProductService;

    CartService cartService;
    @BeforeEach
    public void mockingAll(){
        mockCartDao = mock(CartDao.class);
        mockProductService = mock(ProductService.class);
        cartService = new CartService(mockCartDao,mockProductService);
        Mockito.reset();
    }

    @Test
    public void constructorTest(){
        CartService cartService = new CartService();
    }
    @Test
    public void getCartPositiveTest() throws InternalServerException, UserInputException {
//        List<CartProduct> expectedCartDetails = new ArrayList<>();
        List<List<Integer>> expectedCartRows = new ArrayList<>();
        Product product = new Product(1,"apple",200,1);
        when(mockProductService.getProductById(anyInt())).thenReturn(product);
        when(mockCartDao.getCartDetails(1)).thenReturn(expectedCartRows);
        List<CartProduct> actualCartDetails = cartService.getCart(1);
        assertEquals(0,actualCartDetails.size());
    }
    @Test
    public void  getCartNegativeTest() throws InternalServerException, UserInputException {
        Product product = new Product(1,"apple",200,1);
        when(mockProductService.getProductById(anyInt())).thenReturn(product);
        doThrow(InternalServerException.class).when(mockCartDao).getCartDetails(1);
        assertThrows(InternalServerException.class, ()->{
            cartService.getCart(1);
        });
    }
    @Test
    public void addProductIntoCartPositiveTest() throws InternalServerException, UserInputException {
        Product product = new Product(1,"apple",200,1);
        Cart cart = new Cart(1,1,1);
        UserService mockUserService = mock(UserService.class);
        CartService cartService = new CartService(mockCartDao,mockProductService,mockUserService);
        when(mockUserService.getUserById(anyInt())).thenReturn(new User());
        when(mockProductService.getProductById(anyInt())).thenReturn(product);
        when(mockCartDao.addProduct(any())).thenReturn(true);
        cartService.addProductIntoCart(cart);
        verify(mockCartDao,times(1)).addProduct(cart);
    }

    @Test
    public void addProductIntoCartNegativeTest() throws InternalServerException, UserInputException {
        Cart cart = new Cart(1,1,1);
//        Product product = new Product(1,"apple",200,1);
        UserService mockUserService = mock(UserService.class);
        CartService cartService = new CartService(mockCartDao,mockProductService,mockUserService);
        when(mockProductService.getProductById(anyInt())).thenReturn(null);
        assertThrows(UserInputException.class,()->{
            cartService.addProductIntoCart(cart);
        });
    }
    @Test
    public void addProductIntoCartInvalidInputTest(){
        Cart cart = new Cart(-1,-1,-1);
        assertThrows(UserInputException.class,()->{
            cartService.addProductIntoCart(cart);
        });
    }
    @Test
    public void deleteProductFromCartTest() throws InternalServerException, UserInputException {
        CartProductDelete productIdObject = new CartProductDelete(1,1);
        when(mockCartDao.deleteProduct(any(CartProductDelete.class))).thenReturn(1);
        cartService.deleteProductFromCart(productIdObject);
        verify(mockCartDao,times(1)).deleteProduct(productIdObject);
    }
    @Test
    public void deleteProductFromCartNegativeTest() throws InternalServerException {
        CartProductDelete productIdObject = new CartProductDelete(1,1);
        when(mockCartDao.deleteProduct(any())).thenReturn(1);
        doThrow(InternalServerException.class).when(mockCartDao).deleteProduct(productIdObject);
        assertThrows(InternalServerException.class,()->{
            cartService.deleteProductFromCart(productIdObject);
        });
    }

    @Test
    public void deleteProductFromCartInvalidInputTest(){
        CartProductDelete productIdObject = new CartProductDelete(-1,-1);
        assertThrows(UserInputException.class,()->{
            cartService.deleteProductFromCart(productIdObject);
        });
    }

    @Test
    public void updateCartProductTest() throws InternalServerException, UserInputException {
        Cart cart = new Cart(1,1,1);
        UserService mockUserService = mock(UserService.class);
        CartService cartService = new CartService(mockCartDao,mockProductService,mockUserService);
        when(mockUserService.getUserById(anyInt())).thenReturn(new User());
        when(mockCartDao.findCartProductById(cart.getProductId())).thenReturn(true);
        cartService.updateCartProduct(cart);
        verify(mockCartDao,times(1)).updateProduct(cart);
    }
    @Test
    public void updateCartProductNegativeTest() throws InternalServerException, UserInputException {
        Cart cart = new Cart(1,1,1);
        UserService mockUserService = mock(UserService.class);
        CartService cartService = new CartService(mockCartDao,mockProductService,mockUserService);
        when(mockUserService.getUserById(anyInt())).thenReturn(new User());
        doThrow(InternalServerException.class).when(mockCartDao).updateProduct(cart);
        when(mockCartDao.findCartProductById(cart.getProductId())).thenReturn(true);
        assertThrows(InternalServerException.class,()->{
            cartService.updateCartProduct(cart);
        });
    }
    @Test
    public void updateProductInvalidInputTest(){
        Cart cart = new Cart(-1,-1,-1);
        assertThrows(UserInputException.class,()->{
            cartService.updateCartProduct(cart);
        });
    }
}
