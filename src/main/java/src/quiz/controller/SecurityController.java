package src.quiz.controller;


import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import src.quiz.entity.User;
import src.quiz.entity.UserPrincipal;
import src.quiz.exception.CustomerNotFoundException;
import src.quiz.jwt.JwtTokenUtil;
import src.quiz.security.Utility;
import src.quiz.service.SendEmailService;
import src.quiz.service.UserDetailsServiceImpl;
import src.quiz.service.UserService;
import src.quiz.validator.CheckPassword;

import javax.mail.MessagingException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;


@Controller
public class SecurityController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;

    @Autowired
    private JwtTokenUtil jwtUtil;


    @Autowired
    private SendEmailService sendEmail;

    @Autowired
    private CheckPassword checkPassword;


    @GetMapping("/login")
    public String login() {

        return "login";
    }

    @PostMapping("/signin")
    public String loginsuccess(@ModelAttribute(name = "user") User user, RedirectAttributes redirectAttributes, HttpServletResponse response) {
        UserPrincipal userPrincipal = (UserPrincipal) userDetailsServiceImpl.loadUserByUsername(user.getName());
        if (userPrincipal == null) {
            redirectAttributes.addFlashAttribute("error", "UserName or password is incorrect");
            return "redirect:/login";
        } else {
            if (!new BCryptPasswordEncoder().matches(user.getPassword(), userPrincipal.getPassword())) {
                redirectAttributes.addFlashAttribute("user", user);
                redirectAttributes.addFlashAttribute("error", "UserName or password is incorrect");
                return "redirect:/login";
            }
            if (userPrincipal.getEnabled().equals(false)) {
                redirectAttributes.addFlashAttribute("user", user);
                redirectAttributes.addFlashAttribute("error", "The account is not activated yet");
                return "redirect:/login";
            }
            String jwt = jwtUtil.generateToken(userPrincipal);
            response.addCookie(new Cookie("token", jwt));
            String name = jwtUtil.getUserNameFromToken(jwt);
            User userdemo = userService.findByName(name);
            String roleAdmin = String.valueOf(userdemo.getRole());
            if (roleAdmin.equals("ADMINISTRATOR")) {

                return "redirect:/admin/question/home";
            }

        }
        return "redirect:/home";

    }

    @GetMapping("/registration")
    public String registration(ModelMap model) {
        User user = new User();
        model.addAttribute(user);
        return "registration";
    }

    @PostMapping("/registration")
    public String registration(@Valid @ModelAttribute("user") User newUser, BindingResult bindingResult, HttpServletRequest request, RedirectAttributes redirectAttributes, ModelMap model) {
        if (bindingResult.hasErrors()) {
            return "registration";
        }
        if (!userService.usernameIsAvaible(newUser.getName())) {
            model.addAttribute("user", newUser);
            model.addAttribute("message", "The username isn't available.\nPlease pick another one.");
            return "registration";
        }

        if (!userService.emailIsAvaible(newUser.getEmail())) {
            model.addAttribute("user", newUser);
            model.addAttribute("emailerror", "The email is already registered.\nPlease pick another one.");
            return "registration";
        }

        if (!checkPassword.isValidPassword(newUser.getPassword()).equals("validPassword")) {
            String message = checkPassword.isValidPassword(newUser.getPassword());
            model.addAttribute("user", newUser);
            model.addAttribute("error", message);
            return "registration";
        }

        try {
            userService.register(newUser);
        } catch (Exception e) {
            model.addAttribute("user", newUser);
            model.addAttribute("message", "There was an error while trying to register this account.");
            return "registration";
        }
        String token = RandomString.make(30);
        String email = newUser.getEmail();

        try {
            userService.updateResetPasswordToken(token, email);
            String resetPasswordLink = Utility.getSiteURL(request) + "/registrationVerification?token=" + token;
            sendEmail.AccountVerification(email, resetPasswordLink);

        } catch (CustomerNotFoundException ex) {
            model.addAttribute("error", ex.getMessage());
        } catch (UnsupportedEncodingException | MessagingException e) {
            redirectAttributes.addFlashAttribute("error", "Your this  e-mail does not exist, please register with another email");
            userService.deleteUserByEmail(email);
            return "redirect:/registration";
        }

        redirectAttributes.addFlashAttribute("message", "We have sent an account activation link to your email. Please check.");
        return "redirect:/login";
    }

    //
    @RequestMapping("/registrationVerification")
    public String registrationVerification(@Param(value = "token") String token, RedirectAttributes redirectAttributes, Model model) {


        User customer = userService.getByResetPasswordToken(token);

        if (customer == null) {
            model.addAttribute("message", "account activated");
            return "login";
        } else {
            userService.activeAccount(customer);
            redirectAttributes.addFlashAttribute("message", "You have successfully activated your account");
        }
        return "redirect:/login";
    }

    @GetMapping("/admin/registration")
    public String registrationAdmin(Model model) {
        User user = new User();
        model.addAttribute(user);
        return "admin/registrationAdmin";
    }

    @PostMapping("/admin/registration")
    public String registrationAdmin(@ModelAttribute("user") User newAdmin, HttpServletRequest request, RedirectAttributes redirectAttributes, ModelMap model) {
        if (!userService.usernameIsAvaible(newAdmin.getName())) {
            redirectAttributes.addFlashAttribute("user", newAdmin);
            redirectAttributes.addFlashAttribute("message", "The username isn't available.\nPlease pick another one.");
            return "redirect:/admin/registration";
        }

        if (!userService.emailIsAvaible(newAdmin.getEmail())) {
            redirectAttributes.addFlashAttribute("user", newAdmin);
            redirectAttributes.addFlashAttribute("emailerror", "The email is already registered.\nPlease pick another one.");
            return "redirect:/admin/registration";
        }
        if (!checkPassword.isValidPassword(newAdmin.getPassword()).equals("validPassword")) {
            String message = checkPassword.isValidPassword(newAdmin.getPassword());
            model.addAttribute("user", newAdmin);
            model.addAttribute("error", message);
            return "admin/registration";
        }
        try {
            userService.registerAdmin(newAdmin);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("user", newAdmin);
            redirectAttributes.addFlashAttribute("message", "There was an error while trying to register this account.");
            return "redirect:/admin/registration";
        }
        String token = RandomString.make(30);
        String email = newAdmin.getEmail();

        try {
            userService.updateResetPasswordToken(token, email);
            String resetPasswordLink = Utility.getSiteURL(request) + "/admin/registrationVerification?token=" + token;
            sendEmail.AccountVerification(email, resetPasswordLink);

        } catch (CustomerNotFoundException ex) {
            model.addAttribute("error", ex.getMessage());
        } catch (UnsupportedEncodingException | MessagingException e) {
            redirectAttributes.addFlashAttribute("error", "Your this  e-mail does not exist, please register with another email");
        }

        redirectAttributes.addFlashAttribute("message", "We have sent an account activation link to your email. Please check.");
        return "redirect:/admin/registration";
    }

    @RequestMapping("/admin/registrationVerification")
    public String registrationAdVerification(@Param(value = "token") String token, RedirectAttributes redirectAttributes, Model model) {

        User admin = userService.getByResetPasswordToken(token);

        if (admin == null) {
            model.addAttribute("message", "account activated");
            return "login";
        } else {
            userService.activeAccount(admin);
            redirectAttributes.addFlashAttribute("message", "You have successfully activated your account.");
        }
        return "redirect:/admin/registration";
    }


    @GetMapping("/forgot_password")
    public String showForgotPasswordForm() {
        return "forgot_password_form";
    }

    @PostMapping("/forgot_password")
    public String processForgotPassword(HttpServletRequest request, Model model) {
        String email = request.getParameter("email");
        String token = RandomString.make(30);

        try {
            userService.updateResetPasswordToken(token, email);
            String resetPasswordLink = Utility.getSiteURL(request) + "/reset_password?token=" + token;
            sendEmail.sendEmailForgotPassword(email, resetPasswordLink);
            model.addAttribute("message", "We have sent a reset password link to your email. Please check.");
        } catch (CustomerNotFoundException ex) {
            model.addAttribute("error", ex.getMessage());
        } catch (UnsupportedEncodingException | MessagingException e) {
            model.addAttribute("error", "Error while sending email");
        }
        return "forgot_password_form";
    }



    @GetMapping("/reset_password")
    public String showResetPasswordForm(@Param(value = "token") String token, Model model) {
        User customer = userService.getByResetPasswordToken(token);
        model.addAttribute("token", token);
        if (customer == null) {
            model.addAttribute("message", "mầy thích lươn ko");
            return "403";
        }

        return "reset_password_form";
    }

    @PostMapping("/reset_password")
    public String processResetPassword(HttpServletRequest request, RedirectAttributes redirectAttributes, Model model) {
        String token = request.getParameter("token");
        String password = request.getParameter("password");
        String confirmpa = request.getParameter("confirmpass");

        if (!checkPassword.isValidPassword(password).equals("validPassword")) {
            String message = checkPassword.isValidPassword(password);
            model.addAttribute("error", message);
            return "redirect:/reset_password?token=" + token;
        }

        if (!password.equals(confirmpa)) {
            redirectAttributes.addFlashAttribute("error", "Password Incorrect");
            return "redirect:/reset_password?token=" + token;
        }

        User customer = userService.getByResetPasswordToken(token);
        model.addAttribute("title", "Reset your password");

        if (customer == null) {
            model.addAttribute("message", "Invalid Token");
            return "login";
        } else {
            userService.updatePassword(customer, password);

            model.addAttribute("message", "You have successfully changed your password.");
        }

        return "login";
    }


}
