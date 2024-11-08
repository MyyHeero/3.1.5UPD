package ru.kata.spring.boot_security.demo.service;

import ru.kata.spring.boot_security.demo.entities.Role;

import java.util.List;

public interface RoleService {
    Role findByName(String name);
    Role findById(int id);
    List<Role> findAll();
}
