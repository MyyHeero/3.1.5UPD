package ru.kata.spring.boot_security.demo.service;

import ru.kata.spring.boot_security.demo.entities.User;

import java.util.List;

public interface AdminService {

    void save(User user);

    void deleteById(int id);

    User findById(Integer id);

    List<User> findAll();

    void updateById(int id, User user);
}
