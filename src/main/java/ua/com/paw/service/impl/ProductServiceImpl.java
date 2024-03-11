package ua.com.paw.service.impl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ua.com.paw.entity.Image;
import ua.com.paw.entity.Product;
import ua.com.paw.entity.User;
import ua.com.paw.exceptions.ExceptionMessages;
import ua.com.paw.repositories.ImageRepository;
import ua.com.paw.repositories.ProductRepository;
import ua.com.paw.repositories.UserRepository;
import ua.com.paw.service.ProductService;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<Product> listProducts(String title) {
        if (title != null && !title.isEmpty()) {
            return productRepository.findByTitleContainingIgnoreCase(title);
        } else {
            return productRepository.findAll();
        }
    }

    @Override
    @Transactional
    public void saveProduct(Principal principal, Product product, MultipartFile[] files) throws IOException {
        product.setUser(getUserByPrincipal(principal));
        Image image1;
        Image image2;
        Image image3;

        for (MultipartFile file : files) {
            if (file.getSize() != 0) {
                Image image = toImageEntity(file);
                product.addImageToProduct(image);
            }
        }

        log.info("Saving new Product. Title: {}; Author email: {}; ", product.getTitle(), product.getUser().getEmail());
        productRepository.save(product);
    }

    @Override
    public User getUserByPrincipal(Principal principal){
        if(principal == null){
            return new User();
        }
        return userRepository.findByEmail(principal.getName());
    }

    private Image toImageEntity(MultipartFile file) throws IOException {
        Image image = new Image();
        image.setName(file.getName());
        image.setOriginalFileName(file.getOriginalFilename());
        image.setContentType(file.getContentType());
        image.setSize(file.getSize());
        image.setBytes(file.getBytes());
        return image;
    }

    @Override
    public void delete(long id) {
        productRepository.deleteById(id);
        log.info("Product with id {} was deleted", id);
    }

    @Override
    public Product getProductById(long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ExceptionMessages.PRODUCT_WAS_NOT_FOUND));
    }
}
