package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.entities.Role;
import ru.kata.spring.boot_security.demo.entities.User;
import ru.kata.spring.boot_security.demo.repositories.RoleRepository;
import ru.kata.spring.boot_security.demo.service.AdminService;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserDetailsServiceImpl;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class AdminsController {

    private final AdminService adminService;
    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public AdminsController(AdminService adminService, UserService userService, RoleService roleService) {
        this.roleService = roleService;
        this.adminService = adminService;
        this.userService = userService;
    }

    @GetMapping("/admin/users")
    public String getAllUsers(Model model, Principal principal) {
        model.addAttribute("users", adminService.findAll());
        model.addAttribute("page", "admin");
        User user = userService.findByUsername(principal.getName());
        model.addAttribute("user", user);
        Set<String> roles = user.getRoles().stream().map(role -> role.getName()).collect(Collectors.toSet());
        model.addAttribute("roles", roles);
        return "showallusers";
    }

    @GetMapping("/admin/users/view")
    public String getUserById(@RequestParam("id") int id, Model model) {
        User user = adminService.findById(id);
        if (user == null) {
            return "redirect:/admin/users";
        }
        model.addAttribute("user", user);
        return "index";
    }

    @GetMapping("/admin/users/new")
    public String newUser(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        model.addAttribute("page", "newUser");
        return "new";
    }

    @PostMapping("/admin")
    public String saveUser(@ModelAttribute("user") User user, @RequestParam("roles") List<Integer> roleIds) {
        Set<Role> userRoles = new HashSet<>();
        for (Integer roleId : roleIds) {
            Role roleEntity = roleService.findById(roleId);
            if (roleEntity != null) {
                userRoles.add(roleEntity);
            }
        }
        user.setRoles(userRoles);
        adminService.save(user);
        return "redirect:/admin/users";
    }


    @GetMapping("/admin/users/edit")
    public String editUser(@RequestParam("id") int id, Model model) {
        User user = adminService.findById(id);
        if (user == null) {
            return "redirect:/admin/users";
        }
        model.addAttribute("user", user);
        return "edit";
    }

    @PostMapping("/admin/users/update")
    public String updateUser(@RequestParam("id") int id, @ModelAttribute("user") User user, @RequestParam("roles") List<Integer> roleIds) {
        Set<Role> userRoles = new HashSet<>();
        for (Integer roleId : roleIds) {
            Role roleEntity = roleService.findById(roleId);
            if (roleEntity != null) {
                userRoles.add(roleEntity);
            }
        }
        User existingUser = adminService.findById(id);
        existingUser.setRoles(userRoles);
        existingUser.setFirstName(user.getFirstName());
        existingUser.setLastName(user.getLastName());
        existingUser.setAge(user.getAge());
        existingUser.setUsername(user.getUsername());
        adminService.updateById(id, user);
        return "redirect:/admin/users";
    }

    @PostMapping("/admin/users/delete")
    public String deleteUser(@RequestParam("id") int id) {
        adminService.deleteById(id);
        return "redirect:/admin/users";
    }

}
