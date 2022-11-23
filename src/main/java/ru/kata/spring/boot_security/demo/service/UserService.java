package ru.kata.spring.boot_security.demo.service;

import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

public interface UserService {

    void saveUser(User user);

    void removeUser(User userToDelete);

    void updateUser(User updatedUser);

    User findByUsername(String username);

    List<User> getAllUsers();

    User getUserById(Long id);

}
