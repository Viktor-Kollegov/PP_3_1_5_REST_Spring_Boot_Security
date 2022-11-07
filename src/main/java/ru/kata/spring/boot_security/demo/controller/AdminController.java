package ru.kata.spring.boot_security.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;

@Controller
@RequestMapping(value = "/admin")
public class AdminController {
    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public String printWelcome(ModelMap modelMap) {
        modelMap.addAttribute("users", userService.getAllUsers());
        //Создаём атрибут users для работы с ним на странице users.html
        return "users";
    }

    @GetMapping(value = "/new")
    public String newUser(@ModelAttribute("user") User user) {
        //@ModelAttribute даёт доступ к экземпляру на странице создания
        return "create_users";
    }

    @PostMapping()
    public String createUser(@ModelAttribute("user") User user) {
        //Здесь же @ModelAttribute возвращает доступ обратно в контроллер
        userService.saveUser(user);
        return "redirect:/admin";
    }

    @GetMapping("/edit/{id}")
    public String editUserForm(@PathVariable Long id, Model model) {
        userService.findUserById(id).ifPresent(userToUpdate -> model
                .addAttribute("userToUpdate", userToUpdate));
        //С заданным атрибутом работаем на странице edit_users.html
        return "edit_users";
    }

    @PostMapping("/update/{id}")
    public String updateUser(User UpdatedUser) {
        //Получаем объект через действие в форме методом POST
        //Имя переменной задаём здесь произвольно
        userService.updateUser(UpdatedUser);
        return "redirect:/admin";
    }

//    @PostMapping("/promote/{id}") // не работает, getRoles возвращает null
//    public String promoteUserToAdmin(User userToPromote) {
//        // и достаём через действие методом POST самого юзера
//        //Имя переменной задаём здесь произвольно
//        Set<Role> newRoleSet = userToPromote.getRoles(); // null
//        newRoleSet.add(new Role(1L, "ROLE_ADMIN"));
//        userToPromote.setRoles(userToPromote.getRoles());
//        userService.saveUser(userToPromote);
//        return "redirect:/admin";
//    }

    @DeleteMapping("/delete/{id}")
    public String deleteUser(User userToDelete) {
        // и достаём через действие методом POST самого юзера
        //Имя переменной задаём здесь произвольно
        userService.removeUser(userToDelete);
        return "redirect:/admin";
    }

    @DeleteMapping("/clear")
    public String ClearTheTable() {
        userService.cleanUsersTable();
        return "redirect:/admin";
    }

    @PostMapping("/logout")
    public String logout() {
        return "redirect:/logout";
    }

}

