package src.quiz.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import src.quiz.entity.Course;
import src.quiz.entity.Post;
import src.quiz.entity.Role;

import java.util.List;
import java.util.Set;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {

    Post findPostById(Integer id);

    List<Post> findByTitleLike(String title);

    boolean existsByTitle(String title);

    public Set<Post> findByTitle(String title);

    @Query("SELECT p FROM Post p WHERE p.title like %:key% or p.user.name like %:key% ")
    Page<Post> findAll(String key, Pageable pageable);

    @Query("SELECT p FROM Post p WHERE (p.title like %:key% or p.user.name like %:key%) and p.course= :course")
    Page<Post> findAndPageByCourse(String key, Course course, Pageable pageable);

    List<Post> findByCourse(Course course);

    @Query("SELECT p FROM Post p where p.user.role like :role")
    List<Post> findByUserRole(@Param("role") Role role, Pageable pageable);
}
