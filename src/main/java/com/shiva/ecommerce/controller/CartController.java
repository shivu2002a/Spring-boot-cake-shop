package com.shiva.ecommerce.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.shiva.ecommerce.model.CartItem;
import com.shiva.ecommerce.model.CustomUserDetail;
import com.shiva.ecommerce.model.Order;
import com.shiva.ecommerce.model.User;
import com.shiva.ecommerce.repository.UserRepository;
import com.shiva.ecommerce.service.CartItemService;
import com.shiva.ecommerce.service.OrderService;
import com.shiva.ecommerce.service.ProductService;

@Controller
public class CartController {

    @Autowired
    ProductService productService;

    @Autowired
    CartItemService cartService;

    @Autowired
    UserRepository userRepo;

    @Autowired
    OrderService orderService;

    @GetMapping("/addToCart/{id}")
    public String addToCart(@PathVariable long id, @AuthenticationPrincipal OAuth2User ouser) {
        User user = getUser(ouser);
        CartItem cartItem;
        Optional<CartItem> optitem = cartService.getCartItemByProdIdAndUserId(user.getId(), id);
        if (optitem.isPresent()) {
            cartItem = optitem.get();
            cartItem.setCount(cartItem.getCount() + 1);
        } else {
            cartItem = new CartItem();
            cartItem.setCount(1);
            cartItem.setProduct(productService.getProductById(id).get());
            cartItem.setUser(user);
        }
        cartService.addCartItem(cartItem);
        return "redirect:/cart";
    }

    @GetMapping("/cart")
    public String cartGet(Model model, @AuthenticationPrincipal OAuth2User ouser) {
        User user = getUser(ouser);
        List<CartItem> cartitems = cartService.getAllItems(user.getId());
        model.addAttribute("cartCount", cartitems.size());
        float totalPrice = 0;
        for (CartItem c : cartitems)
            totalPrice += c.getProduct().getPrice() * c.getCount();
        model.addAttribute("total", totalPrice);
        model.addAttribute("cart", cartitems);
        model.addAttribute("user_id", user.getId());
        return "cart";
    }

    @GetMapping("/cart/removeItem/{id}")
    public String removeCartItem(@PathVariable int id) {
        cartService.removeCartItemById(id);
        return "redirect:/cart";
    }


    private User getUser(@AuthenticationPrincipal OAuth2User auth2user) {
        String email;
        if (auth2user == null) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            CustomUserDetail loggDetail = (CustomUserDetail) auth.getPrincipal();
            email = loggDetail.getEmail();
        } else {
            email = auth2user.getAttribute("email").toString();
        }
        User user = userRepo.findUserByEmail(email).get();
        return user;
    }

    @GetMapping("/orders")
    public String getOrders(@AuthenticationPrincipal OAuth2User cuser, Model model){
        int user_id = getUser(cuser).getId();
        List<Order> orders = orderService.getAllOrdersByUser(user_id);
        model.addAttribute("orders", orders);
        return "orders";
    }

}
