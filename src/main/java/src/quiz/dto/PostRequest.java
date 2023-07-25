package src.quiz.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;

@Data
public class PostRequest {
    private Integer id;
    @NotEmpty(message = "The title must not be blank")
    private String title;
    @NotEmpty(message = "The content must not be blank")
    private String content;
    private MultipartFile files[];
    private Integer courseId;

}
