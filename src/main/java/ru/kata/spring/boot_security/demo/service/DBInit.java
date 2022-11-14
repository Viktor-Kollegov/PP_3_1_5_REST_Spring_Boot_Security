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
    public void RolesInit() {
        Role adminRole = new Role(1L, "ROLE_ADMIN");
        Role userRole = new Role(2L, "ROLE_USER");
        Set<Role> roles = new HashSet<>();
        roles.add(adminRole);
        roles.add(userRole);
        roleRepository.saveAll(roles);
        makeAdmin();
        makeTestUser();
    }

    public boolean makeAdmin() {
        User user = userRepository.findByUsername("admin");
        if (user != null) {
            return false;
        }
        User admin = User.builder()
                .firstName("Peter")
                .lastName("Wallis")
                .email("test@mail.ru")
                .password(bCryptPasswordEncoder.encode("admin"))
                .roles(Collections.singleton(new Role(1L, "ROLE_ADMIN")))
                .username("admin")
                .build();
        userRepository.save(admin);
        return true;
    }

    public boolean makeTestUser() {
        User user = userRepository.findByUsername("user");
        if (user != null) {
            return false;
        }
        User testUser = User.builder()
                .firstName("John")
                .lastName("Titor")
                .email("test2@mail.ru")
                .password(bCryptPasswordEncoder.encode("user"))
                .roles(Collections.singleton(new Role(2L, "ROLE_USER")))
                .username("user")
                .build();
        userRepository.save(testUser);
        return true;
    }
}
