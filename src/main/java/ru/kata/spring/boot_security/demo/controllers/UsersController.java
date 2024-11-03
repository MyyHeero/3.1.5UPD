package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.entities.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.security.Principal;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class UsersController {

    private final UserService userService;

    @Autowired
    public UsersController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user")
    public String showUserInfo(Model model, Principal principal) {
        User user = userService.findByUsername(principal.getName());

        model.addAttribute("user", user);

        Set<String> roles = user.getRoles().stream().map(role -> role.getName()).collect(Collectors.toSet());

        model.addAttribute("roles", roles);

        model.addAttribute("page", "user");
        return "showuser";
    }
}
