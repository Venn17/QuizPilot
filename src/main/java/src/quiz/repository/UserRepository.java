package src.quiz.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import src.quiz.entity.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    User getUserById(Integer id);

    User findByName(String username);

    Optional<User> findById(Integer id);

    Boolean existsUserByName(String name);

    Boolean existsUserByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.email = ?1")
    User findByEmail(String email);

    User findByNameAndUserType(String name, String userType);

    User findByEmailFacebookGoogleAndUserType(String email, String userType);

    @Query("SELECT u FROM User u WHERE u.resetPasswordToken = ?1")
    User findByResetPasswordToken(String resetPasswordTokenoken);

    Boolean deleteByEmail(String email);


}
