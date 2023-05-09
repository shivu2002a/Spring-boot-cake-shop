package com.shiva.ecommerce.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.shiva.ecommerce.dto.ProductDTO;
import com.shiva.ecommerce.model.Category;
import com.shiva.ecommerce.model.Product;
import com.shiva.ecommerce.service.CategoryService;
import com.shiva.ecommerce.service.ProductService;

@Controller
public class AdminController {

    public static String uploadDir = System.getProperty("user.dir") + "/src/main/resources/static/productImages";


    @Autowired
    CategoryService categoryService;

    @Autowired
    ProductService productService;
    
    @GetMapping("/admin")
    public String getAdmin(){
        return "adminHome";
    }

    //Categories routes
    @GetMapping("admin/categories")
    public String getCategories(Model model){
        model.addAttribute("categories", categoryService.getCategories());
        return "categories";
    }

    @GetMapping("admin/categories/add")
    public String getCategoriesAdd(Model model){ 
        model.addAttribute("category", new Category());
        return "categoriesAdd";
    }

    @PostMapping("admin/categories/add")
    public String postCategoriesAdd(@ModelAttribute("category") Category category){ 
        categoryService.addCategory(category);
        return "redirect:/admin/categories";
    }

    @GetMapping("admin/categories/delete/{id}")
    public String deleteCat(@PathVariable("id") int catId){
        categoryService.removeCategoryById(catId);
        return "redirect:/admin/categories";
    }

    @GetMapping("admin/categories/update/{id}")
    public String updateCat(@PathVariable("id") int catId, Model model){
        Optional<Category> cat = categoryService.getCategoryById(catId);
        if(cat.isPresent()){
            model.addAttribute("category", cat.get());
            return "categoriesAdd";
        }
        return "404";
    }

    //Product routes
    @GetMapping("/admin/products")
    public String getProducts(Model model){
        model.addAttribute("products", productService.getAllProducts());
        return "products";
    }

    @GetMapping("/admin/products/add")
    public String productAddGet(Model model){   
        model.addAttribute("productDTO", new ProductDTO());
        model.addAttribute("categories", categoryService.getCategories());
        return "productsAdd";
    }

    @PostMapping("/admin/products/add")
    public String productAddPost(@ModelAttribute("ProductDTO") ProductDTO productDTO, 
    @RequestParam("productImage")MultipartFile file, @RequestParam("imgName")String imgName) throws IOException {
        Product product = new Product();
        product.setId(productDTO.getId());
        product.setName(productDTO.getName());
        product.setCategory(categoryService.getCategoryById(productDTO.getCategoryId()).get());
        product.setDescription(productDTO.getDescription());
        product.setPrice(productDTO.getPrice());

        //Storing image
        String imgUUID;
        if(!file.isEmpty()){
            imgUUID = file.getOriginalFilename();
            Path filenameandPath = Paths.get(uploadDir, imgUUID);
            Files.write(filenameandPath, file.getBytes());
        } else {
            imgUUID = imgName;
        }
        product.setImageName(imgUUID);
        productService.addProduct(product);



        return "redirect:/admin/products";
    }

    @GetMapping("/admin/product/delete/{id}")
    public String deleteProduct(@PathVariable long id){
        productService.removeProductById(id);
        return "redirect:/admin/products";
    }

    @GetMapping("/admin/product/update/{id}")
    public String updateProductGet(@PathVariable long id, Model model){
        Product product = productService.getProductById(id).get();
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(product.getId());
        productDTO.setName(product.getName());
        productDTO.setCategoryId(product.getCategory().getId());
        productDTO.setPrice(product.getPrice());
        productDTO.setDescription(product.getDescription());
        productDTO.setImageName(product.getImageName());
        model.addAttribute("categories", categoryService.getCategories());
        model.addAttribute("productDTO", productDTO);
        
        return "productsAdd";
    }

    @PostMapping("/admin/product/update/{id}")
    public String updateProductPost(@PathVariable long id){
        productService.removeProductById(id);
        return "redirect:/admin/products";
    }
    
}




