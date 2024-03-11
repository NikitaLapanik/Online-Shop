package ua.com.paw.repositories;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ua.com.paw.entity.Product;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ProductRepositoryTest {

    private final Product product = new Product();

    @Container
    PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:13");

    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        product.setTitle("Title");
        productRepository.save(product);
    }

    @AfterEach
    void tearDown() {
        productRepository.delete(product);
    }

    @Test
    void testFindByTitleContainingIgnoreCase_Success() {
        List<Product> products = productRepository.findByTitleContainingIgnoreCase("Title");
        assertTrue(products.size() > 0);
    }

    @Test
    void testFindByTitleContainingIgnoreCase_WrongTitle() {
        List<Product> products = productRepository.findByTitleContainingIgnoreCase("super mega wrong title");
        assertEquals(0, products.size());
    }
}
