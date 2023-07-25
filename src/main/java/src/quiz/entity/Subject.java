package src.quiz.entity;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.List;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "subject")
@Data
@JsonIgnoreProperties(value = {"lstCourse"})
public class Subject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subject_id")
    private int id;

    @Column(name = "subject_name", unique = true)
    private String name;

    @OneToMany(mappedBy = "subject", fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<Course> lstCourse;

    public Subject() {
    }

    public Subject(String name) {
        this.name = name;
    }
}
