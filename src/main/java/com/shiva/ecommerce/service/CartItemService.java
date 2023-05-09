package com.shiva.ecommerce.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shiva.ecommerce.model.CartItem;
import com.shiva.ecommerce.repository.CartItemRepo;

@Service
public class CartItemService {

    @Autowired
    CartItemRepo cartItemRepo;

    public List<CartItem> getAllItems(int user_id) {
        return cartItemRepo.findAllByUser_Id(user_id);
    }

    public void addCartItem(CartItem item) {
        cartItemRepo.save(item);
    }

    public void removeCartItemById(int cartItemID) {
        cartItemRepo.deleteById(cartItemID);
    }

    public Optional<CartItem> getCartItemByProdIdAndUserId(int user_id, long product_id){
        return cartItemRepo.findByUser_IdAndProduct_Id(user_id, product_id);
    }
}
