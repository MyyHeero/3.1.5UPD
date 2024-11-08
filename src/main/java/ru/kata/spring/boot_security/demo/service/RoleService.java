package ru.kata.spring.boot_security.demo.service;

import ru.kata.spring.boot_security.demo.entities.Role;

import java.util.List;
import java.util.Set;

public interface RoleService {
    Role findByName(String name);
    Role findById(int id);
    List<Role> findAll();
    Set<Role> findByNameIn(Set<String> names);
}
