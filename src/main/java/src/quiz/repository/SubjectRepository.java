package src.quiz.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import src.quiz.entity.Subject;

import java.util.List;
import java.util.Set;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Integer> {
    public Set<Subject> findByName(String name);

    @Query("SELECT s FROM Subject s WHERE s.name like %:key%")
    Page<Subject> findAll(String key, Pageable pageable);

    @Query("SELECT s FROM Subject s WHERE s.name like %:key%")
    List<Subject> findByKey(String key);
}
