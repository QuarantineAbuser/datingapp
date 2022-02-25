package mk.ukim.finki.datingapp.web;

import mk.ukim.finki.datingapp.models.enumerations.Role;
import mk.ukim.finki.datingapp.models.enumerations.Sex;
import mk.ukim.finki.datingapp.models.exceptions.*;
import mk.ukim.finki.datingapp.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/register")
public class RegisterController {

    private final UserService userService;

    public RegisterController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String getRegisterPage(@RequestParam(required = false) String error, Model model) {
        if (error != null && !error.isEmpty()) {
            model.addAttribute("hasError", true);
            model.addAttribute("error", error);
        }
        model.addAttribute("bodyContent", "register");
        return "master-template";
    }

    @PostMapping
    public String register(@RequestParam String username,
                           @RequestParam String password,
                           @RequestParam String repeatedPassword,
                           @RequestParam String name,
                           @RequestParam String surname,
                           @RequestParam int age,
                           @RequestParam String bio,
                           @RequestParam String city,
                           @RequestParam Sex sex,
                           @RequestParam(required = false) Role role,
                           @RequestParam String phoneNumber) {
        try {
            if(role == null)
                role = Role.ROLE_USER;

            userService.register(username, password, repeatedPassword,
                    name, surname, role, age, bio, city, sex, phoneNumber);
            return "redirect:/login";
        } catch (UsernameAlreadyExistsException | PasswordsDoNotMatchException
                | InvalidArgumentsException | InvalidAgeException | WeakPasswordException e) {
            return "redirect:/register?error=" + e.getMessage();
        }
    }
}
