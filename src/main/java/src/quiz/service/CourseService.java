package src.quiz.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import src.quiz.dto.CourseDTO;
import src.quiz.entity.Course;
import src.quiz.entity.Post;
import src.quiz.entity.Subject;
import src.quiz.entity.Test;
import src.quiz.repository.CourseRepository;
import src.quiz.repository.PostRepository;

import java.util.List;

@Service
public class CourseService {
    @Autowired
    CourseRepository courseRepository;
    @Autowired
    SubjectService subjectService;
    @Autowired
    PostRepository postRepository;
    @Autowired
    TestService testService;

    public Course createCourseByDTO(CourseDTO courseDTO) {
        Course course = new Course();
        course.setId(courseDTO.getId());
        course.setName(courseDTO.getName());
        Subject subject = subjectService.findById(courseDTO.getSubject_id());
        course.setSubject(subject);
        return course;
    }

    public Course save(Course s) {
        return courseRepository.save(s);
    }

    public Course findById(int id) {
        return courseRepository.findById(id);
    }

    @Transactional
    public void deleteById(int id) {
        courseRepository.deleteById(id);
    }

    @Transactional
    public void deleteByIdSj(int idSj) {
        Subject subject = subjectService.findById(idSj);
        courseRepository.deleteBySubject(subject);
    }


    public boolean checkForDeleteCourse(int id) {
        boolean flag = true;
        Course course = this.findById(id);
        List<Post> posts = postRepository.findByCourse(course);
        List<Test> tests = testService.findByCourseId(id);
        if (posts.size() > 0 || tests.size() > 0) {
            flag = false;
        }
        return flag;
    }

    public boolean existNewName(Course course) {
        List<Course> courses = this.getAll();
        boolean flag = false;
        for (Course item : courses) {
            if (item.getName().equals(course.getName())) {
                flag = true;
            }
        }
        return flag;
    }


    public List<Course> getAll() {
        return courseRepository.findAll();

    }

    public List<Course> listAllCourse() {
        return courseRepository.findAll();
    }


    public boolean existUpdateName(Course course) {
        List<Course> courses = this.getAll();
        boolean flag = false;
        for (Course item : courses) {
            if (item.getName().equals(course.getName()) && item.getId() != course.getId()) {
                flag = true;
            }
        }
        return flag;
    }

    public CourseDTO findDTOById(int id) {
        Course course = this.findById(id);
        CourseDTO courseDTO = new CourseDTO();
        courseDTO.setId(course.getId());
        courseDTO.setName(course.getName());
        courseDTO.setSubject_id(course.getSubject().getId());
        return courseDTO;
    }
}