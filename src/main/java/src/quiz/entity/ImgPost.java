package src.quiz.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "img_post")
@Getter
@Setter
@ToString
public class ImgPost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "img")
    private String img;
    @ManyToOne(targetEntity = Post.class, optional = false)
    @JoinColumn(name = "id_post", referencedColumnName = "post_id", nullable = false)
    private Post post;

    public ImgPost() {
    }

    public ImgPost(Integer id, String img, Post post_root) {
        this.id = id;
        this.img = img;
        this.post = post_root;
    }

    public ImgPost(String img, Post post_root) {
        this.img = img;
        this.post = post_root;
    }
}

