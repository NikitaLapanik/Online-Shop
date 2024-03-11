package ua.com.paw.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ua.com.paw.entity.Product;
import ua.com.paw.entity.User;
import ua.com.paw.entity.enums.Role;
import ua.com.paw.service.ProductService;

import java.security.Principal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testGetProductsPage_Success() throws Exception {
        String title = "PS";
        Principal principal = Mockito.any();
        Mockito.when(productService.listProducts(title)).thenReturn(List.of(new Product(), new Product()));
        Mockito.when(productService.getUserByPrincipal(principal)).thenReturn(new User());
        mockMvc.perform(get("/")).andExpect(status().isOk()).andExpect(view().name("products"));
    }

    @Test
    void testProductInfo_Success() throws Exception {
        Set<Role> roles = new HashSet<>();
        roles.add(Role.ROLE_ADMIN);
        long productId = 1L;
        Product product = new Product();
        User user = new User();
        user.setRoles(roles);
        product.setUser(user);

        Mockito.when(productService.getProductById(productId)).thenReturn(product);

        mockMvc.perform(get("/product/{id}", productId))
                .andExpect(status().isOk())
                .andExpect(view().name("product-info"))
                .andExpect(model().attribute("user", user));
    }

    @Test
    void testCreateProduct_Success() throws Exception {
        mockMvc.perform(post("/product/create")
                        .param("files", "file1", "file2")
                        .param("anotherParam", "value")
                        .content(objectMapper.writeValueAsString(new Product()))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is3xxRedirection());
    }
}
