package ru.kata.spring.boot_security.demo.controller;

import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/admin")
@ComponentScan("ru")
public class AdminController {
    private static final String REDIRECT = "redirect:/admin";
    private final UserServiceImpl userServiceImpl;

    @Autowired
    public AdminController(UserServiceImpl userServiceImpl) {
        this.userServiceImpl = userServiceImpl;
    }

    @GetMapping("")
    public String onlyAdmins(Model model, Principal principal) {
        model.addAttribute("users", userServiceImpl.read());
        model.addAttribute("user", userServiceImpl.findByEmail(principal.getName()));
        model.addAttribute("allRoles", userServiceImpl.getAllRoles());
        model.addAttribute("userNew", new User());
        model.addAttribute("DAO", userServiceImpl.getDAO());
        return "admin";
    }

    @PostMapping("/add")
    public String create(@ModelAttribute("user") User user) {
        userServiceImpl.create(user);
        return REDIRECT;
    }

    @PatchMapping("/{id}")
    public String edit(@PathVariable("id") Long id, @ModelAttribute("user") User user) {
        userServiceImpl.update(user, id);
        return REDIRECT;
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") Long id) {
        userServiceImpl.delete(id);
        return REDIRECT;
    }
}
