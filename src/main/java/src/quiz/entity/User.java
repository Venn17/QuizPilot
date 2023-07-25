package src.quiz.entity;


import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import src.quiz.entity.converter.RoleConventer;

import javax.persistence.*;
import java.util.List;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer id;

    @Column(name = "user_name")
    @Length(min = 5, max = 20, message = "Name must be between 6 -> 20 characters")
    private String name;

    @Column(name = "password")
    private String password;

    @Column(name = "reset_password_token")
    private String resetPasswordToken;

    @Column(name = "user_type")
    private String userType;

    @Column(unique = true, name = "email")
    private String email;

    @Column(name = "email_fb_google")
    private String emailFacebookGoogle;

    @Column(name = "enabled")
    private Boolean enabled;
    @Convert(converter = RoleConventer.class)
    @Column(name = "role", nullable = false)
    private Role role;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private List<Exam> lstExam;

    @OneToMany(mappedBy = "userComment")
    private List<Comment> lstComment;

    public User(String name, String password, Boolean enabled, Role role, List<Exam> lstExam, List<Comment> lstcomment) {
        this.name = name;
        this.password = password;
        this.enabled = enabled;
        this.role = role;
        this.lstExam = lstExam;
        this.lstComment = lstcomment;
    }

}
