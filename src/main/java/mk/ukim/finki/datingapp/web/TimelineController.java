package mk.ukim.finki.datingapp.web;

import mk.ukim.finki.datingapp.models.Post;
import mk.ukim.finki.datingapp.models.User;
import mk.ukim.finki.datingapp.models.exceptions.InvalidPostException;
import mk.ukim.finki.datingapp.models.exceptions.UserNotFoundException;
import mk.ukim.finki.datingapp.service.PostService;
import mk.ukim.finki.datingapp.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping({"/timeline", "/", "home"})
public class TimelineController {

    private final PostService postService;

    private final UserService userService;

    private final HttpServletRequest request;

    public TimelineController(PostService postService, UserService userService, HttpServletRequest request) {
        this.postService = postService;
        this.userService = userService;
        this.request = request;
    }

    private String reloadPage() {
        String referrer = request.getHeader("referer").substring(21);
        return "redirect:" + referrer;
    }

    @ModelAttribute("activeUser")
    public User activeUser() {
        return userService.getActiveUser();
    }

    @GetMapping()
    public String getTimelinePage(@RequestParam(required = false) String keyword,
                                  @RequestParam(required = false) String sort,
                                  Model model) {
        List<Post> posts = postService.findAll();
        if (keyword != null && !keyword.isEmpty())
            posts = postService.filterPosts(keyword);
        if(sort != null && !sort.isEmpty())
            posts = postService.sortPosts(posts,sort);
        else
            posts = postService.sortPosts(posts, "ascDate");

        model.addAttribute("posts", posts);
        model.addAttribute("keyword", keyword);
        model.addAttribute("sort", sort);
        model.addAttribute("bodyContent", "timeline");
        return "master-template";
    }

    @GetMapping("/profile/{username}")
    public String getProfilePage(@PathVariable String username, Model model) {
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));

//        List<Post> posts = user.getPosts();
//        if (keyword != null && !keyword.isEmpty())
//            posts = postService.filterPosts(keyword);
//        if(sort != null && sort.equals("desc"))
//            posts = postService.sortPosts(posts,true);
//        else
//            posts = postService.sortPosts(posts,false);
//        model.addAttribute("keyword", keyword);
//        model.addAttribute("sort", sort);

        model.addAttribute("posts", user.getPosts());
        model.addAttribute("user", user);
        model.addAttribute("bodyContent", "profile_timeline");
        return "master-template";
    }

    @PutMapping("/like/{id}")
    public String likePost(@PathVariable Long id) {
        postService.likePost(id, true);
        return reloadPage();

    }

    @DeleteMapping("/unlike/{id}")
    public String unlikePost(@PathVariable Long id) {
        postService.likePost(id, false);
        return reloadPage();

    }

    @GetMapping("/add")
    public String addPostForm(@RequestParam(required = false) String error, Model model) {
        if (error != null && !error.isEmpty()) {
            model.addAttribute("hasError", true);
            model.addAttribute("error", error);
        }
        model.addAttribute("bodyContent", "add_post_form");
        return "master-template";
    }

    @PostMapping("/add")
    public String addPost(@RequestParam String content) {
        try {
            postService.addPost(content);
        } catch (InvalidPostException e) {
            return "redirect:/add?error=" + e.getMessage();
        }
        return "redirect:/timeline";
    }

    @DeleteMapping("/delete/{id}")
    public String deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return reloadPage();
    }
}
