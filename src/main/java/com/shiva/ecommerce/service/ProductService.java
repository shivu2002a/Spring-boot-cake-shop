package com.shiva.ecommerce.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shiva.ecommerce.model.Product;
import com.shiva.ecommerce.repository.ProductRepo;

@Service
public class ProductService  {
    
    @Autowired
    ProductRepo productRepo;

    public List<Product> getAllProducts(){
        return productRepo.findAll();
    }

    public void addProduct(Product product){
        productRepo.save(product);
    }

    public void removeProductById(long prodId) {
        productRepo.deleteById(prodId);
    }

    public Optional<Product> getProductById(long prodId) {
        return productRepo.findById(prodId);
    }

    public List<Product> getProductsByCategoryId(int id){
        return productRepo.findAllByCategory_Id(id);
    }
}

