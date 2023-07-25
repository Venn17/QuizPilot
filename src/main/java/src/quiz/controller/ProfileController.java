package src.quiz.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import src.quiz.entity.Exam;
import src.quiz.entity.Subject;
import src.quiz.entity.User;
import src.quiz.service.ExamService;
import src.quiz.service.SubjectService;
import src.quiz.service.UserService;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/me")
public class ProfileController {
    @Autowired
    private SubjectService subjectService;
    @Autowired
    private UserService userService;
    @Autowired
    private ExamService examService;

    public String myHome() {
        return "";
    }

    @RequestMapping("/history")
    public String myHistory(ModelMap modelMap, Principal principal) {
        String name = principal.getName();
        User user = userService.findName(name);
        List<Subject> subjectList = subjectService.listAllSubject();
        List<Exam> examList = user.getLstExam();
        modelMap.addAttribute("user", user);
        modelMap.addAttribute("subjectList", subjectList);
        modelMap.addAttribute("examList", examList);
        return "main/profile/history";
    }

    @RequestMapping("/history/delete/{id}")
    public String deleteHistory(@PathVariable("id") Integer id) {
        examService.deleteOneById(id);
        return "redirect:/me/history";
    }

    @GetMapping(value = {"/profile/{id}", "home/profile/{id}"})
    public ModelAndView profile(@PathVariable(name = "id") int id, ModelMap modelMap, Principal principal) {
        String name = principal.getName();
        User user = userService.findName(name);
        modelMap.addAttribute("user", user);
        ModelAndView modelAndView = new ModelAndView("main/profile_user");
        return modelAndView;
    }

    @GetMapping(value = {"/profile/update/{id}", "home/profile/update/{id}"})
    public ModelAndView viewUpdateProfile(@PathVariable(name = "id") int id, ModelMap modelMap) {
        User user = userService.GetByID(id);
        modelMap.addAttribute("user", user);
        ModelAndView modelAndView = new ModelAndView("main/profile_update");
        return modelAndView;
    }

    @PostMapping(value = {"/profile/update", "home/profile/update"})
    public ModelAndView Setprofile(@RequestParam(name = "id") int id,
                                   @RequestParam(name = "name") String name,
                                   @RequestParam(name = "email") String email, ModelMap modelMap) {
        User user = userService.GetByID(id);
        user.setName(name);
        user.setEmail(email);
        User user_check = userService.insert(user);
        if (user_check == null) {
            modelMap.addAttribute("msgErr", "Cập nhật thất bại !");
        } else {
            modelMap.addAttribute("msgOk", "Cập nhật thành công !");
        }
        modelMap.addAttribute("user", user);
        ModelAndView modelAndView = new ModelAndView("main/profile_user");
        return modelAndView;
    }

    @GetMapping(value = {"/profile/change_password/{id}", "home/profile/change_password/{id}"})
    public ModelAndView viewChangePassword(@PathVariable(name = "id") int id, ModelMap modelMap) {
        User user = userService.GetByID(id);
        modelMap.addAttribute("user", user);
        ModelAndView modelAndView = new ModelAndView("main/changePassword");
        return modelAndView;
    }

    @PostMapping(value = {"/profile/change_password", "home/profile/change_password"})
    public ModelAndView Setprofile(@RequestParam(name = "user_id") int id,
                                   @RequestParam(name = "old_pass") String oldPass,
                                   @RequestParam(name = "new_pass") String newPass,
                                   @RequestParam(name = "confirm_pass") String confirmPass, ModelMap modelMap) {
        User user = userService.GetByID(id);
        modelMap.addAttribute("user", user);
        boolean check_valid = new BCryptPasswordEncoder().matches(oldPass, user.getPassword());
        if (check_valid) {
            if (newPass.equals(confirmPass)) {
                user.setPassword(new BCryptPasswordEncoder().encode(newPass));
                userService.insert(user);
                modelMap.addAttribute("msgOk", "Cập nhật thành công !");
                return new ModelAndView("main/profile_user");
            } else {
                modelMap.addAttribute("msgErr", "Mật khẩu xác thực không khớp !");
                return new ModelAndView("main/changePassword");
            }
        } else {
            modelMap.addAttribute("msgErr", "Mật khẩu cũ không tồn tại !");
            return new ModelAndView("main/changePassword");
        }
    }
}
