package com.shiva.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shiva.ecommerce.model.Role;

public interface RoleRepositoy extends JpaRepository<Role, Integer> {
    
}
