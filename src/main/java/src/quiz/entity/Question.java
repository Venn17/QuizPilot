package src.quiz.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.hibernate.annotations.Cascade;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "question")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_id")
    private Integer id;
    @Column(name = "question_text")
    private String text;
    @ManyToMany(mappedBy = "question")
    @JsonIgnore
    private Collection<Test> test;
    @OneToMany(mappedBy = "question", targetEntity = Answer.class)
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    @JsonManagedReference
    private List<Answer> answerList = new ArrayList<>();
    @Column(name = "level")
    @Enumerated(EnumType.STRING)
    private Level level;
    @ManyToOne
    @JoinColumn(name = "course_id", referencedColumnName = "course_id")
    @JsonManagedReference
    private Course course;


}
