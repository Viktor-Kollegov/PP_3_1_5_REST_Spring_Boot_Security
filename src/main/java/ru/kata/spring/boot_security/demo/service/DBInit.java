package ru.kata.spring.boot_security.demo.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;
import ru.kata.spring.boot_security.demo.repository.UserRepository;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Service
public class DBInit {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public DBInit(UserRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @PostConstruct
    public void init() {
        makeAdmin();
        makeTestUser();
    }

    public Set<Role> RolesInit() {
        Role adminRole = new Role(1L, "ROLE_ADMIN");
        Role userRole = new Role(2L, "ROLE_USER");
        Set<Role> roles = new HashSet<>();
        roles.add(adminRole);
        roles.add(userRole);
        roleRepository.saveAll(roles);
        return roles;
    }

    public void makeAdmin() {
        if (userRepository.findByUsername("admin@mail.ru") == null) {
            User admin = User.builder()
                    .email("admin@mail.ru")
                    .firstName("Paul")
                    .lastName("Wallis")
                    .password(bCryptPasswordEncoder.encode("admin"))
                    .roles(RolesInit())
                    .age(26)
                    .singleRoleId(1L)
                    .build();
            userRepository.save(admin);
        }
    }

    public void makeTestUser() {
        if (userRepository.findByUsername("user@mail.ru") == null) {
            User testUser = User.builder()
                    .email("user@mail.ru")
                    .firstName("John")
                    .lastName("Titor")
                    .password(bCryptPasswordEncoder.encode("user"))
                    .roles(Collections.singleton(new Role(2L, "ROLE_USER")))
                    .age(16)
                    .singleRoleId(2L)
                    .build();
            userRepository.save(testUser);
        }
    }

}
