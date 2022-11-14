package ru.kata.spring.boot_security.demo.controller;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.validation.Valid;
import java.util.Objects;

@Controller
@RequestMapping(value = "/admin")
public class AdminController {
    private final UserService userService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public AdminController(UserService userService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userService = userService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @GetMapping()
    public String printWelcome(ModelMap modelMap) {
        modelMap.addAttribute("users", userService.getAllUsers());
        return "users";
    }

    @GetMapping(value = "/new")
    public String newUser(@ModelAttribute("user") User user) {
        return "create_users";
    }

    @PostMapping()
    public String createUser(@ModelAttribute("user") @Valid User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "create_users";
        }
        userService.saveUser(user);
        return "redirect:/admin";
    }

    @GetMapping("/edit/{id}")
    public String editUserForm(@PathVariable Long id, Model model) {
        if (!model.containsAttribute("userToUpdate")) {
            userService.findUserById(id).ifPresent(userToUpdate ->
                    model.addAttribute("userToUpdate", userToUpdate));
        }
        return "edit_users";
    }

    @PostMapping("/update/{id}")
    public String updateUser(@RequestParam("newPassword") String newPassword,
                             @RequestParam("passwordConfirm") String passwordConfirm,
                             @RequestParam("previousPassword") String previousPassword,
                             @ModelAttribute("userToUpdate") @Valid User UpdatedUser,
                             BindingResult bindingResult,
                             Model model) {
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

    @DeleteMapping("/clear")
    public String ClearTheTable() {
        userService.cleanUsersTable();
        return "redirect:/admin";
    }

    //С этим методом выводится окно с уточнением, действительно ли мы хотим разлогиниться.
//    @PostMapping("/logout")
//    public String logout() {
//        return "redirect:/logout";
//    }

}

