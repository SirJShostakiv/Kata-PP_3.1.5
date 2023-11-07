package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserServiceImpl;

import java.security.Principal;
import java.util.List;

@Controller
@ComponentScan("ru")
public class UserController {
    private static final String REDIRECT = "redirect:/admin";
    private final UserServiceImpl userServiceImpl;

    @Autowired
    public UserController(UserServiceImpl userServiceImpl) {
        this.userServiceImpl = userServiceImpl;
    }
    @GetMapping("/")
    public String users(Model model, Principal principal) {
        User user = userServiceImpl.findByEmail(principal.getName());
        model.addAttribute("user", user);
        return "user";
    }

    @GetMapping("/admin")
    public List<User> onlyAdmins(Model model, Principal principal) {
        model.addAttribute("users", userServiceImpl.read());
        model.addAttribute("user", userServiceImpl.findByEmail(principal.getName()));
        model.addAttribute("allRoles", userServiceImpl.getAllRoles());
        model.addAttribute("userNew", new User());
        model.addAttribute("DAO", userServiceImpl.getDAO());
        return userServiceImpl.read();
    }

    @PostMapping("/admin")
    public String create(@ModelAttribute("user") User user) {
        userServiceImpl.create(user);
        return REDIRECT;
    }

    @PatchMapping("/admin/{id}")
    public String edit(@PathVariable("id") Long id, @ModelAttribute("user") User user) {
        userServiceImpl.update(user, id);
        return REDIRECT;
    }

    @DeleteMapping("/admin/{id}")
    public String delete(@PathVariable("id") Long id) {
        userServiceImpl.delete(id);
        return REDIRECT;
    }
}
