package src.quiz.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class CourseDTO {
    private Integer id;
    @NotEmpty(message = "The title must not be blank")
    private String name;
    private Integer subject_id;
}
