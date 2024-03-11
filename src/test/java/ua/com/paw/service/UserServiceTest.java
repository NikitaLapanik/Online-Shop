package ua.com.paw.service;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.com.paw.entity.User;
import ua.com.paw.entity.enums.Role;
import ua.com.paw.exceptions.ExceptionMessages;
import ua.com.paw.repositories.UserRepository;
import ua.com.paw.service.impl.UserServiceImpl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    void testCreateUser_Success() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("password");
        when(userRepository.findByEmail("test@example.com")).thenReturn(null);
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");

        boolean result = userService.createUser(user);

        assertTrue(result);
        assertTrue(user.isActive());
        assertEquals("encodedPassword", user.getPassword());
        verify(userRepository, times(1)).save(user);
        verify(passwordEncoder, times(1)).encode("password");
    }

    @Test
    void testCreateUser_UserAlreadyExists() {
        User user = new User();
        user.setEmail("existing@example.com");
        when(userRepository.findByEmail("existing@example.com")).thenReturn(new User());

        boolean result = userService.createUser(user);

        assertFalse(result);
        verify(userRepository, never()).save(user);
        verify(passwordEncoder, never()).encode(anyString());
    }

    @Test
    void testGetUserById_Success() {
        User user = createTestUser();
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        assertEquals(user.getEmail(), userService.getUserById(1L).getEmail());
        assertEquals(user.getName(), userService.getUserById(1L).getName());
    }

    @Test
    void testGetUserById_WrongId() {
        long wrongId = 234234234L;
        EntityNotFoundException exception = Assertions.assertThrows(EntityNotFoundException.class,
                () -> userService.getUserById(wrongId));
        assertEquals(ExceptionMessages.USER_WAS_NOT_FOUND, exception.getMessage());
    }

    @Test
    void testGetAllUsers() {
        List<User> users = new ArrayList<>();
        Collections.addAll(users, new User(), new User(), new User());
        Mockito.when(userRepository.findAll()).thenReturn(users);
        assertEquals(3, userService.getAllUsers().size());
    }

    @Test
    void testBanUser_Success() {
        User user = createTestUser();
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        userService.banUser(1L);
        Mockito.verify(userRepository).save(Mockito.any());
    }

    @Test
    void testBanUser_WrongId() {
        long wrongId = 1231231L;
        Mockito.when(userRepository.findById(wrongId)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> {
            userService.banUser(wrongId);
        });

        verify(userRepository, never()).save(any());
    }

    @Test
    void testChangeUserRoles_Success() {
        User user = createTestUser();

        Map<String, String> form = new HashMap<>();
        form.put("role", "ROLE_ADMIN");

        when(userRepository.save(any())).then(AdditionalAnswers.returnsFirstArg());

        userService.changeUserRoles(user, form);

        verify(userRepository, times(1)).save(user);

        assertTrue(user.getRoles().contains(Role.ROLE_ADMIN));
        assertFalse(user.getRoles().contains(Role.ROLE_USER));
    }

    @Test
    void testDeleteUser_Success() {
        User user = createTestUser();
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        userService.deleteUser(1L);
        Mockito.verify(userRepository, times(1)).delete(user);
    }

    @Test
    void testDeleteUser_WrongID() {
        long wrongId = 1231231L;
        Mockito.when(userRepository.findById(wrongId)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> {
            userService.deleteUser(wrongId);
        });
        verify(userRepository, never()).delete(any());
    }

    @Test
    void testFindByRole_Success() {
        Mockito.when(userRepository.findByRole(Role.ROLE_USER)).thenReturn(List.of(new User(), new User()));
        userService.findAllByRole(Role.ROLE_USER);
        assertEquals(userRepository.findByRole(Role.ROLE_USER).size(), 2);
    }

    @Test
    void testFindByEmail_Success() {
        User user = createTestUser();
        Mockito.when(userRepository.findByEmail(user.getEmail())).thenReturn(user);
        assertEquals(userService.findByEmail(user.getEmail()), user);
        assertEquals(userService.findByEmail(user.getEmail()).getName(), user.getName());
    }

    @Test
    void testFindByEmail_WrongEmail() {
        String wrongEmail = "werew@werfsdf";
        assertNull(userService.findByEmail(wrongEmail));
    }

    private User createTestUser(){
        User user = new User();
        user.setId(1L);
        user.setActive(true);
        user.setPassword("11");
        user.setEmail("email");
        user.setName("name");

        Set<Role> userRoles = new HashSet<>();
        userRoles.add(Role.ROLE_USER);
        user.setRoles(userRoles);

        return user;
    }
}
