package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.configs.dto.UserUpdateRequestDTO;
import ru.kata.spring.boot_security.demo.entities.Role;
import ru.kata.spring.boot_security.demo.entities.User;
import ru.kata.spring.boot_security.demo.repositories.RoleRepository;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;
import ru.kata.spring.boot_security.demo.util.UserNotFoundException;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AdminServiceImpl implements AdminService {

    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Autowired
    public AdminServiceImpl(PasswordEncoder passwordEncoder, UserRepository userRepository, RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
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
    public void delete(int id) {
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
        Set<Role> updatedRoles = new HashSet<>(roleRepository.findByNameIn(userDTO.getRoles()));
        existingUser.setRoles(updatedRoles);
        userRepository.save(existingUser);
    }

}
