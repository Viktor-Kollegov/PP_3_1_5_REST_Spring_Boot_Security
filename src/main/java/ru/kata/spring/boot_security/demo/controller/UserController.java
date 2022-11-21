package ru.kata.spring.boot_security.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.security.Principal;

/*
Кроме того, если вы используете бутстрап 5 и при этом подключаете джсник от него
(а вам, скорее всего, придётся подключить его), то нужно будет использовать
preventDefault (я делал это внутри addEventListener) на некоторых элементах,
чтобы игнорировалось поведение, например, кнопки по умолчанию (которое вполне
может начать перезагружать страницу, чего нам не надо)
 */

@Controller
@RequestMapping(value = "/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public String userPage() {
        return "RESTuser";
    }

    @ResponseBody
    @GetMapping("/api")
    public User getUser(Principal principal) {
        return userService.findByUsername(principal.getName());
    }

}
