package src.quiz.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import src.quiz.entity.Comment;
import src.quiz.entity.Post;
import src.quiz.repository.CommentRepository;

import java.util.List;

@Service

public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    public List<Comment> findbyPost(Post post) {
        return commentRepository.findByPost(post);
    }

    public Comment insert(Comment comment) {
        return commentRepository.save(comment);
    }
}
