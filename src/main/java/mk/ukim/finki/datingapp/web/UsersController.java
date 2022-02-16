package mk.ukim.finki.datingapp.web;

import mk.ukim.finki.datingapp.models.User;
import mk.ukim.finki.datingapp.models.exceptions.UserNotFoundException;
import mk.ukim.finki.datingapp.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class UsersController {

    private final UserService userService;

    public UsersController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = {"/", "/users"})
    public String getUsersPage(Model model, HttpServletRequest request){
        String username = request.getRemoteUser();
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));

        List<User> users = userService.listAll();
        users.removeAll(user.getInterestedIn());
        model.addAttribute("users", users);
        model.addAttribute("bodyContent", "users_page");
        return "master-template";
    }

    @GetMapping(value = "/interested")
    public String getInterestedInPage(Model model, HttpServletRequest request){
        String username = request.getRemoteUser();
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
        List<User> interestedIn = user.getInterestedIn();
        model.addAttribute("users", interestedIn);
        model.addAttribute("bodyContent", "interested_page");
        return "master-template";
    }

    @DeleteMapping(value = "/interested/{username}")
    public String addInterest(@PathVariable String username,HttpServletRequest request){
        String activeUsername = request.getRemoteUser();
        User activeUser = userService.findByUsername(activeUsername)
                .orElseThrow(() -> new UserNotFoundException(username));
        User interestedUser = userService.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
        userService.interest(activeUser, interestedUser);
        return "redirect:/users";
    }

    @DeleteMapping(value = "/uninterested/{username}")
    public String removeInterest(@PathVariable String username,HttpServletRequest request){
        String activeUsername = request.getRemoteUser();
        User activeUser = userService.findByUsername(activeUsername)
                .orElseThrow(() -> new UserNotFoundException(username));
        User interestedUser = userService.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
        userService.uninterested(activeUser, interestedUser);
        return "redirect:/interested";
    }

    @GetMapping("/access_denied")
    public String accessDeniedPage(Model model) {
        model.addAttribute("bodyContent", "access_denied");
        return "master-template";
    }
}
