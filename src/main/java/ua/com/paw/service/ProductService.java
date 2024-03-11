package ua.com.paw.service;

import org.springframework.web.multipart.MultipartFile;
import ua.com.paw.entity.Product;
import ua.com.paw.entity.User;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

public interface ProductService {

     public void saveProduct(Principal principal,Product product,  MultipartFile[] files) throws IOException;
     public void delete(long id);

     Product getProductById(long id);

     public List<Product> listProducts(String title);

     public User getUserByPrincipal(Principal principal);
}
