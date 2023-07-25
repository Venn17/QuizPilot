package src.quiz.controller;

import io.minio.errors.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import src.quiz.dto.PostRequest;
import src.quiz.entity.*;
import src.quiz.jwt.JwtTokenUtil;
import src.quiz.service.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("admin")
public class PostController {
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
    private CourseService courseService;
    @Autowired
    private UserService userService;
    @Autowired
    JwtTokenUtil jwtUtil;

    @GetMapping("posts/new")
    public String insertView(@ModelAttribute PostRequest postRequest, ModelMap model, HttpServletRequest request, RedirectAttributes redirectAttrs) {
        List<Subject> subjects = subjectService.getAll();
        List<Course> courses = courseService.getAll();
        if (courses.size() > 0) {
            model.addAttribute("subjects", subjects);
            model.addAttribute("courses", courses);
            return "admin/post/newPost";
        }
        String referer = request.getHeader("Referer");
        redirectAttrs.addFlashAttribute("courseIsEmty", "Please create a course before creating a post");
        if (referer == null) {
            return "redirect:/admin/posts";
        }
        return "redirect:" + referer;
    }

    @PostMapping("posts/new")
    public String insertPost(@Valid PostRequest postRequest, Principal principal, BindingResult bindingResult, ModelMap model) throws IOException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        Post post = postService.createPostFromDTO(postRequest);
        String userName = principal.getName();
        User user = userService.findByName(userName);
        post.setUser(user);
        if (bindingResult.hasErrors() || postService.exist(post)) {
            List<Subject> subjects = subjectService.getAll();
            List<Course> courses = courseService.getAll();
            model.addAttribute("subjects", subjects);
            model.addAttribute("courses", courses);
            model.addAttribute("postRequest", postRequest);
            model.addAttribute("post", post);
            if (postService.exist(post)) {
                model.addAttribute("uniqueTitle", "A title like this already exist");
            }
            return "admin/post/ValidNewPost";
        }
        Post postInserted = postService.insert(post);
        if (postRequest.getFiles() != null) {
            minioService.uploadAndTinypng(postRequest.getFiles(), postInserted.getId());
            imgPostService.insertByFiles(postRequest.getFiles(), postInserted);
        }
        return "admin/post/test";
    }

    @GetMapping("post/{id}")
    public String postById(@PathVariable("id") int id, ModelMap model) {
        Post post = postService.findById(id);
        model.addAttribute("post", post);
        List<ImgPost> imgPosts = imgPostService.getImgPostByPost(post);
        model.addAttribute("imgPosts", imgPosts);
        return "admin/post/post";
    }

    @GetMapping("posts/edit/{id}")
    public String postShowEditById(@PathVariable("id") int id, ModelMap model) {
        Post post = postService.findById(id);
        List<Subject> subjects = subjectService.getAll();
        List<Course> courses = courseService.getAll();
        model.addAttribute("subjects", subjects);
        model.addAttribute("courses", courses);
        model.addAttribute("post", post);
        List<ImgPost> imgPosts = imgPostService.getImgPostByPost(post);
        model.addAttribute("imgPosts", imgPosts);
        return "admin/post/editPost";
    }

    @PostMapping("posts/edit")
    public String postEditById(@ModelAttribute @Valid PostRequest postRequest, Principal principal, BindingResult bindingResult, ModelMap model) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        Post post = postService.createPostFromDTO(postRequest);
        String userName = principal.getName();
        User user = userService.findByName(userName);
        post.setUser(user);
        if (bindingResult.hasErrors() || postService.existForUpdate(post)) {
            List<Subject> subjects = subjectService.getAll();
            List<Course> courses = courseService.getAll();
            model.addAttribute("subjects", subjects);
            model.addAttribute("courses", courses);
            model.addAttribute("postRequest", postRequest);
            model.addAttribute("post", post);
            if (postService.existForUpdate(post)) {
                model.addAttribute("uniqueTitle", "A title like this already exist");
            }
            return "admin/post/ValidNewPost";
        }
        postService.update(post);
        List<ImgPost> imgPosts = imgPostService.getImgPostByPost(post);
        for (ImgPost imgpost : imgPosts) {
            minioService.removeFile(imgpost.getImg());
        }
        imgPostService.removeFromIdPost(post.getId());
        if (postRequest.getFiles() != null) {
            minioService.uploadAndTinypng(postRequest.getFiles(), post.getId());
            imgPostService.insertByFiles(postRequest.getFiles(), post);
        }
        return "admin/post/test";
    }

    @GetMapping("posts/delete/{id}")
    public String postDeleteById(@PathVariable("id") int idPost, ModelMap model, RedirectAttributes redirectAttrs) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        Post post = postService.findById(idPost);
        List<ImgPost> imgPosts = imgPostService.getImgPostByPost(post);
        for (ImgPost imgpost : imgPosts) {
            minioService.removeFile(imgpost.getImg());
        }
        imgPostService.removeFromIdPost(idPost);
        postService.detelePostById(idPost);
        redirectAttrs.addFlashAttribute("notiMess", "Deleted successfully!");
        return "redirect:/admin/posts";
    }

    @GetMapping("posts")
    public String posts(ModelMap model, @RequestParam(name = "page", defaultValue = "1") int page, @RequestParam(name = "key", defaultValue = "") String key) {
        int size = 10;
        Page<Post> data = postService.findByPagination(key, page, size);
        List<Post> posts = data.getContent();
        if (posts.size() == 0) {
            model.addAttribute("notiMess", "Can not find the word:" + key);
        }
        key = URLEncoder.encode(key, StandardCharsets.UTF_8);
        model.addAttribute("key", key);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", data.getTotalPages());
        model.addAttribute("posts", posts);
        return "admin/post/posts";
    }

    @GetMapping("posts/test")
    public String test() {
        return "post/testPost";
    }
}
