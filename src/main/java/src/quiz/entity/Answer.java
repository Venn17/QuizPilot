package src.quiz.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "answer")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "answer_id")
    private Integer id;

    @Column(name = "answer_text")
    private String text;

    @Column(name = "sequence")
    private Integer sequence;

    @Column(name = "is_correct", columnDefinition = "boolean default false")
    private boolean isCorrect = false;

    @ManyToOne
    @JoinColumn(name = "question_id", referencedColumnName = "question_id")
    @JsonBackReference
    private Question question;

}
