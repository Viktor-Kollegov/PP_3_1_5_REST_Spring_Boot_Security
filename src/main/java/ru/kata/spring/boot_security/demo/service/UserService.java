package ru.kata.spring.boot_security.demo.service;

import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    boolean saveUser(User user);

    void removeUser(User userToDelete);

    void updateUser(User updatedUser);

    User findByUsername(String username);

    Optional<User> findUserById(Long id);

    List<User> getAllUsers();

    void cleanUsersTable();

}
