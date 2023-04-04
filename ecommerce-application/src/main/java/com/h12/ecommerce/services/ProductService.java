package com.h12.ecommerce.services;

import com.h12.ecommerce.dao.ProductDao;
import com.h12.ecommerce.exceptions.InternalServerException;
import com.h12.ecommerce.exceptions.UserInputException;
import com.h12.ecommerce.models.Product;
import com.h12.ecommerce.models.ProductId;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Product service.
 */
@Service
public class ProductService {
    private final ProductDao productDao;

    /**
     * Product service default constructor.
     */
    public ProductService() {
        this.productDao = new ProductDao();
    }

    /**
     * To initialize productDao object.
     * @param productDao productDao object.
     */
    public ProductService(ProductDao productDao) {
        this.productDao = productDao;
    }

    /**
     *
     * @param product product object.
     * @throws InternalServerException when connection issues or sql statement execution fails.
     */
    public void createProduct(Product product) throws InternalServerException, UserInputException {
        if(product.getProductId()<=0 || product.getCategoryId()<=0 || product.getCost()<=0){
            throw new UserInputException("Product id or cost or category id cannot be negative.");
        }
        productDao.insertProduct(product);
    }

    /**
     *
     * @param product product object.
     * @throws InternalServerException when connection issues or sql statement execution fails.
     */
    public void updateProduct(Product product) throws InternalServerException, UserInputException {
        if(product.getProductId()<=0 || product.getCost()<=0 || product.getCategoryId()<=0){
            throw new UserInputException("Product id or cost or category id cannot be negative.");
        }
        else if(product.getName().equals("")){
            throw new UserInputException("Product name cannot be empty.");
        }
        if(!this.findProductById(product.getProductId())){
            throw new UserInputException("PProduct not found in database.");
        }
        productDao.updateRow(product);
    }
    /**
     * To find product.
     * @param productId category product id.
     * @return true if cart product found else false.
     * @throws InternalServerException when connection issues or sql statement execution fails.
     */
    private boolean findProductById(int productId) throws UserInputException, InternalServerException {
        return productDao.getProductDetailsById(productId) != null;
    }

    /**
     *
     * @param productId product id.
     * @return product object.
     * @throws InternalServerException when connection issues or sql statement execution fails.
     */
    public Product getProductById(int productId) throws InternalServerException, UserInputException {
        if(productId<=0){
            throw new UserInputException("Product id cannot be negative.");
        }
        return productDao.getProductDetailsById(productId);
    }

    /**
     *
     * @return list of product objects.
     * @throws InternalServerException when connection issues or sql statement execution fails.
     */
    public List<Product> getAllProducts() throws InternalServerException {
        return productDao.selectAllProducts();
    }

    /**
     *
     * @param  productId id.
     * @throws InternalServerException when connection issues or sql statement execution fails.
     */
    public void deleteProductById(ProductId productId) throws InternalServerException, UserInputException {
        if(productId.getId()<=0){
            throw new UserInputException("Product id cannot be negative.");
        }
        productDao.deleteProduct(productId);
    }

    /**
     *
     * @param id category id.
     * @return list of products
     * @throws InternalServerException when connection issues or sql statement execution fails.
     */
    public List<Product> getProductByCategory(int id) throws InternalServerException {
        return productDao.getProductsByCategory(id);
    }
}
