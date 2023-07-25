package src.quiz.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import src.quiz.entity.Post;
import src.quiz.entity.Subject;
import src.quiz.service.PostService;
import src.quiz.service.SubjectService;

import java.util.List;

@Controller
public class CourseUserController {
    @Autowired
    SubjectService subjectService;
    @Autowired
    PostService postService;

    @GetMapping("subjects")
    public String getAll(ModelMap model) {
        List<Subject> subjects = subjectService.getAll();
        model.addAttribute("subjectList", subjects);
        return "main/index";
    }

    @PostMapping("search")
    public String findByKeyBar(ModelMap model, @RequestParam(value = "keyBar", defaultValue = "") String keyBar) {
        List<Subject> subjects = subjectService.findByKey(keyBar);
        model.addAttribute("subjectList", subjects);
        return "main/ajaxSearchBar";
    }

    @GetMapping("course/{id}/post")
    public String getPostsByCourse(ModelMap model, @RequestParam(name = "key", defaultValue = "") String key, @PathVariable int id, @RequestParam(name = "page", defaultValue = "1") int page) {
        List<Subject> subjects = subjectService.getAll();
        model.addAttribute("subjectList", subjects);
        int size = 3;
        Page<Post> data = postService.pageByCourseId(id, key, page, size);
        List<Post> posts = data.getContent();
        model.addAttribute("posts", posts);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", data.getTotalPages());
        model.addAttribute("courseId", id);
        model.addAttribute("key", key);
        return "main/course";
    }

}
