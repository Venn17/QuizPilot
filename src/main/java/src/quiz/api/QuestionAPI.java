package src.quiz.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import src.quiz.entity.Question;
import src.quiz.service.QuestionService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/question")
public class QuestionAPI {
    @Autowired
    QuestionService questionService;

    @Autowired
    ObjectMapper objectMapper;


    @PostMapping(value = "search", consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody String search(@RequestBody String body) throws JsonProcessingException {
        String questionJson = "";
        List<Question> quests = new ArrayList<Question>();
        JsonNode jsonNode = objectMapper.readTree(body);
        String searchValue = jsonNode.get("search").asText();
        quests = questionService.search(searchValue);
        questionJson = objectMapper.writeValueAsString(quests);
        return questionJson;
    }

    @PostMapping(value = "/delete", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody String delete(@RequestBody String body) throws JsonProcessingException {
        JsonNode jsonNode = null;
        jsonNode = objectMapper.readTree(body);
        if (jsonNode != null) {
            Integer id = jsonNode.get("id").asInt();
            Question question = questionService.getOne(id);
            if (question.getTest().size() > 0) {
                String listTestJson = objectMapper.writeValueAsString(question.getTest());
                return listTestJson;
            } else {
                questionService.deleteOne(id);
                return "delete success";
            }
        } else {
            return "request null";
        }
    }
}
