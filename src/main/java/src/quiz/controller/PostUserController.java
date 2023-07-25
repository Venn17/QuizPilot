package src.quiz.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import src.quiz.entity.*;
import src.quiz.service.*;

import javax.validation.Valid;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/post/")
public class PostUserController {
    @Autowired
    PostService postService;
    @Value("${tinify.key}")
    String tinifyKey;
    @Autowired
    private ImgPostService imgPostService;
    @Autowired
    private MinioService minioService;
    @Autowired
    private SubjectService subjectService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private TestService testService;
    @Autowired
    private  UserService userService;
    @Autowired
    private CourseService courseService;
    @PostMapping("store")
    public String storePost(@ModelAttribute(name = "post") Post post, BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("msgErr", "Thông tin không khớp !");
        }
        if (postService.checkTitleExist(post.getTitle())) {
            redirectAttributes.addFlashAttribute("msgErr", "Bài đăng đã tồn tại !");
        }else {
            postService.insert(post);
            redirectAttributes.addFlashAttribute("msgOk", "Thêm bài thành công !");
        }
        return "redirect:/blog";
    }

    @GetMapping("detail/{id}")
    public ModelAndView viewDetail(@PathVariable(name = "id") int id, ModelMap modelMap, Principal principal) {
        String username = principal.getName();
        User user = userService.findName(username);
        modelMap.addAttribute("user",user);
        modelMap.addAttribute("username",username);
        Post post = postService.findById(id);
        List<Test> testList = testService.findByCourseId(post.getCourse().getId());
        List<Comment> commentList = commentService.findbyPost(post);
        List<ImgPost> imgPosts = imgPostService.getImgPostByPost(post);
        modelMap.addAttribute("imgPosts", imgPosts);
        modelMap.addAttribute("post", post);
        modelMap.addAttribute("testList", testList);
        modelMap.addAttribute("commentList", commentList);
        ModelAndView modelAndView = new ModelAndView("main/PostDetail");
        return modelAndView;
    }

    @GetMapping("update/{id}/{user_id}")
    public String viewupdate(@PathVariable(name = "id") int id, @PathVariable(name = "user_id") int user_id, ModelMap modelMap,RedirectAttributes redirectAttributes) {
        User user = userService.GetByID(user_id);
        Post post = postService.findById(id);
        List<Course> courseList = courseService.getAll();
        List<Subject> subjectList = subjectService.getAll();
        modelMap.addAttribute("user",user);
        modelMap.addAttribute("courseList",courseList);
        modelMap.addAttribute("subjectList",subjectList);
        if (post.getUser().getId() == user_id) {
            modelMap.addAttribute("post", post);
            return "main/PostUpdate";
        } else {
            redirectAttributes.addFlashAttribute("msgErr", "Bạn không phải người tạo bài viết !");
            ModelAndView modelAndView = new ModelAndView("main/PostDetail");
            return "redirect:/blog";
        }
    }

    @PostMapping("update")
    public String update(@ModelAttribute(name = "post") Post post1, BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("msgErr", "Thông tin nhập không hkớp !");
        } else {
            Post post = postService.insert(post1);
            if(post == null){
                redirectAttributes.addFlashAttribute("msgErr","Cập nhật lỗi !");
            }else {
                redirectAttributes.addFlashAttribute("msgOk", "Cập nhật thành công !");
            }
        }
        return "redirect:/home";
    }


    @PostMapping("/comment/add")
    public String addComment(@ModelAttribute(name = "comment") Comment comment,BindingResult result,RedirectAttributes redirectAttributes){
        if(result.hasErrors()){
            redirectAttributes.addFlashAttribute("msgErr","Mapping dữ liệu lỗi !");
        }else {
            commentService.insert(comment);
            List<Comment> commentList = commentService.findbyPost(comment.getPost());
            redirectAttributes.addFlashAttribute("commentList",commentList);
            redirectAttributes.addFlashAttribute("post",comment.getPost());
        }
        return "redirect:/post/detail/"+comment.getPost().getId();
    }
}

