package src.quiz.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import src.quiz.entity.Course;
import src.quiz.entity.Level;
import src.quiz.entity.Question;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
public class TestDTO {

    private Integer id;

    @NotEmpty(message = "Field can't be empty!")
    private String text;

    private Integer duration;

    private Integer mark;

    @NotNull
    @JsonIgnore
    private Course course;

    @NotNull
    private Level level;

    @JsonIgnore
    private Collection<Question> question;
    @NotNull(message = "You must choose the question!")
    private List<Integer> selectedQuestion;
}
