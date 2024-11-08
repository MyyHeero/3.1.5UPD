package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.configs.dto.UserDTO;
import ru.kata.spring.boot_security.demo.configs.dto.UserMapper;
import ru.kata.spring.boot_security.demo.entities.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.security.Principal;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class UsersController {

    private final UserMapper userMapper;
    private final UserService userService;

    @Autowired
    public UsersController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @GetMapping("/user")
    public UserDTO showUserInfo(Principal principal) {
        User user = userService.findByUsername(principal.getName());
        return userMapper.toDTO(user);
    }
}
