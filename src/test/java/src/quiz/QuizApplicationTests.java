package src.quiz;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import src.quiz.validator.CheckPassword;

@SpringBootTest
class QuizApplicationTests {
    @Autowired
    CheckPassword checkPasswordandName;

    @Test
    void contextLoads() {
        String message = checkPasswordandName.checkPassword("A111");
        System.out.println(message);
    }

}
