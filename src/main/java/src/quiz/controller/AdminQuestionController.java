package src.quiz.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import src.quiz.dto.QuestionDTO;
import src.quiz.entity.Course;
import src.quiz.entity.Level;
import src.quiz.entity.Subject;
import src.quiz.service.AnswerService;
import src.quiz.service.CourseService;
import src.quiz.service.QuestionService;
import src.quiz.service.SubjectService;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/admin/question")
public class AdminQuestionController {
    @Autowired
    private QuestionService questionService;
    @Autowired
    private AnswerService answerService;
    @Autowired
    private SubjectService subjectService;
    @Autowired
    private CourseService courseService;

    @GetMapping({"/", "/home", ""})
    public String home(ModelMap modelMap) {
        List<Subject> subjectList = subjectService.listAllSubject();
        List<Course> courseList = courseService.listAllCourse();
        Level[] levelList = Level.values();
        modelMap.addAttribute("subjectList", subjectList);
        modelMap.addAttribute("courseList", courseList);
        modelMap.addAttribute("levelList", levelList);
        return "admin/question/home";
    }

    @GetMapping("/add")
    public String add(ModelMap modelMap) {
        Level[] levelList = Level.values();
        ;
        List<Subject> subjectList = subjectService.getAll();
        if (!modelMap.containsAttribute("question")) {
            modelMap.addAttribute("question", new QuestionDTO());
        }
        modelMap.addAttribute("subjectList", subjectList);
        modelMap.addAttribute("levelList", levelList);
        return "admin/question/add";
    }

    @PostMapping(value = "/add")
    public String postAdd(@Valid @ModelAttribute("question") QuestionDTO question, BindingResult result, ModelMap modelMap) {
        if (result.hasErrors()) {
            Level[] levelList = Level.values();
            ;
            List<Subject> subjectList = subjectService.getAll();
            modelMap.addAttribute("subjectList", subjectList);
            modelMap.addAttribute("levelList", levelList);
            return "admin/question/add";
        }
        questionService.saveOne(question);
        answerService.saveAll(question);
        return "redirect:/admin/question/";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") int id, ModelMap modelMap, RedirectAttributes redirectAttributes) {
        Level[] levelList = Level.values();
        ;
        List<Subject> subjectList = subjectService.getAll();
        QuestionDTO questionDTO = null;
        if (!modelMap.containsAttribute("question")) {
            questionDTO = questionService.toQuestionDTO(questionService.getOne(id));
        } else {
            questionDTO = (QuestionDTO) modelMap.getAttribute("question");
            questionDTO.setAnswerList(questionService.getOne(id).getAnswerList());
        }
        modelMap.addAttribute("subjectList", subjectList);
        modelMap.addAttribute("levelList", levelList);

        if (questionDTO != null) {
            modelMap.addAttribute("question", questionDTO);
            return "admin/question/edit";
        } else {
            redirectAttributes.addFlashAttribute("message", "Question does not exist");
            return "redirect:/admin/question/";
        }
    }

    @PostMapping("/update")
    public String update(@Valid @ModelAttribute("question") QuestionDTO questionDTO, BindingResult result,
                         RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.question", result);
            redirectAttributes.addFlashAttribute("question", questionDTO);
            return "redirect:/admin/question/edit/" + questionDTO.getId();
        }
        questionService.saveOne(questionDTO);
        Boolean isSaved = answerService.saveAll(questionDTO);
        if (!isSaved) {
            redirectAttributes.addFlashAttribute("message", "Invalid correct answer");
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.question", result);
            redirectAttributes.addFlashAttribute("question", questionDTO);
            return "redirect:/admin/question/edit/" + questionDTO.getId();
        }
        redirectAttributes.addFlashAttribute("message", "Update success");
        return "redirect:/admin/question/";
    }
}
