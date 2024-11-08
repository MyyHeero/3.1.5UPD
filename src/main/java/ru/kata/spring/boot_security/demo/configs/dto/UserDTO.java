package ru.kata.spring.boot_security.demo.configs.dto;

import java.util.Set;

public class UserDTO {

    private String firstName;
    private String lastName;
    private int age;
    private String username;
    private Set<String> roles;
    private String password;

    public UserDTO() {
    }

    public UserDTO(String firstName, String lastName, int age, String username, Set<String> roles, String password) {

        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.username = username;
        this.roles = roles;
        this.password = password;
    }


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
