package src.quiz.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import src.quiz.entity.Exam;

@Repository
public interface ExamRepository extends JpaRepository<Exam,Integer> {

}
