package ru.kata.spring.boot_security.demo.repositories;

import ru.kata.spring.boot_security.demo.configs.dto.UserUpdateRequestDTO;
import ru.kata.spring.boot_security.demo.entities.User;

import java.util.List;
import java.util.Optional;

public interface UserDAO {

    void save(User user);

    void delete(int id);

    Optional<User> findById(Integer id);

    List<User> findAll();

    void update(UserUpdateRequestDTO user);

    public Optional<User> findByUsername(String username);

}
