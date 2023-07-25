package src.quiz.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import src.quiz.entity.Course;
import src.quiz.entity.Test;

import java.util.List;

@Repository
public interface TestRepository extends JpaRepository<Test, Integer> {
    public List<Test> findByCourse(Course course);

    @Query("SELECT t FROM Test t WHERE t.text LIKE %:keyword% and t.course.name LIKE %:course% and t.course.subject.name LIKE %:subject%")
    Page<Test> findAllBy(@Param("keyword") String keyword, @Param("course") String course, @Param("subject") String subject, Pageable pageable);

    List<Test> findByCourseId(Integer id);
}
