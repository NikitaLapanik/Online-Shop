package ua.com.paw.repositories;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ua.com.paw.entity.User;
import ua.com.paw.entity.enums.Role;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {

    @Container
    PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:13");

    private final User user = new User();
    private final static String email = UUID.randomUUID().toString() + "@example.com";

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp(){
        user.setName("Name");
        user.setEmail(email);
        user.setPassword("pass");
        user.setActive(true);

        userRepository.save(user);
    }

    @AfterEach
    void tearDown() {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            userRepository.delete(user);
        }
    }
    @Test
    void testFindByEmail_Success() {
        User user = userRepository.findByEmail(email);
        System.out.println(user.getId());
        assertNotNull(user);
    }

    @Test
    void testFindByEmail_WrongEmail() {
        User user = userRepository.findByEmail("wrong email");
        assertNull(user);
    }

    @Test
    void testFindByRole_Success(){
        List<User> users = userRepository.findByRole(Role.ROLE_USER);
        assertNotNull(users);
        List<User> admins = userRepository.findByRole(Role.ROLE_ADMIN);
        assertNotNull(admins);
    }
}
