package ru.kata.spring.boot_security.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.security.Principal;

@Controller
@RequestMapping(value = "/admin")
public class AdminController {
    private final UserService userService;

    private final RoleRepository roleRepository;

    public AdminController(UserService userService, RoleRepository roleRepository) {
        this.userService = userService;
        this.roleRepository = roleRepository;
    }

    @GetMapping()
    public String printWelcome(ModelMap modelMap, Principal principal) {
        modelMap.addAttribute("users", userService.getAllUsers());
        modelMap.addAttribute("admin", userService.findByUsername(principal.getName()));
        modelMap.addAttribute("newUser", new User()); //для вкладки
        modelMap.addAttribute("roles", roleRepository.findAll()); //для вкладки
        // В модальное окно подтягивается данный юзер, без него инициализация th:field невозможна
        // Но, удаляется у нас юзер из цикла таймлиф, а не этот.
        modelMap.addAttribute("user", new User());
        // В прочем th:value покрывает потребности, в извращениях нет нужды
        return "users";
    }

    @PostMapping()
    public String createUser(User userToCreate, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "redirect:/admin";
        }
        userService.saveUser(userToCreate);
        return "redirect:/admin";
    }

    @PatchMapping("/update/{id}")
    public String updateUser(User UpdatedUser) {
        userService.updateUser(UpdatedUser);
        return "redirect:/admin";
    }

    @DeleteMapping("/delete/{id}")
    public String deleteUser(User userToDelete) {
        userService.removeUser(userToDelete);
        return "redirect:/admin";
    }

}

