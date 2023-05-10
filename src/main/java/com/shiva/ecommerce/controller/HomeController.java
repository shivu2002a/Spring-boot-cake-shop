package com.shiva.ecommerce.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.shiva.ecommerce.global.GlobalData;
import com.shiva.ecommerce.service.CategoryService;
import com.shiva.ecommerce.service.ProductService;

@Controller
public class HomeController {
    
    @Autowired
    CategoryService categoryService;

    @Autowired
    ProductService productService;

    @GetMapping({"/", "/home"})
    public String getHome(Model model){
        return "index";
    }

    @GetMapping("/shop")
    public String getShop(Model model){
        model.addAttribute("categories", categoryService.getCategories());
        model.addAttribute("products", productService.getAllProducts());
        model.addAttribute("cartCount", GlobalData.cart.size());
        return "shop";
    }

    @GetMapping("/shop/category/{catId}")
    public String getProductsByCategoryId(@PathVariable int catId, Model model){
        model.addAttribute("categories", categoryService.getCategories());
        model.addAttribute("products", productService.getProductsByCategoryId(catId));
        model.addAttribute("cartCount", GlobalData.cart.size());
        return "shop";
    }

    @GetMapping("/shop/viewproduct/{id}")
    public String getProductById(Model model, @PathVariable long id){
        model.addAttribute("product", productService.getProductById(id).get());
        return "viewProduct";
    }

    

}
