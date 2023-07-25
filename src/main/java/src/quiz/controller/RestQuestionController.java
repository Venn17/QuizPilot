package src.quiz.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import src.quiz.entity.Question;
import src.quiz.service.QuestionService;
import src.quiz.service.UserService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/question")
public class RestQuestionController {
    @Autowired
    QuestionService questionService;
    @Autowired
    UserService userService;
    @Autowired
    ObjectMapper objectMapper;


    @GetMapping(value = "search")
    public @ResponseBody String search(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                       @RequestParam(value = "search", defaultValue = "") String search,
                                       @RequestParam(value = "level", defaultValue = "") String level,
                                       @RequestParam(value = "subject", defaultValue = "") Integer subject,
                                       @RequestParam(value = "course", defaultValue = "") Integer course,
                                       @RequestParam(value = "orderBy", defaultValue = "id") String orderBy,
                                       @RequestParam(value = "orderRule", defaultValue = "DESC") String orderRule,
                                       @RequestParam(value = "perPage", defaultValue = "10") Integer perPage)
            throws JsonProcessingException {
        List<Question> questions = questionService.search(page, perPage, search, orderBy, orderRule, level, subject, course);
        Long totalQuestion = questionService.countAll(search, level, subject, course);
        Map<String, Object> response = new HashMap<String, Object>();
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("questions", questions);
        result.put("totalQuestion", totalQuestion);
        response.put("results", result);
        return objectMapper.writeValueAsString(response);
    }

    @DeleteMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody String delete(@RequestBody String body) throws JsonProcessingException {
        JsonNode jsonNode = objectMapper.readTree(body);
        Map response = new HashMap();

        if (jsonNode != null) {
            Integer id = jsonNode.get("id").asInt();
            response.put("results", questionService.deleteOne(id));
        }
        return objectMapper.writeValueAsString(response);
    }
}
