package ua.com.paw.controllers;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ua.com.paw.entity.User;
import ua.com.paw.service.UserService;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    void testGetLoginPage_Success() throws Exception {
        mockMvc.perform(get("/login")).andExpect(status().isOk()).andExpect(view().name("login"));
    }

    @Test
    void testGetRegistrationPage_Success() throws Exception {
        mockMvc.perform(get("/registration")).andExpect(status().isOk()).andExpect(view().name("registration"));
    }

    @Test
    void testCreateUser_Success() throws Exception {
        when(userService.createUser(any(User.class))).thenReturn(true);
        mockMvc.perform(post("/registration")
                                .param("email", "aa@aa")
                                .param("password", "password")
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    @Test
    void testCreateUser_Failure() throws Exception {
        when(userService.createUser(any(User.class))).thenReturn(false);
        mockMvc.perform(post("/registration")
                                .param("email", "existing@example.com")
                                .param("password", "password")
                )
                .andExpect(status().isOk())
                .andExpect(view().name("registration"))
                .andExpect(model().attributeExists("errorMessage"));
    }

    @Test
    void testGetUserInfo_Success() throws Exception {
        long id = 1L;
        User user = new User();
        user.setId(id);
        Mockito.when(userService.getUserById(id)).thenReturn(user);
        mockMvc.perform(get("/user/{id}", id)).andExpect(status().isOk());
    }
}
