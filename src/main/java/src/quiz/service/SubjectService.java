package src.quiz.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import src.quiz.dto.SubjectDTO;
import src.quiz.entity.Subject;
import src.quiz.repository.SubjectRepository;

import java.util.List;
import java.util.Set;

@Service
public class SubjectService {
    @Autowired
    SubjectRepository subjectRepository;

    public boolean exist(Subject subject) {
        Set<Subject> fromDB = subjectRepository.findByName(subject.getName());
        return fromDB.contains(subject);
    }

    public Subject save(Subject s) {
        return subjectRepository.save(s);
    }

    public List<Subject> getAll() {
        return subjectRepository.findAll();
    }

    public Subject createSubjectFromDTO(SubjectDTO subjectDTO) {
        Subject subject = new Subject();
        subject.setName(subjectDTO.getName());
        if (subjectDTO.getId() != null) {
            subject.setId(subjectDTO.getId());
        }
        return subject;
    }

    public Subject findById(int id) {
        return subjectRepository.findById(id).get();
    }

    @Transactional
    public void delete(int id) {
        subjectRepository.deleteById(id);
    }

    public boolean existNewName(Subject subject) {
        List<Subject> subjects = this.getAll();
        boolean flag = false;
        for (Subject item : subjects) {
            if (item.getName().equals(subject.getName())) {
                flag = true;
            }
        }
        return flag;
    }

    public boolean existUpdateName(Subject subject) {
        List<Subject> subjects = this.getAll();
        boolean flag = false;
        for (Subject item : subjects) {
            if (item.getName().equals(subject.getName()) && item.getId() != subject.getId()) {
                flag = true;
            }
        }
        return flag;
    }

    public SubjectDTO findDTOById(int id) {
        Subject subject = this.findById(id);
        SubjectDTO subjectDTO = new SubjectDTO();
        subjectDTO.setId(subject.getId());
        subjectDTO.setName(subject.getName());
        return subjectDTO;
    }

    public Page<Subject> findByPagination(String key, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return subjectRepository.findAll(key, pageable);
    }

    public List<Subject> listAllSubject() {
        return subjectRepository.findAll();
    }

    public List<Subject> findByKey(String key) {
        return subjectRepository.findByKey(key);
    }

}
