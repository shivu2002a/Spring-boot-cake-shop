package com.shiva.ecommerce.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shiva.ecommerce.model.Order;
import com.shiva.ecommerce.repository.OrderRepository;

@Service
public class OrderService {
    
    @Autowired
    OrderRepository orderRepo;

    public List<Order> getAllOrdersByUser(int user_id){
        return orderRepo.findAllByUser_Id(user_id);
    }

    public void addOrder(Order order){
        orderRepo.save(order);
    }
}
