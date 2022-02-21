package mk.ukim.finki.datingapp.web;

import mk.ukim.finki.datingapp.models.Post;
import mk.ukim.finki.datingapp.models.User;
import mk.ukim.finki.datingapp.service.PostService;
import mk.ukim.finki.datingapp.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/timeline")
public class TimelineController {

    private final PostService postService;

    private final UserService userService;

    public TimelineController(PostService postService, UserService userService) {
        this.postService = postService;
        this.userService = userService;
    }

    @ModelAttribute
    void headerAttributes(Model model, HttpServletRequest request){
        User activeUser = userService.getActiveUser(request);

        model.addAttribute("activeUser", activeUser);
        model.addAttribute("fragments/header", "activeUser");
    }

    @GetMapping()
    public String getTimelinePage(Model model){
        List<Post> posts = postService.findAll();
        model.addAttribute("posts", posts);
        model.addAttribute("bodyContent", "timeline");
        return "master-template";
    }

    @DeleteMapping("/like/{id}")
    public String likePost(@PathVariable Long id, HttpServletRequest request){
        postService.likePost(id, true, request);
        return "redirect:/timeline";
    }

    @DeleteMapping("/unlike/{id}")
    public String unlikePost(@PathVariable Long id, HttpServletRequest request){
        postService.likePost(id, false, request);
        return "redirect:/timeline";
    }

    @GetMapping("/add")
    public String addPostForm(Model model){
        model.addAttribute("bodyContent","add_post_form");
        return "master-template";
    }

    @PostMapping("/add")
    public String addPost(@RequestParam String content, HttpServletRequest request){
        postService.addPost(content, request);
        return "redirect:/timeline";
    }
}
