package src.quiz.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import src.quiz.entity.Answer;
import src.quiz.entity.Course;
import src.quiz.entity.Level;
import src.quiz.entity.Test;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionDTO {
    private Integer id;
    @NotBlank()
    private String text;
    @JsonIgnore
    private Collection<Test> test;
    @JsonManagedReference
    private List<Answer> answerList = new ArrayList<>();
    @Size(min = 1, message = "Must have an answer")
    @JsonIgnore
    private List<String> answers;
    @NotNull(message = "Must have a correct answer")
    private Integer correctAnswer;
    @NotNull
    @Enumerated(EnumType.STRING)
    private Level level;
    @NotNull
    private Course course;
}
