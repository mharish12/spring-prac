package services;

import com.zs.assignment_final.dao.CategoryDao;
import com.zs.assignment_final.models.Category;
import com.zs.assignment_final.models.Product;
import com.zs.assignment_final.models.ProductId;
import com.zs.assignment_final.exceptions.InternalServerException;
import com.zs.assignment_final.exceptions.UserInputException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CategoryServiceTest {

    CategoryDao mockCategoryDao;
    CategoryService categoryService;

    @BeforeEach
    public void mockCategoryDao(){
        mockCategoryDao = mock(CategoryDao.class);
        categoryService = new CategoryService(mockCategoryDao);
        Mockito.reset();
    }

    @Test
    public void constructorTest(){
        CategoryService categoryService = new CategoryService();
    }
    @Test
    public void getAllCategoriesTest() throws InternalServerException {

        when(mockCategoryDao.selectAllCategories()).thenReturn(null);
        List<Category> actualListOfCategories = categoryService.getAllCategories();
        verify(mockCategoryDao,times(1)).selectAllCategories();
        assertNull(actualListOfCategories);
    }
    @Test
    public void getAllCategoriesNegativeTest() throws InternalServerException {
//        when(mockCategoryDao.selectAllCategories()).thenReturn(null);
//        List<Category> actualListOfCategories = categoryService.getAllCategories();
        doThrow(InternalServerException.class).when(mockCategoryDao).selectAllCategories();
        assertThrows(InternalServerException.class, categoryService::getAllCategories);
    }

    @Test
    public void createCategoryTest() throws InternalServerException, UserInputException {
        Category category = new Category(1,"grocery");
        categoryService.createCategory(category);
        verify(mockCategoryDao,times(1)).insertCategory(category);
    }
    @Test
    public void createCategoryNegativeTest() throws InternalServerException {
        Category category = new Category(1,"grocery");
        doThrow(InternalServerException.class).when(mockCategoryDao).insertCategory(category);
        assertThrows(InternalServerException.class,()->{
            categoryService.createCategory(category);
        });
    }
    @Test
    public void createCategoryInvalidInputTest(){
        Category category = new Category(-1,"grocery");
        assertThrows(UserInputException.class,()->{
            categoryService.createCategory(category);
        });
    }

    @Test
    public void updateCategoryTest() throws InternalServerException, UserInputException {
        Category category = new Category(1,"grocery");
        when(mockCategoryDao.findCategoryById(category.getId())).thenReturn(true);
        categoryService.updateCategory(category);
        verify(mockCategoryDao,times(1)).updateCategory(category);
    }

    @Test
    public void updateCategoryNegativeTest() throws InternalServerException {
        Category category = new Category(1,"grocery");
        when(mockCategoryDao.findCategoryById(category.getId())).thenReturn(true);
        doThrow(InternalServerException.class).when(mockCategoryDao).updateCategory(category);
        assertThrows(InternalServerException.class,()->{
            categoryService.updateCategory(category);
        });
    }
    @Test
    public void updateCategoryInvalidInputTest() throws InternalServerException {
        Category category = new Category(-1,"grocery");
        when(mockCategoryDao.findCategoryById(category.getId())).thenReturn(true);
        assertThrows(UserInputException.class,()->{
            categoryService.updateCategory(category);
        });
    }
    @Test
    public void deleteCategory() throws InternalServerException, UserInputException {
        int id = 1;
        ProductId productId = new ProductId(id);
        categoryService.deleteCategory(productId);
        verify(mockCategoryDao,times(1)).deleteCategory(productId);
    }
    @Test
    public void deleteCategoryNegativeTest() throws InternalServerException {
        int id = 1;
        ProductId productId = new ProductId(id);
        doThrow(InternalServerException.class).when(mockCategoryDao).deleteCategory(productId);
        assertThrows(InternalServerException.class,()->{
            categoryService.deleteCategory(productId);
        });
    }
    @Test
    public void deleteCategoryInvalidInputTest(){
        int id = -1;
        ProductId productId = new ProductId(id);
        assertThrows(UserInputException.class,()->{
            categoryService.deleteCategory(productId);
        });
    }
    @Test
    public void getProductsByCategoryNegativeTest(){
        int categoryId = -1;
        assertThrows(UserInputException.class,()->{
            categoryService.getProductsByCategory(categoryId);
        });
    }
    @Test
    public void getProductsByCategoryTest() throws InternalServerException, UserInputException {
        int categoryId = 1;
        List<Product> expectedListOfProducts = new ArrayList<>();
        Product product = new Product(1,"apple",100,1);
        expectedListOfProducts.add(product);
        ProductService productService = mock(ProductService.class);
        CategoryService categoryService = new CategoryService(mockCategoryDao,productService);
        when(productService.getProductByCategory(categoryId)).thenReturn(expectedListOfProducts);
        List<Product> actualListOfProduct = categoryService.getProductsByCategory(categoryId);
        assertEquals(expectedListOfProducts.size(),actualListOfProduct.size());
        for(int i=0;i<expectedListOfProducts.size();i++){
            assertEquals(expectedListOfProducts.get(i).getProductId(),actualListOfProduct.get(i).getProductId());
            assertEquals(expectedListOfProducts.get(i).getName(),actualListOfProduct.get(i).getName());
            assertEquals(expectedListOfProducts.get(i).getCategoryId(),actualListOfProduct.get(i).getCategoryId());
            assertEquals(expectedListOfProducts.get(i).getCost(),actualListOfProduct.get(i).getCost());
        }
    }

}
