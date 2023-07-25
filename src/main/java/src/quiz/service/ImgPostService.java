package src.quiz.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import src.quiz.entity.ImgPost;
import src.quiz.entity.Post;
import src.quiz.repository.ImgPostRepository;
import src.quiz.repository.PostRepository;

import java.util.List;

@Service
public class ImgPostService {
    @Autowired
    private ImgPostRepository imgPostRepository;

    @Autowired
    private PostRepository postRepository;

    public ImgPost insert(ImgPost i) {
        return imgPostRepository.save(i);
    }

    public List<ImgPost> getAll() {
        return imgPostRepository.findAll();
    }

    @Transactional
    public void removeFromIdPost(int id) {
        Post post = postRepository.findById(id).get();
        imgPostRepository.deleteImgPostByPost(post);
    }

    public void insertByFiles(MultipartFile files[], Post post) {
        for (int i = 0; i < files.length; i++) {
            String img_name = files[i].getOriginalFilename();
            String[] parts = img_name.split("\\.(?=[^\\.]+$)");
            String name_img = "post_" + post.getId() + "_" + i + "." + parts[1];
            ImgPost imgPost = new ImgPost(name_img, post);
            this.insert(imgPost);
        }
    }

    public List<ImgPost> getImgPostByPost(Post post) {
        return imgPostRepository.findByPost(post);
    }
}
