package src.quiz.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import src.quiz.repository.ExamRepository;

@Service
public class ExamService {
    @Autowired
    ExamRepository examRepo;

    public void deleteOneById(Integer id) {
        examRepo.deleteById(id);
    }
}
