package src.quiz.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import src.quiz.entity.*;
import src.quiz.jwt.JwtRequestFilter;
import src.quiz.jwt.JwtTokenUtil;
import src.quiz.service.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;

@Controller
public class HomeController {
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
    @Autowired
    private JwtTokenUtil jwtUtil;
    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @GetMapping(value = {"/home", "/"})
    public ModelAndView home(@RequestParam(name = "page", defaultValue = "1") int page,
                             @RequestParam(name = "sort", defaultValue = "id_ASC") String sort,
                             ModelMap modelMap, HttpServletRequest request, Principal principal) {
        String orderby = sort.split("[_]")[0];
        String orderRule = sort.split("[_]")[1];
        String name = principal.getName();
        User user = userService.findName(name);
        List<Post> postList = postService.pagination(Role.ADMINISTRATOR, page, orderby, orderRule);
        List<Subject> subjectList = subjectService.getAll();
        List<Course> courseList = courseService.getAll();
        modelMap.addAttribute("active", "post");
        modelMap.addAttribute("user", user);
        modelMap.addAttribute("postList", postList);
        modelMap.addAttribute("subjectList", subjectList);
        modelMap.addAttribute("courseList", courseList);
        ModelAndView modelAndView = new ModelAndView("main/index");
        return modelAndView;
    }

    @GetMapping(value = {"/search", "/home/search"})
    public ModelAndView search(@RequestParam(name = "search") String search, ModelMap modelMap, Principal principal) {
        String name = principal.getName();
        User user = userService.findName(name);
        List<Post> postList = postService.findTitle("%" + search + "%");
        List<Subject> subjectList = subjectService.getAll();
        List<Course> courseList = courseService.getAll();
        modelMap.addAttribute("user", user);
        modelMap.addAttribute("postList", postList);
        modelMap.addAttribute("subjectList", subjectList);
        modelMap.addAttribute("courseList", courseList);
        ModelAndView modelAndView = new ModelAndView("main/index");
        return modelAndView;
    }

    @GetMapping(value = {"/sort", "/home/sort"})
    public String soft_post(@RequestParam(name = "page", defaultValue = "1") int page,
                            @RequestParam(name = "sort") String sort, ModelMap modelMap, Principal principal) {
        String orderby = sort.split("[_]")[0];
        String orderRule = sort.split("[_]")[1];
        List<Post> postList = postService.pagination(Role.ADMINISTRATOR, page, orderby, orderRule);
        String name = principal.getName();
        User user = userService.findName(name);
        List<Subject> subjectList = subjectService.getAll();
        List<Course> courseList = courseService.getAll();
        modelMap.addAttribute("user", user);
        modelMap.addAttribute("postList", postList);
        modelMap.addAttribute("subjectList", subjectList);
        modelMap.addAttribute("courseList", courseList);
        return "main/ajaxSortPost";
    }

    @GetMapping("/403")
    public String error() {
        return "403";
    }

    @GetMapping("course/{id}")
    public String getCoursePost(@PathVariable("id") Integer id, ModelMap modelMap) {
        List<Subject> subjectList = subjectService.listAllSubject();
        Course course = courseService.findById(id);
        modelMap.addAttribute("subjectList", subjectList);
        modelMap.addAttribute("course", course);
        return "main/post_list";
    }

    @GetMapping("course/{id}/test")
    public String getCourseTest(@PathVariable("id") Integer id, ModelMap modelMap) {
        List<Subject> subjectList = subjectService.listAllSubject();
        Course course = courseService.findById(id);
        List<Test> testList = testService.findByCourseId(id);
        modelMap.addAttribute("subjectList", subjectList);
        modelMap.addAttribute("testList", testList);
        modelMap.addAttribute("course", course);
        return "main/test_list";
    }

    @GetMapping("play/{id}")
    public String letPlay(@PathVariable("id") Integer id, ModelMap modelMap, Principal principal) {
        String name = principal.getName();
        User user = userService.findName(name);
        List<Subject> subjectList = subjectService.listAllSubject();
        Test test = testService.getTest(id);
        modelMap.addAttribute("subjectList", subjectList);
        modelMap.addAttribute("test", test);
        modelMap.addAttribute("user", user);
        return "main/do_test_full";
    }
}
