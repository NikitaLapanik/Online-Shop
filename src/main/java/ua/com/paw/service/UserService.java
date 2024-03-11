package ua.com.paw.service;

import ua.com.paw.entity.User;
import ua.com.paw.entity.enums.Role;

import java.util.List;
import java.util.Map;

public interface UserService {

    public boolean createUser(User user);
    public User getUserById(Long id);

    public List<User> getAllUsers();

    public void banUser(long id);

    public void changeUserRoles(User user, Map<String, String> form);

    public void deleteUser(long id);

    public List<User> findAllByRole(Role role);

    public User findByEmail(String email);

}
