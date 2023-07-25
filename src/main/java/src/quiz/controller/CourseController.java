package src.quiz.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import src.quiz.dto.CourseDTO;
import src.quiz.entity.Course;
import src.quiz.entity.Subject;
import src.quiz.service.CourseService;
import src.quiz.service.SubjectService;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("admin")
public class CourseController {
    @Autowired
    CourseService courseService;
    @Autowired
    SubjectService subjectService;

    @GetMapping("courses/new/{idSj}")
    public String insertCourseView(ModelMap model, @PathVariable("idSj") int idSj) {
        List<Subject> subjects = subjectService.getAll();
        model.addAttribute("subjects", subjects);
        Subject subjectNow = subjectService.findById(idSj);
        model.addAttribute("subjectNow", subjectNow);
        if (!model.containsAttribute("courseDTO")) {
            model.addAttribute("courseDTO", new CourseDTO());
        }
        return "admin/course/newCourse";
    }

    @PostMapping("courses/new")
    public String insertCourse(@Valid CourseDTO courseDTO, BindingResult bindingResult, ModelMap model, RedirectAttributes redirectAttrs) {
        Course course = courseService.createCourseByDTO(courseDTO);
        if (bindingResult.hasErrors() || courseService.existNewName(course)) {
            redirectAttrs.addFlashAttribute("courseDTO", courseDTO);
            if (courseService.existNewName(course)) {
                redirectAttrs.addFlashAttribute("uniqueName", "A name like this already exist");
            }
            redirectAttrs.addFlashAttribute("org.springframework.validation.BindingResult.courseDTO", bindingResult);
            return "redirect:/admin/courses/new/" + courseDTO.getSubject_id();
        }
        courseService.save(course);
        redirectAttrs.addFlashAttribute("notiMess", "Saved successfully!");
        return "redirect:/admin/subjects";
    }

    @GetMapping("courses/edit/{id}")
    public String showEditById(@PathVariable("id") int id, ModelMap model) {
        List<Subject> subjects = subjectService.getAll();
        model.addAttribute("subjects", subjects);
        if (!model.containsAttribute("courseDTO")) {
            model.addAttribute("courseDTO", courseService.findDTOById(id));
        }
        return "admin/course/editCourse";
    }

    @PostMapping("courses/edit")
    public String editById(@Valid CourseDTO courseDTO, BindingResult bindingResult, ModelMap model, RedirectAttributes redirectAttrs) {
        Course course = courseService.createCourseByDTO(courseDTO);
        if (bindingResult.hasErrors() || courseService.existUpdateName(course)) {
            redirectAttrs.addFlashAttribute("courseDTO", courseDTO);
            if (courseService.existUpdateName(course)) {
                redirectAttrs.addFlashAttribute("uniqueName", "A name like this already exist");
            }
            redirectAttrs.addFlashAttribute("org.springframework.validation.BindingResult.courseDTO", bindingResult);
            return "redirect:/admin/courses/edit/" + courseDTO.getId();
        }
        courseService.save(course);
        redirectAttrs.addFlashAttribute("notiMess", "Updated successfully!");
        return "redirect:/admin/subjects";
    }

    @GetMapping("courses/{id}")
    public String courseById(@PathVariable("id") int id, ModelMap model) {
        Course course = courseService.findById(id);
        model.addAttribute("course", course);
        return "admin/course/course";
    }

    @GetMapping("courses/delete/{id}")
    public String deleteById(@PathVariable("id") int id, RedirectAttributes redirectAttrs) {
        boolean checkDelete = courseService.checkForDeleteCourse(id);
        if (checkDelete) {
            courseService.deleteById(id);
            redirectAttrs.addFlashAttribute("notiMess", "Deleted successfully!");
        } else {
            redirectAttrs.addFlashAttribute("errDelete", "This course cannot be deleted because it contains data");
        }
        return "redirect:/admin/subjects";
    }
}
