package src.quiz.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import src.quiz.entity.Course;
import src.quiz.entity.Level;
import src.quiz.entity.Question;

import java.util.Collection;
import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Integer> {

    public List<Question> findAllByTest(Integer test_id);

    public Long countByTextContainingAndLevelInAndCourseIn(String text, Collection<Level> level, Collection<Course> courses);

    public List<Question> findByTextContainingAndLevelInAndCourseIn(String text, Collection<Level> level, Collection<Course> courses, Pageable pageable);

    public Question getById(Integer question_id);
}
