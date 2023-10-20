package ru.kata.spring.boot_security.demo.controller;

import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
@RequestMapping("/admin")
@ComponentScan("ru")
public class UsersController {
    private static final String REDIRECT = "redirect:/admin";
    private final UserServiceImpl userServiceImpl;

    @Autowired
    public UsersController(UserServiceImpl userServiceImpl) {
        this.userServiceImpl = userServiceImpl;
    }

    @GetMapping()
    public String onlyAdmins(Model model, Principal principal) throws SQLException {
        model.addAttribute("username", principal.getName());
        model.addAttribute("rolesList", userServiceImpl.getRoles(principal.getName()));
        model.addAttribute("users", userServiceImpl.read());
        model.addAttribute("user", userServiceImpl.findByUsername(principal.getName()));
        model.addAttribute("DAO", userServiceImpl.getDAO());
        return "admin";
    }
    @PostMapping()
    public String create(@ModelAttribute("user") User user) {
        userServiceImpl.create(user);
        return REDIRECT;
    }

    @GetMapping("/enter_id")
    public String getId() {
        return "enter_id";
    }

    @GetMapping("/{id}")
    public String getEdit(@PathVariable("id") @RequestParam("id") Long id, Model model) {
        List<Long> idList = new ArrayList<>(userServiceImpl.getIdList());
        if (!idList.contains(id)) {
            return "input_error";
        }
        User user = userServiceImpl.getByID(id);
        model.addAttribute("user", user);
        return "edit";
    }

    @PatchMapping("/{id}")
    public String edit(@ModelAttribute("user") User user, @PathVariable("id") Long id) {
        userServiceImpl.update(user, id);
        return REDIRECT;
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") Long id) {
        userServiceImpl.delete(id);
        return REDIRECT;
    }
}
