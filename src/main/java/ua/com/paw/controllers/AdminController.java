package ua.com.paw.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.com.paw.entity.User;
import ua.com.paw.entity.enums.Role;
import ua.com.paw.service.UserService;

import java.util.Map;

@Controller
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class AdminController {

    @Autowired
    private UserService userService;

    @GetMapping("/admin")
    public String admin(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "admin";
    }

    @RequestMapping(value = "/admin/user/ban/{id}", method = {RequestMethod.POST, RequestMethod.PUT})
    public String userBan(@PathVariable("id") long id) {
        userService.banUser(id);
        return "redirect:/admin";
    }

    @GetMapping("/admin/user/edit/{id}")
    public String editUser(@PathVariable("id") long id, Model model){
        User user = userService.getUserById(id);
        model.addAttribute("id", id);
        model.addAttribute("roles", Role.values());
        model.addAttribute("user", user);
        return "user-edit";
    }

    @PostMapping("/admin/user/edit")
    public String userEdit(@RequestParam("userId") long userId, @RequestParam Map<String, String> form){
        User user = userService.getUserById(userId);
        userService.changeUserRoles(user, form);
        return "redirect:/admin";
    }

    @RequestMapping(value = "/admin/user/delete/{id}", method = {RequestMethod.POST, RequestMethod.DELETE})
    public String deleteUser(@PathVariable("id") long id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }

    @GetMapping("/admin/user/email")
    public String getUserByEmail(@RequestParam(name = "email") String email, Model model) {
        User foundUser = userService.findByEmail(email);
        if (foundUser != null) {
            model.addAttribute("userByEmail", foundUser);
        } else {
            model.addAttribute("noResultsMessage", "No user found for the email: " + email);
        }
        return "admin";
    }
}
