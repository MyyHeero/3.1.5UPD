package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.entities.Role;
import ru.kata.spring.boot_security.demo.repositories.RoleDAO;

import java.util.List;
import java.util.Set;

@Service
public class RoleServiceImpl implements RoleService {
    private final RoleDAO roleDAO;

    @Autowired
    public RoleServiceImpl(RoleDAO roleDAO) {
        this.roleDAO = roleDAO;
    }

    @Transactional(readOnly = true)
    @Override
    public Role findByName(String name) {
        return roleDAO.findByName(name).orElseThrow(() -> new RuntimeException("Role not found"));
    }

    @Transactional(readOnly = true)
    @Override
    public Role findById(int id) {
       return roleDAO.findById(id).orElseThrow(() -> new RuntimeException("No role found with id: " + id));
    }

    @Transactional(readOnly = true)
    @Override
    public List<Role> findAll() {
        return roleDAO.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Set<Role> findByNameIn(Set<String> names) {
       return roleDAO.findByNameIn(names);
    }
}
