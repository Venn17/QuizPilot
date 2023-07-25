package src.quiz.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import src.quiz.dto.TestDTO;
import src.quiz.entity.*;
import src.quiz.service.CourseService;
import src.quiz.service.QuestionService;
import src.quiz.service.SubjectService;
import src.quiz.service.TestService;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("admin/test")
public class TestController {
    @Autowired
    private TestService testService;
    @Autowired
    private CourseService courseService;
    @Autowired
    private SubjectService subjectService;
    @Autowired
    private QuestionService questionService;

    @GetMapping("")
    public String TestView(Model model) {
        List<Subject> subjectList = subjectService.getAll();
        List<Course> courses = courseService.getAll();
        List<String> levelList = new ArrayList<String>();
        for (int i = 0; i < Level.values().length; i++) {
            levelList.add(Level.values()[i].name());
        }
        model.addAttribute("courses", courses);
        model.addAttribute("levelList", levelList);
        model.addAttribute("subjectList", subjectList);

        return "admin/test/testManage";
    }


    @GetMapping("/new")
    public String showTestForm(Model model) {
        if (!model.containsAttribute("test")) {
            model.addAttribute("test", new TestDTO());
        }
        List<Course> courses = courseService.getAll();
        List<String> levelList = new ArrayList<String>();
        for (int i = 0; i < Level.values().length; i++) {
            levelList.add(Level.values()[i].name());
        }
        List<Subject> subjectList = subjectService.getAll();
        List<Question> questionList = questionService.getAll();
        model.addAttribute("courses", courses);
        model.addAttribute("levelList", levelList);
        model.addAttribute("subjectList", subjectList);
        model.addAttribute("questionList", questionList);
        return "admin/test/NewTest";
    }

    @PostMapping("/save")
    public String saveTest(@Valid @ModelAttribute("test") TestDTO testDTO, BindingResult result,
                           RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.test", result);
            redirectAttributes.addFlashAttribute("test", testDTO);
            return "redirect:/admin/test/new";
        } else {
            redirectAttributes.addFlashAttribute("message", "Added test successfully!");
            testDTO.setMark(testDTO.getSelectedQuestion().size());
            testService.saveTest(testDTO);
            return "redirect:/admin/test";
        }

    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Integer id, Model model) {
        Test test = null;
        if (!model.containsAttribute("test")) {
            test = testService.getTest(id);
        } else {
            test = (Test) model.getAttribute("test");
        }
        List<Course> courses = courseService.getAll();
        List<String> levelList = new ArrayList<String>();
        for (int i = 0; i < Level.values().length; i++) {
            levelList.add(Level.values()[i].name());
        }
        List<Subject> subjectList = subjectService.getAll();
        List<Question> questionList = questionService.getAll();
        model.addAttribute("testService", testService);
        model.addAttribute("test", test);
        model.addAttribute("questionList", questionList);
        model.addAttribute("courses", courses);
        model.addAttribute("levelList", levelList);
        model.addAttribute("subjectList", subjectList);

        return "admin/test/EditTest";
    }

    @PostMapping("/update")
    public String updateTest(@Valid @ModelAttribute("test") TestDTO testDTO, BindingResult result,
                             RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.test", result);
            redirectAttributes.addFlashAttribute("test", testDTO);
            return "redirect:/admin/test/edit/" + testDTO.getId();
        } else {
            redirectAttributes.addFlashAttribute("message", "Updated test successfully!");
            testDTO.setMark(testDTO.getSelectedQuestion().size());
            testService.saveTest(testDTO);
            return "redirect:/admin/test";
        }

    }

    @GetMapping("/delete/{id}")
    public String deleteTest(@PathVariable("id") Integer id, RedirectAttributes rd) {
        testService.deleteTest(id);
        rd.addFlashAttribute("message", "Deleted test successfully!");
        return "redirect:/admin/test";
    }
}
