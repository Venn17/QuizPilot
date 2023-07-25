package src.quiz.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import src.quiz.entity.Answer;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Integer> {

    @Transactional
    public void deleteAllByQuestionId(int id);

    public List<Answer> findAllByQuestion(Integer question_id);
}
