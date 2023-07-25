package src.quiz.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import src.quiz.entity.*;
import src.quiz.service.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;

@Controller
public class BlogController {
    @Autowired
    private UserService userService;
    @Autowired
    private PostService postService;
    @Autowired
    private SubjectService subjectService;
    @Autowired
    private CourseService courseService;
    @Autowired
    private TestService testService;

    @GetMapping("/blog")
    public ModelAndView blog(@RequestParam(name = "page", defaultValue = "1") int page,
                             @RequestParam(name = "sort", defaultValue = "id_ASC") String sort,
                             ModelMap modelMap, HttpServletRequest request, Principal principal) {
        String orderby = sort.split("[_]")[0];
        String orderRule = sort.split("[_]")[1];
        String name = principal.getName();
        User user = userService.findName(name);
        List<Post> postList = postService.pagination(Role.USER, page, orderby, orderRule);
        List<Subject> subjectList = subjectService.getAll();
        List<Course> courseList = courseService.getAll();
        modelMap.addAttribute("active", "blog");
        modelMap.addAttribute("user", user);
        modelMap.addAttribute("postList", postList);
        modelMap.addAttribute("subjectList", subjectList);
        modelMap.addAttribute("courseList", courseList);
        modelMap.addAttribute("currentPage", page);
        ModelAndView modelAndView = new ModelAndView("main/blog");
        return modelAndView;
    }

    @GetMapping("/blog/search")
    public ModelAndView search(@RequestParam(name = "search") String search, ModelMap modelMap, Principal principal) {
        List<Post> postList = postService.findTitle("%" + search + "%");
        String name = principal.getName();
        User user = userService.findName(name);
        List<Subject> subjectList = subjectService.getAll();
        List<Course> courseList = courseService.getAll();
        modelMap.addAttribute("user", user);
        modelMap.addAttribute("postList", postList);
        modelMap.addAttribute("subjectList", subjectList);
        modelMap.addAttribute("courseList", courseList);
        ModelAndView modelAndView = new ModelAndView("main/blog");
        return modelAndView;
    }

    @GetMapping("/blog/sort")
    public String sortPost(@RequestParam(name = "page", defaultValue = "1") int page,
                           @RequestParam(name = "sort") String sort, ModelMap modelMap, Principal principal) {
        String orderby = sort.split("[_]")[0];
        String orderRule = sort.split("[_]")[1];
        List<Post> postList = postService.pagination(Role.USER, page, orderby, orderRule);
        String name = principal.getName();
        User user = userService.findName(name);
        List<Subject> subjectList = subjectService.getAll();
        List<Course> courseList = courseService.getAll();
        modelMap.addAttribute("user", user);
        modelMap.addAttribute("postList", postList);
        modelMap.addAttribute("subjectList", subjectList);
        modelMap.addAttribute("courseList", courseList);
        return "main/ajaxSortBlog";
    }

    @GetMapping("/blog/course/{id}")
    public String getCoursePost(@PathVariable("id") Integer id, ModelMap modelMap, Principal principal) {
        String name = principal.getName();
        User user = userService.findName(name);
        List<Subject> subjectList = subjectService.getAll();
        List<Course> courseList = courseService.getAll();
        List<Post> postList = postService.findByCourseId(id);
        modelMap.addAttribute("user", user);
        modelMap.addAttribute("postList", postList);
        modelMap.addAttribute("subjectList", subjectList);
        modelMap.addAttribute("courseList", courseList);
        modelMap.addAttribute("totalPages", 2);
        modelMap.addAttribute("currentPage", 1);
        return "main/blog";
    }
}
