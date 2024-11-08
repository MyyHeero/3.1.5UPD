package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.entities.Role;
import ru.kata.spring.boot_security.demo.entities.User;
import ru.kata.spring.boot_security.demo.repositories.RoleRepository;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;
import ru.kata.spring.boot_security.demo.util.UserNotFoundException;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
public class AdminServiceImpl implements AdminService {

    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Autowired
    public AdminServiceImpl(PasswordEncoder passwordEncoder, UserRepository userRepository, RoleService roleService) {
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }


    @Transactional
    public void save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword())); // Кодируем пароль
        if (user.getRoles() != null && !user.getRoles().isEmpty()) {
            user.setRoles(new HashSet<>(user.getRoles())); // Просто преобразуем роли в набор
        }
        userRepository.save(user); // Сохраняем пользователя
    }

    @Transactional
    public void deleteById(int id) {
        userRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public User findById(Integer id) {
        Optional<User> optional = userRepository.findById(id);
        return optional.orElseThrow(UserNotFoundException::new);
    }

    @Transactional(readOnly = true)
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Transactional
    public void updateById(int id, User user) {
        User existingUser = findById(id);

        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        existingUser.setAge(user.getAge());
        existingUser.setFirstName(user.getFirstName());
        existingUser.setLastName(user.getLastName());

        if (user.getRoles() != null && !user.getRoles().isEmpty()) {
            // Получаем первую роль для добавления
            Role newRole = roleService.findByName(user.getRoles().iterator().next().getName());

            // Если роль уже есть, не добавляем ее
            if (!existingUser.getRoles().contains(newRole)) {
                existingUser.getRoles().add(newRole); // Добавляем новую роль
            }
        }

        userRepository.save(existingUser);
    }

}
