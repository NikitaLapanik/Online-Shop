package ua.com.paw.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ua.com.paw.entity.User;
import ua.com.paw.entity.enums.Role;
import ua.com.paw.service.UserService;

import java.util.Collections;
import java.util.List;

@Component
public class AdminInitializer implements CommandLineRunner {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        List<User> users = userService.findAllByRole(Role.ROLE_ADMIN);
        if (users.isEmpty()) {
            User admin = new User();
            admin.setName("admin");
            admin.setEmail("admin@admin");
            admin.setPassword("admin");
            admin.setRoles(Collections.singleton(Role.ROLE_ADMIN));

            userService.createUser(admin);
        }
    }
}
