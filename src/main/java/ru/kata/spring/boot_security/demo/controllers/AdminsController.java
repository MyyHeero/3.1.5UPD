package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.entities.User;
import ru.kata.spring.boot_security.demo.service.AdminService;
import ru.kata.spring.boot_security.demo.service.UserService;

@Controller
public class AdminsController {

    AdminService adminService;

    @Autowired
    public AdminsController(AdminService userService) {
        this.adminService = userService;
    }

    @GetMapping("/admin/users")
    public String getAllUsers(Model model) {
        model.addAttribute("users", adminService.findAll());
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
    public String newUser(@ModelAttribute("user") User user) {
        return "new";
    }

    @PostMapping("/admin")
    public String saveUser(@ModelAttribute("user") User user) {
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
    public String updateUser(@RequestParam("id") int id, @ModelAttribute("user") User user) {

        adminService.updateById(id, user);
        return "redirect:/admin/users";
    }

    @PostMapping("/admin/users/delete")
    public String deleteUser(@RequestParam("id") int id) {
        adminService.deleteById(id);
        return "redirect:/admin/users";
    }

}
