package src.quiz.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import src.quiz.dto.SubjectDTO;
import src.quiz.entity.Subject;
import src.quiz.service.CourseService;
import src.quiz.service.SubjectService;

import javax.validation.Valid;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Controller
@RequestMapping("admin")
public class SubjectController {
    @Autowired
    SubjectService subjectService;
    @Autowired
    CourseService courseService;

    @GetMapping("subjects")
    public String getAll(ModelMap model, @RequestParam(name = "page", defaultValue = "1") int page, @RequestParam(name = "key", defaultValue = "") String key) {
        int size = 10;
        Page<Subject> data = subjectService.findByPagination(key, page, size);
        List<Subject> subjects = data.getContent();
        model.addAttribute("currentPage", page);
        if (subjects.size() == 0) {
            model.addAttribute("notiMess", "Can not find the word:" + key);
        }
        key = URLEncoder.encode(key, StandardCharsets.UTF_8);
        model.addAttribute("key", key);
        model.addAttribute("totalPages", data.getTotalPages());
        model.addAttribute("subjects", subjects);
        return "admin/subject/subjects";
    }

    @GetMapping("subjects/new")
    public String insert_view(ModelMap model) {
        if (!model.containsAttribute("subjectDTO")) {
            model.addAttribute("subjectDTO", new SubjectDTO());
        }
        return "admin/subject/newSubject";
    }

    @PostMapping("subjects/new")
    public String insert_post(@Valid SubjectDTO subjectDTO, BindingResult bindingResult, ModelMap model, RedirectAttributes redirectAttrs) {
        Subject subject = subjectService.createSubjectFromDTO(subjectDTO);
        if (bindingResult.hasErrors() || subjectService.existNewName(subject)) {
            redirectAttrs.addFlashAttribute("subjectDTO", subjectDTO);
            if (subjectService.existNewName(subject)) {
                redirectAttrs.addFlashAttribute("uniqueName", "A name like this already exist");
            }
            redirectAttrs.addFlashAttribute("org.springframework.validation.BindingResult.subjectDTO", bindingResult);
            return "redirect:/admin/subjects/new";
        }
        subjectService.save(subject);
        redirectAttrs.addFlashAttribute("notiMess", "Saved successfully!");
        return "redirect:/admin/subjects";
    }

    @GetMapping("subjects/{id}")
    public String subjectById(@PathVariable("id") int id, ModelMap model) {
        Subject subject = subjectService.findById(id);
        model.addAttribute("subject", subject);
        return "admin/subject/subject";
    }

    @GetMapping("subjects/edit/{id}")
    public String showEditById(@PathVariable("id") int id, ModelMap model) {
        if (!model.containsAttribute("subjectDTO")) {
            model.addAttribute("subjectDTO", subjectService.findDTOById(id));
        }
        return "admin/subject/editSubject";
    }

    @PostMapping("subjects/edit")
    public String editById(@Valid SubjectDTO subjectDTO, BindingResult bindingResult, RedirectAttributes redirectAttrs) {
        Subject subject = subjectService.createSubjectFromDTO(subjectDTO);
        if (bindingResult.hasErrors() || subjectService.existUpdateName(subject)) {
            redirectAttrs.addFlashAttribute("subjectDTO", subjectDTO);
            if (subjectService.existUpdateName(subject)) {
                redirectAttrs.addFlashAttribute("uniqueName", "A name like this already exist");
            }
            redirectAttrs.addFlashAttribute("org.springframework.validation.BindingResult.subjectDTO", bindingResult);
            return "redirect:/admin/subjects/edit/" + subject.getId();
        }
        subjectService.save(subject);
        redirectAttrs.addFlashAttribute("notiMess", "Updated successfully!");
        return "redirect:/admin/subjects";
    }

    @GetMapping("subjects/delete/{id}")
    public String deleteById(@PathVariable("id") int id, RedirectAttributes redirectAttrs) {
        boolean checkDelete = courseService.checkForDeleteCourse(id);
        if (checkDelete) {
            courseService.deleteByIdSj(id);
            subjectService.delete(id);
            redirectAttrs.addFlashAttribute("notiMess", "Deleted successfully!");
        } else {
            redirectAttrs.addFlashAttribute("errDelete", "This subject cannot be deleted because it contains data");
        }
        return "redirect:/admin/subjects";
    }
}
