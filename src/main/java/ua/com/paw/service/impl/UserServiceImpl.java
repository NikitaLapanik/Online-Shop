package ua.com.paw.service.impl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ua.com.paw.entity.User;
import ua.com.paw.entity.enums.Role;
import ua.com.paw.exceptions.ExceptionMessages;
import ua.com.paw.repositories.UserRepository;
import ua.com.paw.service.UserService;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public boolean createUser(User user) {
        String email = user.getEmail();
        if (userRepository.findByEmail(email) != null) {
            return false;
        }
        user.setActive(true);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if (user.getRoles().isEmpty()) {
            Set<Role> roles = new HashSet<>();
            roles.add(Role.ROLE_USER);
            user.setRoles(roles);
        }

        userRepository.save(user);
        log.info("User with email: {} was saved", email);
        return true;
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(ExceptionMessages.USER_WAS_NOT_FOUND));
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public void banUser(long id) {
        User user = getUserById(id);
        if (user.isActive()) {
            user.setActive(false);
            log.info("User with id: {} was banned", id);
        } else {
            user.setActive(true);
            log.info("User with id: {} was unbanned", id);
        }
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void changeUserRoles(User user, Map<String, String> form) {
        Set<String> roles = Arrays.stream(Role.values())
                .map(Role::name)
                .collect(Collectors.toSet());
        user.getRoles().clear();
        for (String value : form.values()) {
            if (roles.contains(value)) {
                user.getRoles().add(Role.valueOf(value));
                log.info("user get new role: {}", Role.valueOf(value));
            }
        }
        userRepository.save(user);
    }

    @Override
    public void deleteUser(long id) {
        userRepository.delete(getUserById(id));
        log.info("User with id: {} was deleted", id);
    }

    @Override
    public List<User> findAllByRole(Role role) {
        return userRepository.findByRole(role);
    }

    @Override
    public User findByEmail(String email){
        return userRepository.findByEmail(email);
    }
}
