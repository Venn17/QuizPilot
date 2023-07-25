package src.quiz.dto;


import lombok.Builder;
import lombok.Data;
import src.quiz.entity.Role;

@Data
@Builder
public class UserDTO {
    private Integer id;
    private String name;
    private String password;
    private String roleStr;
    private Role role;
    private String token;
}
