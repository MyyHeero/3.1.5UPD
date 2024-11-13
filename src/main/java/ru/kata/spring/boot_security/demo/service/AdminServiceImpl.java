package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.configs.dto.UserUpdateRequestDTO;
import ru.kata.spring.boot_security.demo.entities.Role;
import ru.kata.spring.boot_security.demo.entities.User;
import ru.kata.spring.boot_security.demo.repositories.RoleDAO;
import ru.kata.spring.boot_security.demo.repositories.UserDAO;
import ru.kata.spring.boot_security.demo.util.UserNotFoundException;

import java.util.*;

@Service
public class AdminServiceImpl implements AdminService {

    private final RoleDAO roleDAO;
    private final PasswordEncoder passwordEncoder;
    private final UserDAO userDAO;

    @Autowired
    public AdminServiceImpl(PasswordEncoder passwordEncoder, UserDAO userDAO, RoleDAO roleDAO) {
        this.roleDAO = roleDAO;
        this.passwordEncoder = passwordEncoder;
        this.userDAO = userDAO;
    }


    @Transactional
    public void save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword())); // Кодируем пароль
        if (user.getRoles() != null && !user.getRoles().isEmpty()) {
            user.setRoles(new HashSet<>(user.getRoles())); // Просто преобразуем роли в набор
        }
        userDAO.save(user); // Сохраняем пользователя
    }

    @Transactional
    public void delete(int id) {
        userDAO.delete(id);
    }

    @Transactional(readOnly = true)
    public User findById(Integer id) {
        return userDAO.findById(id).orElseThrow(UserNotFoundException::new);
    }

    @Transactional(readOnly = true)
    public List<User> findAll() {
        return userDAO.findAll();
    }

    @Transactional
    public void update(UserUpdateRequestDTO userDTO) {
        User existingUser = findById(userDTO.getId());
        if (existingUser == null) {
            throw new UserNotFoundException();
        }

        existingUser.setUsername(userDTO.getUsername());

        if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
            if (!passwordEncoder.matches(userDTO.getPassword(), existingUser.getPassword())) {
                existingUser.setPassword(passwordEncoder.encode(userDTO.getPassword()));

            }
        }
        existingUser.setAge(userDTO.getAge());
        existingUser.setFirstName(userDTO.getFirstName());
        existingUser.setLastName(userDTO.getLastName());
        Set<Role> updatedRoles = new HashSet<>(roleDAO.findByNameIn(userDTO.getRoles()));
        existingUser.setRoles(updatedRoles);
        userDAO.save(existingUser);
    }

}
