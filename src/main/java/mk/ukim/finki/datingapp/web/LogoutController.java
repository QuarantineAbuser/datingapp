package mk.ukim.finki.datingapp.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/logout")
public class LogoutController {

    private final HttpServletRequest request;

    public LogoutController(HttpServletRequest request) {
        this.request = request;
    }

    @GetMapping
    public String logout(){
        request.getSession().invalidate();
        return "redirect:/login";
    }
}
