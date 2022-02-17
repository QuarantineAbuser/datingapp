package mk.ukim.finki.datingapp.web;

import mk.ukim.finki.datingapp.models.User;
import mk.ukim.finki.datingapp.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class UsersController {

    private final UserService userService;

    public UsersController(UserService userService) {
        this.userService = userService;
    }

    @ModelAttribute
    void headerAttributes(Model model, HttpServletRequest request){
        User activeUser = userService.getActiveUser(request);

        model.addAttribute("activeUser", activeUser);
        model.addAttribute("fragments/header", "activeUser");
    }

    @GetMapping({"/", "/users"})
    public String getUsersPage(Model model, HttpServletRequest request){
        List<User> users = userService.findUsersFor(request.getRemoteUser());

        model.addAttribute("users", users);
        model.addAttribute("bodyContent", "users_page");
        return "master-template";
        //return "users_page";

    }

    @GetMapping( "/interested")
    public String getInterestedInPage(Model model, HttpServletRequest request){
        List<User> interestedIn = userService.findInterestedFor(request.getRemoteUser());

        model.addAttribute("users", interestedIn);
        model.addAttribute("bodyContent", "interested_page");
        return "master-template";
        //return "interested_page";

    }

    @GetMapping( "/likedBy")
    public String getLikedByPage(Model model, HttpServletRequest request){
        List<User> likedBy = userService.findLikedByFor(request.getRemoteUser());

        model.addAttribute("users", likedBy);
        model.addAttribute("bodyContent", "likedBy_page");
        return "master-template";
        //return "likedBy_page";
    }

    @GetMapping( "/matches")
    public String getMatchesPage(Model model, HttpServletRequest request){
        User activeUser = userService.getActiveUser(request);
        userService.updateMatches(request.getRemoteUser());
        List<User> matches = activeUser.getMatched();

        model.addAttribute("users", matches);
        model.addAttribute("bodyContent", "matches_page");
        return "master-template";
        //return "matches_page";
    }

    @DeleteMapping( "/interested/{username}")
    public String addInterest(@PathVariable String username,HttpServletRequest request){
        userService.interested(request.getRemoteUser(), username);

        return "redirect:/users";
    }

    @DeleteMapping( "/uninterested/{username}")
    public String removeInterest(@PathVariable String username,HttpServletRequest request){
        userService.uninterested(request.getRemoteUser(), username);

        return "redirect:/interested";
    }

    @DeleteMapping( "/like/{username}")
    public String addLiked(@PathVariable String username,HttpServletRequest request){
        userService.like(request.getRemoteUser(), username);

        return "redirect:/interested";
    }

    @DeleteMapping( "/unlike/{username}")
    public String removeLiked(@PathVariable String username,HttpServletRequest request){
        userService.unlike(request.getRemoteUser(), username);

        return "redirect:/interested";
    }
    @DeleteMapping( "/delete/{username}")
    public String deleteLikedBy(@PathVariable String username,HttpServletRequest request){
        userService.unlike(username, request.getRemoteUser());
        return "redirect:/likedBy";
    }

    @GetMapping("/access_denied")
    public String accessDeniedPage(Model model) {
        model.addAttribute("bodyContent", "access_denied");
        return "master-template";
    }
}
