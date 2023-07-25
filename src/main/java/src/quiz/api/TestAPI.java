package src.quiz.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import src.quiz.entity.*;
import src.quiz.repository.ExamRepository;
import src.quiz.repository.UserRepository;
import src.quiz.service.TestService;

import java.util.*;

@RestController
@RequestMapping("api/test")
public class TestAPI {
    @Autowired
    TestService testService;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    ExamRepository examRepo;
    @Autowired
    UserRepository userRepo;

    @PostMapping("/submit")
    public String submitAnswer(@RequestBody String body) throws JsonProcessingException {
        JsonNode jsonNode = objectMapper.readTree(body);
        Integer testId = jsonNode.get("testId").asInt();
        Integer userId = jsonNode.get("user").asInt();
        JsonNode answerValue = jsonNode.get("answerValue");
        Test test = testService.getTest(testId);
        User user = userRepo.findById(userId).orElse(null);
        Exam exam = new Exam();
        exam.setTest(test);
        exam.setUser(user);
        exam.setDateTime(new Date());
        Integer score = 0;
        List<Integer> correctList = new ArrayList<>();
        for (JsonNode node : answerValue) {
            Integer questionId = node.get("questionId").asInt();
            Integer answerId = node.get("selectedAnswer").asInt();
            Question questionFromTest = test.getQuestion().stream().filter(question ->
                    question.getId().equals(questionId)
            ).findFirst().orElse(null);
            if (questionFromTest != null) {
                Answer answerFromQuestion = questionFromTest.getAnswerList().stream().filter(answer ->
                        answer.getId().equals(answerId)
                ).findFirst().orElse(null);
                if (answerFromQuestion != null && answerFromQuestion.isCorrect()) {
                    correctList.add(answerId);
                    score++;
                }
            }
        }
        exam.setMark(score);
        exam.setRankk("epic");
        examRepo.save(exam);
        Map<String, Object> results = new HashMap<String, Object>();
        Map<String, Object> content = new HashMap<String, Object>();
        content.put("score", score);
        content.put("correctList", correctList);
        results.put("results", content);
        return objectMapper.writeValueAsString(results);
    }
}
