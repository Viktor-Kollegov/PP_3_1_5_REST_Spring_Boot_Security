package ru.kata.spring.boot_security.demo.controller;

import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.List;

@RestController
@RequestMapping(value = "/admin/api")
public class AdminRestController {

    private final UserService userService;
    private final RoleRepository roleRepository;

    public AdminRestController(UserService userService, RoleRepository roleRepository) {
        this.userService = userService;
        this.roleRepository = roleRepository;
    }

    @GetMapping("/users")
    public List<User> getUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/roles")
    public List<Role> getRoles() {
        return roleRepository.findAll();
    }

    @PostMapping()
    public User createUser(@RequestBody User userToCreate) {
        userService.saveUser(userToCreate);
        return userToCreate;
    }

    @PutMapping("/update/{id}")
    public User updateUser(@RequestBody User UpdatedUser) {
        userService.updateUser(UpdatedUser);
        return UpdatedUser;
    }

    @DeleteMapping("/delete/{id}")
    public String deleteUser(@RequestBody User userToDelete) {
        userService.removeUser(userToDelete);
        return "Deletion successful";
    }

}
