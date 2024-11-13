package ru.kata.spring.boot_security.demo.repositories;

import ru.kata.spring.boot_security.demo.entities.Role;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface RoleDAO {
    Optional<Role> findByName(String name);
    List<Role> findAll();
    Set<Role> findByNameIn(Set<String> names);
   Optional<Role> findById(int id);

}
