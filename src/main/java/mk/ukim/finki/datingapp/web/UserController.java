package mk.ukim.finki.datingapp.web;

import mk.ukim.finki.datingapp.models.User;
import mk.ukim.finki.datingapp.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

//    public String reloadPage(HttpServletRequest request){
//        //HttpServletRequest request
//        String referrer = request.getHeader("referer").substring(21);
//        return "redirect:" + referrer;
//    }

    @ModelAttribute
    void headerAttributes(Model model, HttpServletRequest request){
        User activeUser = userService.getActiveUser(request);

        model.addAttribute("activeUser", activeUser);
        model.addAttribute("fragments/header", "activeUser");
    }

    @GetMapping()
    public String getUsersPage(Model model, HttpServletRequest request){
        List<User> users = userService.findUsersFor(request.getRemoteUser());

        model.addAttribute("users", users);
        model.addAttribute("bodyContent", "users_page");
        return "master-template";
    }

    @GetMapping( "/interested")
    public String getInterestedInPage(Model model, HttpServletRequest request){
        List<User> interestedIn = userService.findInterestedFor(request.getRemoteUser());

        model.addAttribute("users", interestedIn);
        model.addAttribute("bodyContent", "interested_page");
        return "master-template";
    }

    @GetMapping( "/likedBy")
    public String getLikedByPage(Model model, HttpServletRequest request){
        List<User> likedBy = userService.findLikedByFor(request.getRemoteUser());

        model.addAttribute("users", likedBy);
        model.addAttribute("bodyContent", "likedBy_page");
        return "master-template";
    }

    @GetMapping( "/matches")
    public String getMatchesPage(Model model, HttpServletRequest request){
        User activeUser = userService.getActiveUser(request);
        userService.updateMatches(request.getRemoteUser());
        List<User> matches = activeUser.getMatched();

        model.addAttribute("users", matches);
        model.addAttribute("bodyContent", "matches_page");
        return "master-template";
    }

    @DeleteMapping( "/interested/{username}")
    public String addInterest(@PathVariable String username, HttpServletRequest request){
        userService.interested(request.getRemoteUser(), username);
        String referrer = request.getHeader("referer").substring(21);

        return "redirect:" + referrer;
    }

    @DeleteMapping( "/uninterested/{username}")
    public String removeInterest(@PathVariable String username,HttpServletRequest request){
        userService.uninterested(request.getRemoteUser(), username);
        String referrer = request.getHeader("referer").substring(21);

        return "redirect:" + referrer;
    }

    @DeleteMapping( "/like/{username}")
    public String addLiked(@PathVariable String username,HttpServletRequest request){
        userService.like(request.getRemoteUser(), username);

        String referrer = request.getHeader("referer").substring(21);

        return "redirect:" + referrer;
    }

    @DeleteMapping( "/unlike/{username}")
    public String removeLiked(@PathVariable String username,HttpServletRequest request){
        userService.unlike(request.getRemoteUser(), username);
        String referrer = request.getHeader("referer").substring(21);

        return "redirect:" + referrer;
    }
    @DeleteMapping( "/delete/{username}")
    public String deleteLikedBy(@PathVariable String username,HttpServletRequest request){
        userService.unlike(username, request.getRemoteUser());

        String referrer = request.getHeader("referer").substring(21);

        return "redirect:" + referrer;
    }

    @GetMapping("/access_denied")
    public String accessDeniedPage(Model model) {
        model.addAttribute("bodyContent", "access_denied");
        return "master-template";
    }
}
