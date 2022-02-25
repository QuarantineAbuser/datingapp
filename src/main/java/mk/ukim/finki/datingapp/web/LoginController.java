package mk.ukim.finki.datingapp.web;

import mk.ukim.finki.datingapp.models.exceptions.InvalidUserCredentialsException;
import mk.ukim.finki.datingapp.models.exceptions.UserNotFoundException;
import mk.ukim.finki.datingapp.service.AuthService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/login")
public class LoginController {

    private final AuthService authService;

    private final HttpServletRequest request;

    public LoginController(AuthService authService, HttpServletRequest request) {
        this.authService = authService;
        this.request = request;
    }

    @GetMapping
    public String getLoginPage(@RequestParam(required = false) String error, Model model) {
        if (error != null && !error.isEmpty()) {
            model.addAttribute("hasError", true);
            model.addAttribute("error", error);
        }
        if(request.getRemoteUser() != null)
            return "redirect:/timeline";
        model.addAttribute("bodyContent","login");
        return "master-template";
    }

    @PostMapping
    public String login(@RequestParam String username, @RequestParam String password) {
        try {
            authService.login(username, password);
            return "redirect:/timeline";
        } catch (InvalidUserCredentialsException e) {
            return "redirect:/login?error=" + e.getMessage();
        }
    }
}
