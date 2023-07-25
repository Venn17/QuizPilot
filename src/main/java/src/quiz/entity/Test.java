package src.quiz.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.hibernate.annotations.Cascade;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "test")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Test {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "test_id")
    private Integer id;

    @Column(name = "test_text")
    @NotEmpty(message = "Field can't be empty!")
    private String text;

    @Column(name = "duration")
    private Integer duration;

    @Column(name = "total_mark")
    private Integer mark;

    @ManyToOne
    @JoinColumn(name = "course_id", referencedColumnName = "course_id")
    @NotNull
    @JsonManagedReference
    private Course course;

    @Enumerated(EnumType.STRING)
    @NotNull
    private Level level;

    @ManyToMany
    @JoinTable(name = "test_question", joinColumns = @JoinColumn(name = "test_id"), inverseJoinColumns = @JoinColumn(name = "question_id"))
    @JsonIgnore
    private Collection<Question> question = new ArrayList<>();

    @OneToMany(mappedBy = "test", targetEntity = Exam.class)
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    @JsonBackReference
    private List<Exam> examList;

}
