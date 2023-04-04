package com.h12.ecommerce.services;

import com.h12.ecommerce.dao.CategoryDao;
import com.h12.ecommerce.exceptions.InternalServerException;
import com.h12.ecommerce.exceptions.UserInputException;
import com.h12.ecommerce.models.Category;
import com.h12.ecommerce.models.Product;
import com.h12.ecommerce.models.ProductId;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Category service.
 */
@Service
public class CategoryService {

    private final CategoryDao categoryDao;
    private final ProductService productService;
    /**
     * Constructor to initialize values.
     */
    public CategoryService(){
        categoryDao = new CategoryDao();
        productService = new ProductService();
    }

    /**
     * Constructor to initialize values.
     * @param categoryDao category dao object.
     */
    public CategoryService(CategoryDao categoryDao){
        this.categoryDao = categoryDao;
        this.productService = new ProductService();
    }

    public CategoryService(CategoryDao categoryDao,ProductService productService){
        this.categoryDao = categoryDao;
        this.productService = productService;
    }
    /**
     *
     * @return list of category class objects.
     * @throws InternalServerException when connection issues or sql statement execution fails.
     */
    public List<Category> getAllCategories() throws InternalServerException {
        return categoryDao.selectAllCategories();
    }

    /**
     *
     * @param category  category class object.
     * @throws InternalServerException when connection issues or sql statement execution fails.
     */
    public void createCategory(Category category) throws InternalServerException, UserInputException {
        if(category.getCategoryName().equals("")){
            throw new UserInputException("category name cannot be empty");
        }
        else if(category.getId()<=0){
            throw new UserInputException("category id cannot be negative");
        }
        categoryDao.insertCategory(category);
    }

    /**
     *
     * @param category category class object.
     * @throws InternalServerException when connection issues or sql statement execution fails.
     */
    public void updateCategory(Category category) throws InternalServerException, UserInputException {
        if(category.getCategoryName().equals("")){
            throw new UserInputException("category name cannot be empty");
        }
        else if(category.getId()<=0){
            throw new UserInputException("category id cannot be negative");
        }
        if(!this.findCategoryById(category.getId())){
            throw new UserInputException("Category not found.");
        }
        categoryDao.updateCategory(category);
    }

    /**
     * To find category.
     * @param id category product id.
     * @return true if cart product found else false.
     * @throws InternalServerException when connection issues or sql statement execution fails.
     */
    private boolean findCategoryById(int id) throws InternalServerException {
        return categoryDao.findCategoryById(id);
    }

    /**
     *
     * @param productId category id entity class.
     * @throws InternalServerException when connection issues or sql statement execution fails.
     */
    public void deleteCategory(ProductId productId) throws InternalServerException, UserInputException {
        if(productId.getId()<=0){
            throw new UserInputException("category id cannot be negative");
        }
        categoryDao.deleteCategory(productId);
    }

    /**
     *
     * @return list of products.
     * @throws InternalServerException when connection issues or sql statement execution fails.
     * @throws UserInputException when user gives invalid input.
     */
    public List<Product> getProductsByCategory(int categoryId) throws InternalServerException, UserInputException {
        if(categoryId<=0){
            throw new UserInputException("category id cannot be negative");
        }
        return productService.getProductByCategory(categoryId);
    }
}
