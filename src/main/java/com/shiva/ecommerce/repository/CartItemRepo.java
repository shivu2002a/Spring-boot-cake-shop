package com.shiva.ecommerce.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shiva.ecommerce.model.CartItem;

@Repository
public interface CartItemRepo extends JpaRepository<CartItem, Integer>{
    
    List<CartItem> findAllByUser_Id(int id);

    Optional<CartItem> findByUser_IdAndProduct_Id(int user_id, long product_id);
}   
