package ua.com.paw.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ua.com.paw.entity.User;
import ua.com.paw.service.UserService;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AdminControllerTest {

    private final User user = new User();
    private final long id = 1;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void testGetAdminPage_Success() throws Exception {
        mockMvc.perform(get("/admin")).andExpect(status().isOk()).andExpect(view().name("admin"));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void testGetAdminPage_PrincipalIsNotAdmin() throws Exception {
        mockMvc.perform(get("/admin")).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void testUserBan_Success() throws Exception {
        mockMvc.perform(post("/admin/user/ban/{id}", id)).andExpect(redirectedUrl("/admin"));
        verify(userService).banUser(id);
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void testUserBan_WrongId() throws Exception {
        long wrongId = -1L;
        mockMvc.perform(post("/admin/user/ban/{id}", wrongId))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin"));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void testUserBan_PrincipalIsNotAdmin() throws Exception {
        mockMvc.perform(post("/admin/user/ban/{id}", id))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void deleteUser_Success() throws Exception {
        mockMvc.perform(post("/admin/user/delete/{id}", id)).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin"));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void testDeleteUser_WrongId() throws Exception {
        long wrongID = 1L;
        mockMvc.perform(post("/admin/user/delete/{id}", wrongID)).andExpect(status().is3xxRedirection()).andExpect(view().name("redirect:/admin"));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void testDeleteUser_PrincipalIsNotAdmin() throws Exception {
        mockMvc.perform(post("/admin/user/delete/{id}", id)).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void getUserByEmail_Success() throws Exception {
        String testEmail = "test@example.com";

        User mockUser = new User();
        mockUser.setEmail(testEmail);
        when(userService.findByEmail(testEmail)).thenReturn(mockUser);

        mockMvc.perform(get("/admin/user/email").param("email", testEmail))
                .andExpect(status().isOk())
                .andExpect(view().name("admin"))
                .andExpect(model().attributeExists("userByEmail"))
                .andExpect(model().attribute("userByEmail", mockUser));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void getUserByEmail_NoResults() throws Exception {
        String nonExistingEmail = "nonexisting@example.com";

        when(userService.findByEmail(nonExistingEmail)).thenReturn(null);

        mockMvc.perform(get("/admin/user/email").param("email", nonExistingEmail))
                .andExpect(status().isOk())
                .andExpect(view().name("admin"))
                .andExpect(model().attributeDoesNotExist("userByEmail"))
                .andExpect(model().attributeExists("noResultsMessage"))
                .andExpect(model().attribute("noResultsMessage", "No user found for the email: " + nonExistingEmail));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void getUserByEmail() throws Exception {
        mockMvc.perform(get("/admin/user/email").param("email", "email")).andExpect(status().isForbidden());
    }
}
