package src.quiz.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import src.quiz.dto.QuestionDTO;
import src.quiz.entity.Answer;
import src.quiz.entity.Question;
import src.quiz.repository.AnswerRepository;

@Service
public class AnswerService {
    @Autowired
    AnswerRepository answerRepo;
    @Autowired
    QuestionService questionService;

    public boolean saveAll(QuestionDTO questionDTO) {
        Question question = questionService.toQuestion(questionDTO);
        Boolean isCorrectAnswerHasValue = false;
        int index = 1;
        for (String answer_text : questionDTO.getAnswers()) {
            if (!answer_text.isBlank()) {
                Answer answer = new Answer();
                answer.setText(answer_text);
                answer.setQuestion(question);
                if (index == questionDTO.getCorrectAnswer()) {
                    answer.setCorrect(true);
                    isCorrectAnswerHasValue = true;
                }
                questionDTO.getAnswerList().add(answer);
            }
            index++;
        }
        if (isCorrectAnswerHasValue && question.getId() != null) {
            answerRepo.deleteAllByQuestionId(question.getId());
            for (Answer answer : questionDTO.getAnswerList()) {
                answerRepo.save(answer);
            }
        }
        return isCorrectAnswerHasValue;
    }
}
