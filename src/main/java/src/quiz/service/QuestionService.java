package src.quiz.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import src.quiz.dto.QuestionDTO;
import src.quiz.entity.Answer;
import src.quiz.entity.Course;
import src.quiz.entity.Level;
import src.quiz.entity.Question;
import src.quiz.repository.*;

import java.util.*;

@Service
public class QuestionService {
    @Autowired
    QuestionRepository questionRepo;
    @Autowired
    AnswerRepository answerRepo;
    @Autowired
    TestRepository testRepo;
    @Autowired
    CourseRepository courseRepo;
    @Autowired
    SubjectRepository subjectRepo;

    public List<Question> getAll() {
        return questionRepo.findAll();
    }

    public Question getOne(Integer id) {
        return questionRepo.findById(id).orElse(null);
    }

    public Long countAll(String search, String level, Integer subject, Integer course) {
        Long count;
        Collection<Level> levelCollection;
        Collection<Course> courseCollection = new ArrayList<>();
        if (level.isBlank()) {
            levelCollection = List.of(Level.values());
        } else {
            levelCollection = List.of(Level.valueOf(level));
        }
        if (course != null) {
            courseCollection.add(courseRepo.findById(course).get());
        } else {
            if (subject != null) {
                courseCollection = subjectRepo.findById(subject).orElse(null).getLstCourse();
            } else {
                courseCollection = courseRepo.findAll();
            }
        }
        count = questionRepo.countByTextContainingAndLevelInAndCourseIn(search, levelCollection, courseCollection);
        return count;
    }


    public List<Question> search(Integer page, Integer perPage, String search, String orderBy, String orderRule,
                                 String level, Integer subject, Integer course) {
        List<Question> questionResult;
        Sort sort;
        if (orderRule.equals("ASC")) {
            sort = Sort.by(orderBy).ascending();
        } else {
            sort = Sort.by(orderBy).descending();
        }
        Pageable pageableRule = PageRequest.of(page - 1, perPage, sort);
        Collection<Level> levelCollection;
        Collection<Course> courseCollection = new ArrayList<>();
        if (level.isBlank()) {
            levelCollection = List.of(Level.values());
        } else {
            levelCollection = List.of(Level.valueOf(level));
        }
        if (course != null) {
            courseCollection.add(courseRepo.findById(course).get());
        } else {
            if (subject != null) {
                courseCollection = subjectRepo.findById(subject).orElse(null).getLstCourse();
            } else {
                courseCollection = courseRepo.findAll();
            }
        }
        questionResult = questionRepo.findByTextContainingAndLevelInAndCourseIn(search, levelCollection, courseCollection, pageableRule);
        return questionResult;
    }

    public Map<String, Object> deleteOne(Integer id) {
        Question question = this.getOne(id);
        Map<String, Object> results = new HashMap<>();
        if (question.getTest().size() > 0) {
            results.put("testList", question.getTest());
            results.put("deleteStatus", 0);
        } else {
            questionRepo.deleteById(id);
            results.put("deleteStatus", 1);
        }
        return results;
    }

    public Answer getCorrectAnswer(Question question) {
        Answer answerResult = question.getAnswerList().stream().filter(
                (answer -> answer.isCorrect())
        ).findFirst().get();
        return answerResult;
    }

    public void saveOne(Question question) {
        questionRepo.save(question);
    }

    public QuestionDTO saveOne(QuestionDTO questionDTO) {
        Question question = toQuestion(questionDTO);
        questionRepo.save(question);
        questionDTO.setId(question.getId());
        return questionDTO;
    }

    public Question toQuestion(QuestionDTO questionDTO) {
        return Question.builder()
                .id(questionDTO.getId())
                .text(questionDTO.getText())
                .answerList(questionDTO.getAnswerList())
                .test(questionDTO.getTest())
                .level(questionDTO.getLevel())
                .course(questionDTO.getCourse())
                .build();
    }

    public QuestionDTO toQuestionDTO(Question question) {
        return QuestionDTO.builder()
                .id(question.getId())
                .text(question.getText())
                .answerList(question.getAnswerList())
                .test(question.getTest())
                .level(question.getLevel())
                .course(question.getCourse())
                .build();
    }
}
