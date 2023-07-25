package src.quiz.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import src.quiz.entity.ImgPost;
import src.quiz.entity.Post;

import java.util.List;

public interface ImgPostRepository extends JpaRepository<ImgPost, Integer> {
//    @Modifying
//    @Query("DELETE FROM ImgPost WHERE post_root = ?1")

    //    @Modifying
//    @Query(value = "DELETE FROM ImgPost i WHERE i.post_root.id = ?1")
    public void deleteImgPostByPost(Post post);

    public List<ImgPost> findByPost(Post post);


}
