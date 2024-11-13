package ru.kata.spring.boot_security.demo.configs.dto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.entities.Role;
import ru.kata.spring.boot_security.demo.entities.User;
import ru.kata.spring.boot_security.demo.repositories.RoleDAO;
import ru.kata.spring.boot_security.demo.service.RoleService;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UserMapper {


    private final RoleService roleService;

    @Autowired
    public UserMapper(RoleService roleService) {
        this.roleService = roleService;
    }


    public UserDTO toDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setAge(user.getAge());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setRoles(user.getRoles().stream().map(role -> role.getName()).collect(Collectors.toSet()));
        userDTO.setPassword(user.getPassword());
        return userDTO;
    }

    public User toEntity(UserDTO userDto) {
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setAge(userDto.getAge());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        Set<Role> roles = userDto.getRoles().stream().map(role -> roleService.findByName(role))
                .filter(role -> role != null)
                .collect(Collectors.toSet());
        user.setRoles(roles);
        user.setPassword(userDto.getPassword());
        return user;
    }


}
