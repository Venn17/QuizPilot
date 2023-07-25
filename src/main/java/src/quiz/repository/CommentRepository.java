package src.quiz.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import src.quiz.entity.Comment;
import src.quiz.entity.Post;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Integer> {

    List<Comment> findByPost(Post post);
}
