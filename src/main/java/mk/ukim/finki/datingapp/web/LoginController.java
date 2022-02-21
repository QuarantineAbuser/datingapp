package mk.ukim.finki.datingapp.web;

import mk.ukim.finki.datingapp.config.DataInitializer;
import mk.ukim.finki.datingapp.models.User;
import mk.ukim.finki.datingapp.models.exceptions.InvalidUserCredentialsException;
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

    public LoginController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping
    public String getLoginPage(Model model, HttpServletRequest request) {
        if(request.getRemoteUser() != null)
            return "redirect:/timeline";
        model.addAttribute("bodyContent","login");
        return "master-template";
    }

    @PostMapping
    public String login(@RequestParam String username, @RequestParam String password, HttpServletRequest request,
                        Model model) {
        User user;
        try {
            user = authService.login(username, password);
            request.getSession().setAttribute("user", user);
            DataInitializer.ACTIVE_USER = user;
            return "redirect:/timeline";
        } catch (InvalidUserCredentialsException e) {
            model.addAttribute("hasError", true);
            model.addAttribute("error", e.getMessage());
            return "login";
        }
    }
}
