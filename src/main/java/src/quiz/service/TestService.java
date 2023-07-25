package src.quiz.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import src.quiz.dto.TestDTO;
import src.quiz.entity.Answer;
import src.quiz.entity.Course;
import src.quiz.entity.Question;
import src.quiz.entity.Test;
import src.quiz.repository.CourseRepository;
import src.quiz.repository.QuestionRepository;
import src.quiz.repository.TestRepository;

import java.util.*;

@Service
public class TestService {

    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private TestRepository testRepository;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private QuestionService questionService;

    public List<Test> listAllTest() {
        return testRepository.findAll();
    }


    public Test fromDTOtoTest(TestDTO testDTO) {
        return Test.builder()
                .id(testDTO.getId())
                .text(testDTO.getText())
                .duration(testDTO.getDuration())
                .mark(testDTO.getMark())
                .course(testDTO.getCourse())
                .level(testDTO.getLevel())
                .question(testDTO.getQuestion())
                .build();
    }

    public void saveTest(TestDTO testDTO) {
        Test test = this.fromDTOtoTest(testDTO);

        List<Question> questionList = new ArrayList<>();
        for (Integer questionId : testDTO.getSelectedQuestion()) {
            Question question = questionRepository.findById(questionId).orElse(null);
            questionList.add(question);
        }
        test.setQuestion(questionList);
        testRepository.save(test);
    }

    public Test getTest(Integer id) {
        Optional<Test> result = testRepository.findById(id);
        if (result.isPresent()) {
            return result.get();
        }
        return null;
    }

    public void deleteTest(Integer id) {
        testRepository.deleteById(id);
    }

    public List<Test> findByCourseId(int id) {
        Course course = courseRepository.findById(id);
        return testRepository.findByCourse(course);
    }


    public Page<Test> findPaginated(int pageNumber, String sortField, String sortDirection, String keyword, String courseName, String subjectName) {
        Sort sort = Sort.by(sortField);
        sort = sortDirection.equals("desc") ? sort.descending() : sort.ascending();
        Pageable pageable = PageRequest.of(pageNumber - 1, 2, sort);
        if (keyword != null) {
            if (courseName.isBlank()) {
                courseName = "%%";
            }
            if (subjectName.isBlank()) {
                subjectName = "%%";
            }
        }
        return testRepository.findAllBy(keyword, courseName, subjectName, pageable);
    }

    public boolean isTestHasThisQuestion(Test test, Question question) {
        Question questionFound = test.getQuestion().stream()
                .filter(
                        x -> x.getId().equals(question.getId())
                ).findFirst().orElse(null);
        return (questionFound != null);
    }

    public Map checkResult(JsonNode answerValue, Test test) {
        String rank = "";
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Integer>> resultList = new ArrayList<>();
        int score = 0;
        List<Question> questionList = test.getQuestion().stream().toList();
        for (JsonNode selectedAnswer : answerValue) {
            Integer questionId = selectedAnswer.get("questionId").asInt();
            Integer answerId = selectedAnswer.get("selectedAnswer").asInt();
            Question questionFromTest = questionList.stream()
                    .filter(question ->
                            question.getId().equals(questionId))
                    .findFirst().orElse(null);
            if (questionFromTest != null) {
                Map<String, Integer> correctAnswer = new HashMap<>();
                Answer answer = questionService.getCorrectAnswer(questionFromTest);
                correctAnswer.put("questionId", questionId);
                correctAnswer.put("selectedAnswer", answerId);
                correctAnswer.put("correctAnswer", answer.getId());
                resultList.add(correctAnswer);
                if (answerId.equals(answer.getId())) {
                    score++;
                }
            }
        }
        if (score > (questionList.size() / 2)) {
            rank = "PASS";
        } else {
            rank = "FAIL";
        }
        result.put("rank", rank);
        result.put("score", score);
        result.put("resultList", resultList);
        return result;
    }

}
