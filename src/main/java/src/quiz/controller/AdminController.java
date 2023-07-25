package src.quiz.controller;

import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@org.springframework.stereotype.Controller
public class AdminController {
    @GetMapping("/admin/home")
    public ModelAndView viewQuiz(ModelMap m) {
        ModelAndView a = new ModelAndView("admin/quiz/quizManage");
        m.addAttribute("active", "quiz");
        return a;
    }

    @GetMapping("/admin/user")
    public ModelAndView viewUser(ModelMap m) {
        ModelAndView a = new ModelAndView("admin/user/userManage");
        m.addAttribute("active", "user");
        return a;
    }
}
