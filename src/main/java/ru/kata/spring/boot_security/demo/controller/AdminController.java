package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserServiceImpl;

import java.security.Principal;
import java.util.List;

@RestController
@ComponentScan("ru")
public class AdminController {
    private final UserServiceImpl userServiceImpl;

    @Autowired
    public AdminController(UserServiceImpl userServiceImpl) {
        this.userServiceImpl = userServiceImpl;
    }

    @GetMapping("/")
    public ModelAndView userPage(Principal principal) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("user");
        modelAndView.addObject("user", userServiceImpl.findByEmail(principal.getName()));
        return modelAndView;
    }

    @GetMapping("/admin")
    public ModelAndView adminPage(Principal principal) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("admin");
        modelAndView.addObject("user", userServiceImpl.findByEmail(principal.getName()));
        modelAndView.addObject("allRoles", userServiceImpl.getAllRoles());
        return modelAndView;
    }

    @GetMapping("/api/{id}")
    public User users(@PathVariable Long id) {
        return userServiceImpl.getByID(id);
    }

    @GetMapping("/api")
    public List<User> getAll() {
        return userServiceImpl.read();
    }

    @PostMapping("/api")
    public User create(@RequestBody User user) {
        userServiceImpl.create(user);
        return user;
    }

    @PutMapping("/api/{id}")
    public User update(@PathVariable long id, @RequestBody User user) {
        userServiceImpl.update(user);
        return user;
    }

    @DeleteMapping("/api/{id}")
    public void delete(@PathVariable Long id) {
        userServiceImpl.delete(id);
    }
}
