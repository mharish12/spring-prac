package com.h12.ecommerce.services;

import com.h12.ecommerce.dao.ProductRepository;
import com.h12.ecommerce.models.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JPAProductService {
    private final ProductRepository productRepository;

    @Autowired
    public JPAProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product getProductById(int id) {
        return productRepository.getById(id);
    }
}
