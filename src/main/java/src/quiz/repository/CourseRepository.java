package src.quiz.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import src.quiz.entity.Course;
import src.quiz.entity.Subject;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Integer> {
    public void deleteBySubject(Subject subject);

    public Course findById(int id);

    List<Course> findBySubject(Subject subject);
}
