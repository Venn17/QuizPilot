package src.quiz.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class SubjectDTO {
    private Integer id;
    @NotEmpty(message = "The name must not be blank")
    private String name;
}
