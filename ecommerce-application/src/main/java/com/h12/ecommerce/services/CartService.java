package com.h12.ecommerce.services;

import com.h12.ecommerce.dao.CartDao;
import com.h12.ecommerce.exceptions.InternalServerException;
import com.h12.ecommerce.exceptions.UserInputException;
import com.h12.ecommerce.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Cart Service.
 */
@Service
public class CartService {
    @Autowired
    private final CartDao cartDao;
    @Autowired
    private final ProductService productService;
    @Autowired
    private final UserService userService;
    @Autowired
    private RestTemplate restTemplate;
    /**
     * Default constructor.
     */
    public CartService() {
        cartDao = new CartDao();
        productService = new ProductService();
        userService = new UserService();
    }

    public CartService(CartDao cartDao,ProductService productService){
        this.cartDao = cartDao;
        this.productService = productService;
        userService = new UserService();
    }

    public CartService(CartDao cartDao,ProductService productService,UserService userService){
        this.cartDao = cartDao;
        this.userService = userService;
        this.productService = productService;
    }

    @Async("asyncExecutor")
    public CompletableFuture<User> findUser(String user) throws InterruptedException {
//        ExecutorService threadPool = Executors.newCachedThreadPool();
        Executor executor = Executors.newCachedThreadPool();
//        ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) threadPool;

//        CompletableFuture<Long> completableFuture = CompletableFuture.supplyAsync(() -> method.invoke(object, parameters));
//        while (!completableFuture.isDone()) {
//            System.out.println("CompletableFuture is not finished yet...");
//        }
//        long result = completableFuture.get();
//

//        SimpleAsyncTaskExecutor
        String url = String.format("https://api.github.com/users/%s", user);
        User results = restTemplate.getForObject(url, User.class);
        // Artificial delay of 1s for demonstration purposes
        Thread.sleep(1000L);
        return CompletableFuture.completedFuture(results);
    }

    private boolean userCheck(int userId) throws UserInputException, InternalServerException {
        return userService.getUserById(userId) != null;
    }
    /**
     * To get all cart products.
     * @return list of cart products.
     * @throws InternalServerException when connection issues or sql statement execution fails.
     */
    public List<CartProduct> getCart(int userId) throws InternalServerException, UserInputException {
        List<List<Integer>>  cartDetails = cartDao.getCartDetails(userId);
        List<CartProduct> cartProducts = new ArrayList<>();
        for(List<Integer> cartProduct:cartDetails){
            Product product = productService.getProductById(cartProduct.get(0));
            cartProducts.add(new CartProduct(product.getProductId(),product.getName(),product.getCost(),product.getCategoryId(),cartProduct.get(1)));
        }
        return cartProducts;
    }

    /**
     *
     * @param cart cart product.
     * @throws InternalServerException when connection issues or sql statement execution fails.
     * @throws UserInputException when product with id not found in database.
     */
    public void addProductIntoCart(Cart cart) throws InternalServerException, UserInputException {
        if (cart.getProductId()<=0){
            throw new UserInputException("Product id cannot be negative.");
        }
        else if(cart.getQuantity()<=0){
            throw new UserInputException("Quantity cannot be zero or negative.");
        } else if (cart.getUserId() <= 0) {
            throw new UserInputException("User id cannot be negative.");
        }
        if(!this.userCheck(cart.getUserId())){
            throw new UserInputException("User not found.");
        }
        Product product = productService.getProductById(cart.getProductId());
        if(product != null){
            cartDao.addProduct(cart);
        }
        else {
            throw new UserInputException("Product not found.");
        }
    }

    /**
     * To delete product from cart.
     * @param productId product id.
     * @throws InternalServerException when connection issues or sql statement execution fails.
     */
    public void deleteProductFromCart(CartProductDelete productId) throws InternalServerException, UserInputException {
        if(productId.getId()<=0){
            throw new UserInputException("Product id cannot be negative.");
        }
        cartDao.deleteProduct(productId);
    }

    /**
     * To update product.
     * @param cart cart product.
     * @throws InternalServerException when connection issues or sql statement execution fails.
     */
    public void updateCartProduct(Cart cart) throws InternalServerException, UserInputException {
        if(cart.getProductId()<=0 || cart.getQuantity()<=0){
            throw new UserInputException("Product id or quantity cannot be negative.");
        }
        if(!this.findCartProductById(cart.getProductId())){
            throw new UserInputException("Product not found in cart.");
        }
        if (cart.getUserId() <= 0) {
            throw new UserInputException("User id cannot be negative.");
        }
        if(!this.userCheck(cart.getUserId())){
            throw new UserInputException("User not found.");
        }
        cartDao.updateProduct(cart);
    }

    /**
     * To find cart product.
     * @param productId cart product id.
     * @return true if cart product found else false.
     * @throws InternalServerException when connection issues or sql statement execution fails.
     */
    private boolean findCartProductById(int productId) throws InternalServerException{
        return cartDao.findCartProductById(productId);
    }
}
