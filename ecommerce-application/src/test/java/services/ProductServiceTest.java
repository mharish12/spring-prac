package services;

import com.h12.ecommerce.dao.ProductDao;
import com.h12.ecommerce.models.Product;
import com.h12.ecommerce.models.ProductId;
import com.h12.ecommerce.exceptions.InternalServerException;
import com.h12.ecommerce.exceptions.UserInputException;
import com.h12.ecommerce.services.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProductServiceTest {
    private ProductDao mockProductDao;
    private ProductService productService;
    @BeforeEach
    public void setMockProductDao(){
        mockProductDao = mock(ProductDao.class);
        productService = new ProductService(mockProductDao);
        Mockito.reset();
    }
    @Test
    public void constructorTest(){
        ProductService productService = new ProductService();
    }

    @Test
    public void createProductTest() throws InternalServerException, UserInputException {
        Product product = new Product(1,"apple",100,1);
//        doNothing().when(mockProductDao).insertProduct(product);
        productService.createProduct(product);
        verify(mockProductDao,times(1)).insertProduct(product);
    }
    @Test
    public void createProductNegativeTest() throws InternalServerException {
        Product product = new Product(1,"apple",100,1);
        doThrow(InternalServerException.class).when(mockProductDao).insertProduct(product);
        assertThrows(InternalServerException.class,()->{
            productService.createProduct(product);
        });
    }
    @Test
    public void createProductInvalidInputTest() throws InternalServerException {
        Product product = new Product(-1,"",-100,-1);
        assertThrows(UserInputException.class,()->{
            productService.createProduct(product);
        });
    }
    @Test
    public void updateProductTest() throws InternalServerException, UserInputException {
        Product product = new Product(1,"apple",100,1);
        when(mockProductDao.getProductDetailsById(product.getProductId())).thenReturn(product);
        productService.updateProduct(product);
        verify(mockProductDao,times(1)).updateRow(product);
    }
    @Test
    public void updateProductNegativeTest() throws InternalServerException, UserInputException {
        Product product = new Product(1,"apple",100,1);
        when(mockProductDao.getProductDetailsById(product.getProductId())).thenReturn(product);
        doThrow(InternalServerException.class).when(mockProductDao).updateRow(product);
        assertThrows(InternalServerException.class,()->{
            productService.updateProduct(product);
        });
    }
    @Test
    public void updateProductInvalidInputTest() throws InternalServerException, UserInputException {
        Product product = new Product(1,"apple",100,-1);
        when(mockProductDao.getProductDetailsById(product.getProductId())).thenReturn(product);
        doThrow(InternalServerException.class).when(mockProductDao).updateRow(product);
        assertThrows(UserInputException.class,()->{
            productService.updateProduct(product);
        });
    }
    @Test
    public void getProductByIdTest() throws InternalServerException, UserInputException {
        int id = 1;
        Product expectedProduct = new Product(1,"apple",100,1);
        when(mockProductDao.getProductDetailsById(id)).thenReturn(expectedProduct);
        Product actualProduct = productService.getProductById(id);
        verify(mockProductDao,times(1)).getProductDetailsById(id);
        assertEquals(expectedProduct.getProductId(),actualProduct.getProductId());
    }
    @Test
    public void getProductByIdNegativeTest() throws InternalServerException, UserInputException {
        int id = 1;
        doThrow(InternalServerException.class).when(mockProductDao).getProductDetailsById(id);
        assertThrows(InternalServerException.class,()->{
           productService.getProductById(id);
        });
    }
    @Test
    public void getProductByIdInvalidInputTest() throws InternalServerException, UserInputException {
        int id = -1;
        assertThrows(UserInputException.class,()->{
            productService.getProductById(id);
        });
    }

    @Test
    public void getAllProductsTest() throws InternalServerException {
        when(mockProductDao.selectAllProducts()).thenReturn(null);
        List<Product> actualListOfProducts = productService.getAllProducts();
        verify(mockProductDao,times(1)).selectAllProducts();
        assertNull(actualListOfProducts);
    }
    @Test
    public void getAllProductsNegativeTest() throws InternalServerException {
        doThrow(InternalServerException.class).when(mockProductDao).selectAllProducts();
        assertThrows(InternalServerException.class, productService::getAllProducts);
    }
    @Test
    public void deleteProductByIdTest() throws InternalServerException, UserInputException {
        int id =1;
        ProductId productId = new ProductId(id);
        productService.deleteProductById(productId);
        verify(mockProductDao,times(1)).deleteProduct(productId);
    }
    @Test
    public void deleteProductByIdNegativeTest() throws InternalServerException {
        int id =1;
        ProductId productId = new ProductId(id);
        doThrow(InternalServerException.class).when(mockProductDao).deleteProduct(productId);
        assertThrows(InternalServerException.class, ()->{
            productService.deleteProductById(productId);
        });
    }
    @Test
    public void deleteProductByIdInvalidInputTest() throws InternalServerException {
        int id = -1;
        ProductId productId = new ProductId(id);
        assertThrows(UserInputException.class, ()->{
            productService.deleteProductById(productId);
        });
    }
    @Test
    public void getProductByCategoryTest() throws InternalServerException {
        int categoryId = 1;
        productService.getProductByCategory(categoryId);
        verify(mockProductDao,times(1)).getProductsByCategory(categoryId);
    }
    @Test
    public void getProductByCategoryNegativeTest() throws InternalServerException {
        int categoryId = 1;
        doThrow(InternalServerException.class).when(mockProductDao).getProductsByCategory(categoryId);
        assertThrows(InternalServerException.class,()->{
            productService.getProductByCategory(categoryId);
        });
    }
}
