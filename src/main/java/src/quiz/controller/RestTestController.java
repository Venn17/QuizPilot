package src.quiz.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import src.quiz.entity.Exam;
import src.quiz.entity.Test;
import src.quiz.entity.User;
import src.quiz.jwt.JwtTokenUtil;
import src.quiz.repository.ExamRepository;
import src.quiz.repository.UserRepository;
import src.quiz.service.CourseService;
import src.quiz.service.SubjectService;
import src.quiz.service.TestService;
import src.quiz.service.UserService;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/test")
public class RestTestController {
    @Autowired
    TestService testService;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    ExamRepository examRepo;
    @Autowired
    UserRepository userRepo;
    @Autowired
    UserService userService;
    @Autowired
    private JwtTokenUtil jwtUtil;
    @Autowired
    SubjectService subjectService;
    @Autowired
    CourseService courseService;

    @PostMapping("/submit")
    public String submitAnswer(@RequestBody String body) throws JsonProcessingException {
        JsonNode jsonNode = objectMapper.readTree(body);
        Integer testId = jsonNode.get("testId").asInt();
        JsonNode answerValue = jsonNode.get("answerValue");
        String token = jsonNode.get("token").asText();
        Test test = testService.getTest(testId);
        Map<String, Object> result = new HashMap<>();
        result = testService.checkResult(answerValue, test);

        if (!token.isBlank()) {
            String userName = jwtUtil.getUserNameFromToken(token);
            User user = userService.findName(userName);
            Exam exam = new Exam();
            exam.setTest(test);
            exam.setUser(user);
            exam.setDateTime(new Date());
            exam.setMark((int) result.get("score"));
            exam.setRankk(result.get("rank").toString());
            examRepo.save(exam);
        }
        Map<String, Object> results = new HashMap<String, Object>();
        results.put("results", result);
        return objectMapper.writeValueAsString(results);
    }

    @GetMapping("/filter")
    public String filterData(@RequestParam(value = "currentPage", defaultValue = "1") int currentPage,
                             @RequestParam(value = "sortField", defaultValue = "id") String sortField,
                             @RequestParam(value = "sortDir", defaultValue = "DESC") String sortDir,
                             @RequestParam(name = "keyword", defaultValue = "") String keyword,
                             @RequestParam(name = "subjectSearch", defaultValue = "") String subjectSearch,
                             @RequestParam(name = "courseSearch", defaultValue = "") String courseSearch) throws JsonProcessingException {

        Map<String, Object> result = new HashMap<>();
        Page<Test> page = testService.findPaginated(currentPage, sortField, sortDir, keyword, courseSearch, subjectSearch);

        long totalItems = page.getTotalElements();
        int totalPage = page.getTotalPages();

        List<Test> testList = page.getContent();

        result.put("testList", testList);
        result.put("totalItems", totalItems);
        result.put("totalPage", totalPage);

        return objectMapper.writeValueAsString(result);
    }
}
