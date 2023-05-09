package com.shiva.ecommerce.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.shiva.ecommerce.model.CartItem;
import com.shiva.ecommerce.model.PaytmDetails;
import com.shiva.ecommerce.model.User;

@Controller
public class PaymentController {

    @Autowired
    PaytmDetails paytmDetails;
    
    @GetMapping("/checkout")
    public ModelAndView getCheckout(@RequestParam(name = "payable_amount") String payAmount, @AuthenticationPrincipal User user) {
        int user_id = user.getId();
        float totalPrice = 0;
        for (CartItem c : cartService.getAllItems(user_id))
            totalPrice += c.getProduct().getPrice();
        ModelAndView mav = new ModelAndView("redirect:" + paytmDetails.getDetails());
        
    }
}
