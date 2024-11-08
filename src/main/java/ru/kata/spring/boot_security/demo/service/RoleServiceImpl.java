package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.entities.Role;
import ru.kata.spring.boot_security.demo.repositories.RoleRepository;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public Role findByName(String name) {
        return roleRepository.findByName(name);
    }

    @Transactional(readOnly = true)
    @Override
    public Role findById(int id) {
       return roleRepository.findById(id).orElseThrow(() -> new RuntimeException("No role found with id: " + id));
    }

    @Override
    public List<Role> findAll() {
        return roleRepository.findAll();
    }
}
