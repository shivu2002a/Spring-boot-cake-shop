package com.shiva.ecommerce.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shiva.ecommerce.model.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    
    List<Order> findAllByUser_Id(int id);
    
}
