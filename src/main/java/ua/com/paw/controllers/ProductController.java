package ua.com.paw.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ua.com.paw.entity.Product;
import ua.com.paw.service.ProductService;

import java.io.IOException;
import java.security.Principal;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public String products(@RequestParam(name = "title", required = false) String title, Model model, Principal principal) {
        model.addAttribute("products", productService.listProducts(title));
        model.addAttribute("user", productService.getUserByPrincipal(principal));
        model.addAttribute("isLogged", principal != null);
        return "products";
    }

    @GetMapping("/product/{id}")
    public String productInfo(@PathVariable long id, Model model, Principal principal) {
        Product product = productService.getProductById(id);
        model.addAttribute("product", product);
        model.addAttribute("user", product.getUser());
        model.addAttribute("isLogged", principal != null);
        List<String> strA = product.getImages().stream().map(s -> {
            return Base64.getEncoder().encodeToString(s.getBytes());
        }).collect(Collectors.toList());

        model.addAttribute("images", strA);
        return "product-info";
    }

    @PostMapping("/product/create")
    public String createProduct(@RequestParam(name = "files", required = false) MultipartFile[] files,
                                Product product, Principal principal) throws IOException {
        productService.saveProduct(principal, product, files);
        return "redirect:/";
    }
    @RequestMapping(value = "product/delete/{id}", method = RequestMethod.GET)
    public String deleteProduct(@PathVariable long id) {
        productService.delete(id);
        return "redirect:/";
    }
}
