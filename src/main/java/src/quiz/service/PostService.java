package src.quiz.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import src.quiz.dto.PostRequest;
import src.quiz.entity.Course;
import src.quiz.entity.Post;
import src.quiz.entity.Role;
import src.quiz.repository.PostRepository;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
public class PostService {
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private CourseService courseService;
    @Autowired
    private UserService userService;


    public Post insert(Post post) {
        Date created_date = new Date();
        post.setCreatedDate(created_date);
        return postRepository.save(post);
    }

    public Post update(Post post) {
        Date update_date = new Date();
        post.setUpdatedDate(update_date);
        Post postData = this.findById(post.getId());
        post.setCreatedDate(postData.getCreatedDate());
        return postRepository.save(post);
    }

    public List<Post> getAll() {
        return postRepository.findAll();
    }

    public List<Post> pagination(Role role, int page, String orderBy, String orderRule) {
        Sort sort;

        if (orderRule.equals("ASC")) {
            sort = Sort.by(orderBy).ascending();
        } else {
            sort = Sort.by(orderBy).descending();
        }
        Pageable pageableRule = PageRequest.of(page - 1, 6, sort);
        return postRepository.findByUserRole(role, pageableRule);
    }

    public Post findById(int id) {
        return postRepository.findById(id).get();
    }

    public Post createPostFromDTO(PostRequest postRequest) {
        Post post = new Post();
        post.setId(postRequest.getId());
        post.setTitle(postRequest.getTitle());
        post.setContent(postRequest.getContent());
        if (postRequest.getCourseId() != null) {
            Course course = courseService.findById(postRequest.getCourseId());
            post.setCourse(course);
        }
        return post;
    }
    public void detelePostById(int idPost) {
        Post post = postRepository.findById(idPost).get();
        postRepository.delete(post);
    }

    public PostRequest clearPRData(PostRequest postRequest) {
        postRequest.setId(null);
        postRequest.setTitle(null);
        postRequest.setContent(null);
        postRequest.setCourseId(null);
        postRequest.setFiles(null);
        return postRequest;
    }

    public boolean exist(Post post) {
        Set<Post> fromDB = postRepository.findByTitle(post.getTitle());
        return fromDB.size() > 0;
    }

    public boolean existForUpdate(Post post) {
        List<Post> posts = this.getAll();
        boolean flag = false;
        for (Post item : posts) {
            if (item.getId() != post.getId() && item.getTitle().equals(post.getTitle())) {
                flag = true;
            }
        }
        return flag;
    }

    public Page<Post> pageByCourseId(int id, String key, int page, int size) {
        Course course = courseService.findById(id);
        Pageable pageable = PageRequest.of(page - 1, size);
        return postRepository.findAndPageByCourse(key, course, pageable);
    }

    public List<Post> findByCourseId(int id) {
        Course course = courseService.findById(id);
        return postRepository.findByCourse(course);
    }

    public Page<Post> findByPagination(String key, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return postRepository.findAll(key, pageable);
    }

    public List<Post> findAll() {
        return postRepository.findAll();
    }

    public List<Post> findTitle(String title) {
        return postRepository.findByTitleLike(title);
    }

    public boolean checkTitleExist(String title) {
        return postRepository.existsByTitle(title);
    }

}
