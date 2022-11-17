package ru.kata.spring.boot_security.demo.controller;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.validation.Valid;
import java.security.Principal;
import java.util.*;

@Controller
@RequestMapping(value = "/admin")
public class AdminController {
    private final UserService userService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final RoleRepository roleRepository;

    public AdminController(UserService userService, BCryptPasswordEncoder bCryptPasswordEncoder, RoleRepository roleRepository) {
        this.userService = userService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
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
        // modelMap.addAttribute("user", new User());
        // В прочем th:value покрывает потребности, в извращениях нет нужды
        return "users";
    }

    @PostMapping()
    public String createUser(@Valid User userToCreate, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "redirect:/admin";
        }
        userService.saveUser(userToCreate);
        return "redirect:/admin";
    }

    @GetMapping("/edit/{id}")
    public String editUserForm(@PathVariable Long id, Model model) {
        if (!model.containsAttribute("userToUpdate")) {
            userService.findUserById(id).ifPresent(userToUpdate ->
                    model.addAttribute("userToUpdate", userToUpdate));
        }
        model.addAttribute("roles", roleRepository.findAll());
        return "edit_users";
    }

    @PatchMapping("/update/{id}")
    public String updateUser(@RequestParam("newPassword") String newPassword,
                             @RequestParam("passwordConfirm") String passwordConfirm,
                             @RequestParam("previousPassword") String previousPassword,
                             @ModelAttribute("userToUpdate") @Valid User UpdatedUser,
                             BindingResult bindingResult,
                             Model model) {
        model.addAttribute("roles", roleRepository.findAll());
        if (bindingResult.hasErrors()) {
            return "edit_users";
        } else if (!Objects.equals(passwordConfirm, newPassword)) {
            model.addAttribute("error", "The entered password pair does not match");
            return "edit_users";
        } else if (bCryptPasswordEncoder.matches(previousPassword, UpdatedUser.getPassword())) {
            if (newPassword.length() == 0) {
                newPassword = previousPassword;
            }
            UpdatedUser.setPassword(newPassword);
            userService.updateUser(UpdatedUser);
        } else {
            model.addAttribute("error", "Current password mismatch");
            return "edit_users";
        }
        return "redirect:/admin";
    }

    @DeleteMapping("/delete/{id}")
    public String deleteUser(User userToDelete) {
        userService.removeUser(userToDelete);
        return "redirect:/admin";
    }

//    Сергей Горностаев @sergey-gornostaev Куратор тега Java
//    Седой и строгий
//    Во-первых, у Thymeleaf нет модальных окон. Модальное окно у вас относится к
//    Bootstrap. Во-вторых, важно понимать, что Thymeleaf - это шаблонизатор,
//    он выполняется на бэкенде, а Bootstrap выполняется на фронтенде. Бэкенд и
//    фронтенд - это две разных программы, написанные на разных языках и работающие
//    на разных компьютерах в разное время. Так что вам придётся либо в цикле
//    шаблонизатора наплодить разных модалок на каждой итерации, либо придётся
//    написать javascript-код, который будет передавать данные из нажатой кнопки в
//    единственное модальное окно. Естественно, второй вариант разумнее.
//    Ответ написан более трёх лет назад

}

