package ru.kata.spring.boot_security.demo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<List<User>> getUsers() {
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        return new ResponseEntity<>(userService.getUserById(id), HttpStatus.OK);
    }

    @GetMapping("/roles")
    public ResponseEntity<List<Role>> getRoles() {
        return new ResponseEntity<>(roleRepository.findAll(), HttpStatus.OK);
    }

    @PostMapping()
    //@ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<User> createUser(@RequestBody User userToCreate) {
        userService.saveUser(userToCreate);
        return new ResponseEntity<>(userToCreate, HttpStatus.CREATED);
    }

    @PutMapping("/update")
    //@ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<User> updateUser(@RequestBody User UpdatedUser) {
        userService.updateUser(UpdatedUser);
        return new ResponseEntity<>(UpdatedUser, HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/delete/{id}")
    //@ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<String> deleteUser(@RequestBody User userToDelete, @PathVariable Long id) {
        userService.removeUser(userToDelete);
        return new ResponseEntity<>("Deletion user with id = " + id + " successful", HttpStatus.ACCEPTED);
    }

}
