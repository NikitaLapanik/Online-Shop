package ua.com.paw.service;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.com.paw.entity.Image;
import ua.com.paw.entity.Product;
import ua.com.paw.entity.User;
import ua.com.paw.exceptions.ExceptionMessages;
import ua.com.paw.repositories.ProductRepository;
import ua.com.paw.service.impl.ProductServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @InjectMocks
    private ProductServiceImpl productService;

    @Mock
    private ProductRepository productRepository;

    @Test
    void testListProduct_Success() {
        when(productRepository.findAll()).thenReturn(List.of(new Product(), new Product()));
        assertEquals(2, productService.listProducts(null).size());

        String title = "Title";
        when(productRepository.findByTitleContainingIgnoreCase(title)).thenReturn(List.of(new Product(), new Product(), new Product()));
        assertEquals(3, productService.listProducts(title).size());
    }

    @Test
    void testListProduct_WrongTitle() {
        String wrongTitle = "wrong title";
        when(productRepository.findByTitleContainingIgnoreCase(wrongTitle)).thenReturn(null);
        assertNull(productService.listProducts(wrongTitle));
    }

    @Test
    void testDelete_Success() {
        long id = 1L;
        productService.delete(id);
        verify(productRepository, times(1)).deleteById(id);
    }

    @Test
    void testGetProductById_Success() {
        Mockito.when(productRepository.findById(1L)).thenReturn(Optional.of(createTestProduct()));
        Product product = productService.getProductById(1L);
        assertNotNull(product);
        assertEquals(product.getPrice(), createTestProduct().getPrice());
    }

    @Test
    void testGetProductById_WrongId() {
        long wrongID = 123423L;
        Mockito.when(productRepository.findById(wrongID)).thenReturn(Optional.empty());
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->{
            productService.getProductById(wrongID);
        });
        assertEquals(ExceptionMessages.PRODUCT_WAS_NOT_FOUND, exception.getMessage());
    }

    private Product createTestProduct() {
        Product product = new Product();
        product.setTitle("title");
        product.setUser(new User());
        product.setId(1L);
        product.setCity("city");
        product.setPrice(22);
        product.setDescription("description");
        product.setImages(List.of(new Image()));
        return product;
    }
}
