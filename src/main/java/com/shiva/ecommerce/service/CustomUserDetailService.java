package com.shiva.ecommerce.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.shiva.ecommerce.model.CustomUserDetail;
import com.shiva.ecommerce.model.User;
import com.shiva.ecommerce.repository.UserRepository;

@Service
public class CustomUserDetailService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findUserByEmail(email);
        user.orElseThrow(() -> new UsernameNotFoundException("Email doesn't exist"));
        return user.map(CustomUserDetail::new).get();
    }

    public UserDetails findUserByID(int id) {   
        Optional<User> user = userRepository.findById(id);
        if(user.isPresent()) return user.map(CustomUserDetail::new).get();
        return null;
    }
    
}
