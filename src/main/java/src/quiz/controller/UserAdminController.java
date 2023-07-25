package src.quiz.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import src.quiz.entity.User;
import src.quiz.jwt.JwtRequestFilter;
import src.quiz.jwt.JwtTokenUtil;
import src.quiz.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/admin/user")
public class UserAdminController {
    @Autowired
    private UserService userService;
    @Autowired
    private JwtTokenUtil jwtUtil;
    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @GetMapping(value = {"", "/", "/home"})
    public ModelAndView viewUser(ModelMap modelMap) {
        List<User> userList = userService.getAll();
        ModelAndView modelAndView = new ModelAndView("admin/user/userManage");
        modelMap.addAttribute("active", "user");
        modelMap.addAttribute("userList", userList);
        return modelAndView;
    }

    @GetMapping("/registration")
    public String registrationAdmin(Model model) {
        User user = new User();
        model.addAttribute(user);
        return "admin/registrationAdmin";
    }

    @PostMapping("/registration")
    public  String registrationAdmin(@ModelAttribute("user") User newAdmin, RedirectAttributes redirectAttributes,ModelMap model){
        if ( ! userService.usernameIsAvaible(newAdmin.getName()) ) {
            redirectAttributes.addFlashAttribute("user", newAdmin);
            redirectAttributes.addFlashAttribute("message", "The username isn't available.\nPlease pick another one.");
            return "redirect:/admin/registration";
        }
        if ( ! userService.emailIsAvaible(newAdmin.getEmail()) ) {
            redirectAttributes.addFlashAttribute("user", newAdmin);
            redirectAttributes.addFlashAttribute("emailerror", "The email is already registered.\nPlease pick another one.");
            return "redirect:/admin/registration";
        }
        try{
            userService.registerAdmin(newAdmin);
            redirectAttributes.addFlashAttribute("message", "You created a new account.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("user", newAdmin);
            redirectAttributes.addFlashAttribute("message", "There was an error while trying to register this account.");
            return "redirect:/admin/registration";
        }

        return "redirect:/admin/user";
    }

    @GetMapping(value = {"/update/{id}", "/home/update/{id}"})
    public ModelAndView viewUpdate(@PathVariable(name = "id") int id, ModelMap modelMap) {
        User user = userService.GetByID(id);
        modelMap.addAttribute("user", user);
        return new ModelAndView("admin/user/update");
    }

    @PostMapping(value = {"/update", "/home/update"})
    public String updateUserAdmin(@ModelAttribute(name = "user") User user, RedirectAttributes redirectAttributes) {
        boolean check_name = userService.checkExistName(user.getName());
        boolean check_email = userService.checkExistEmail(user.getEmail());
        if (check_name) {
            redirectAttributes.addFlashAttribute("msgErr", "Tên người dùng đá tồn tại !");
        } else if (check_email) {
            redirectAttributes.addFlashAttribute("msgErr", "Email đã tồn tại");
        } else {
            User is_Ok = userService.insert(user);
            if (is_Ok == null) {
                redirectAttributes.addFlashAttribute("msgErr", "Cập nhật người dùng thất bại !");
            } else {
                redirectAttributes.addFlashAttribute("msgOk", "Cập nhật thành công!");
            }
        }
        return "redirect:/admin/user";
    }

    @GetMapping(value = {"/delete/{id}", "/home/delete/{id}"})
    public String deleteUserAdmin(@PathVariable(name = "id") int id, RedirectAttributes redirectAttributes, HttpServletRequest request) {
        String jwt = jwtRequestFilter.parseJwt(request);
        String name = jwtUtil.getUserNameFromToken(jwt);
        User user = userService.findName(name);
        if (user.getId() == id) {
            redirectAttributes.addFlashAttribute("msgErr", "Không thể xóa tài khoản đang đăng nhập !");
        } else {
            userService.delete(id);
            redirectAttributes.addFlashAttribute("msgOk", "Đã xóa người dùng !");
        }
        return "redirect:/admin/user/";
    }

}
