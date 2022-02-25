package mk.ukim.finki.datingapp.web;

import mk.ukim.finki.datingapp.models.User;
import mk.ukim.finki.datingapp.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    private final HttpServletRequest request;

    public UserController(UserService userService, HttpServletRequest request) {
        this.userService = userService;
        this.request = request;
    }

    public String reloadPage() {
        String referrer = request.getHeader("referer").substring(21);
        return "redirect:" + referrer;
    }

    @ModelAttribute("activeUser")
    public User activeUser(){
        return userService.getActiveUser();
    }

    @GetMapping()
    public String getUsersPage(@RequestParam(required = false) String keyword,
                               @RequestParam(required = false) String age,
                               @RequestParam(required = false) String city,
                               @RequestParam(required = false) String sex,
                               Model model) {

        List<User> users = userService.findUsersFor(userService.getActiveUser().getUsername());
        if ((keyword != null && !keyword.isEmpty())
                || (age != null && !age.isEmpty())
                || (city != null && !city.isEmpty())
                || (sex != null && !sex.isEmpty())) {
            users = userService.filterUsers(users, keyword, age, city, sex);
        }

        model.addAttribute("users", users);
        model.addAttribute("keyword", keyword);
        model.addAttribute("age", age);
        model.addAttribute("city", city);
        model.addAttribute("sex", sex);
        model.addAttribute("bodyContent", "users_page");
        return "master-template";
    }

    @GetMapping("/interested")
    public String getInterestedInPage(Model model) {
        List<User> interestedIn = userService.findInterestedFor(userService.getActiveUser().getUsername());

        model.addAttribute("users", interestedIn);
        model.addAttribute("bodyContent", "interested_page");
        return "master-template";
    }

    @GetMapping("/likedBy")
    public String getLikedByPage(Model model) {
        List<User> likedBy = userService.findLikedByFor(userService.getActiveUser().getUsername());

        model.addAttribute("users", likedBy);
        model.addAttribute("bodyContent", "likedBy_page");
        return "master-template";
    }

    @GetMapping("/matches")
    public String getMatchesPage(Model model) {
        userService.updateMatches(userService.getActiveUser().getUsername());
        List<User> matches = userService.getActiveUser().getMatched();

        model.addAttribute("users", matches);
        model.addAttribute("bodyContent", "matches_page");
        return "master-template";
    }

    @PutMapping("/interested/{username}")
    public String addInterest(@PathVariable String username) {
        userService.interest(userService.getActiveUser().getUsername(), username, true);
        return reloadPage();
    }

    @DeleteMapping("/uninterested/{username}")
    public String removeInterest(@PathVariable String username) {
        userService.interest(userService.getActiveUser().getUsername(), username, false);
        return reloadPage();

    }

    @PutMapping("/like/{username}")
    public String addLiked(@PathVariable String username) {
        userService.like(userService.getActiveUser().getUsername(), username, true);
        return reloadPage();
    }

    @DeleteMapping("/unlike/{username}")
    public String removeLiked(@PathVariable String username) {
        userService.like(userService.getActiveUser().getUsername(), username, false);
        return reloadPage();

    }

    @DeleteMapping("/decline/{username}")
    public String deleteLikedBy(@PathVariable String username) {
        userService.like(username, userService.getActiveUser().getUsername(), false);
        return reloadPage();

    }

    @GetMapping("/access_denied")
    public String accessDeniedPage(Model model) {
        model.addAttribute("bodyContent", "access_denied");
        return "master-template";
    }
}
