package src.quiz.service;

import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import src.quiz.entity.Role;
import src.quiz.entity.User;
import src.quiz.exception.CustomerNotFoundException;
import src.quiz.jwt.JwtTokenUtil;
import src.quiz.repository.UserRepository;
import src.quiz.security.oauth.CustomOAuth2User;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;
    @Autowired
    private JwtTokenUtil jwtUtil;
    @Autowired
    private UserRepository userRepo;

    public User findByName(String name) {
        return userRepo.findByName(name);
    }

    public boolean usernameIsAvaible(String username) {
        return (!userRepo.existsUserByName(username));
    }

    public boolean checkExistName(String username) {
        return userRepo.existsUserByName(username);
    }

    public boolean checkExistEmail(String email) {

        return userRepo.existsUserByEmail(email);
    }

    public User findName(String name) {
        return userRepo.findByName(name);
    }

    public User GetByID(Integer id) {
        return userRepo.getUserById(id);
    }

    public User findByID(Integer id) {
        return userRepo.findById(id).orElse(null);
    }

    public User insert(User user) {
        return userRepo.save(user);
    }

    public void delete(Integer id) {
        User user = userRepo.getUserById(id);
        userRepo.delete(user);
    }

    public List<User> getAll() {
        return userRepo.findAll();
    }

    public User register(User user) {
        user.setRole(Role.USER);
        user.setEnabled(false);
        user.setUserType("database");
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        return userRepo.save(user);
    }

    public boolean emailIsAvaible(String email) {
        return (!userRepo.existsUserByEmail(email));
    }

    public User registerAdmin(User admin) {
        admin.setEnabled(false);
        admin.setUserType("database");
        admin.setPassword(new BCryptPasswordEncoder().encode(admin.getPassword()));
        return userRepo.save(admin);
    }


    public void AuthenticationUserFacebookAndGoogle(CustomOAuth2User oauth2User, HttpServletResponse response) {
        String name = oauth2User.getName();
        String email = oauth2User.getEmail();
        String oauth2ClientName = oauth2User.getOauth2ClientName();
        User existUserDatabase = userRepo.findByNameAndUserType(name, "database");
        User existUserFacebookGoogle = userRepo.findByEmailFacebookGoogleAndUserType(email, oauth2ClientName);
        if ((existUserFacebookGoogle == null) && (existUserDatabase == null)) {
            User newUser = new User();
            newUser.setName(name);
            newUser.setEmailFacebookGoogle(email);
            newUser.setRole(Role.USER);
            newUser.setUserType(oauth2ClientName);
            userRepo.save(newUser);
        }
        if ((existUserDatabase != null) && (existUserFacebookGoogle == null)) {
            String random = RandomString.make(5);
            User newUser = new User();
            newUser.setName(name + "_" + random);
            newUser.setEmailFacebookGoogle(email);
            newUser.setRole(Role.USER);
            newUser.setUserType(oauth2ClientName);
            userRepo.save(newUser);
        }


    }

    public void updateResetPasswordToken(String token, String email) throws CustomerNotFoundException {
        User customer = userRepo.findByEmail(email);
        if (customer != null) {
            customer.setResetPasswordToken(token);
            userRepo.save(customer);
        } else {
            throw new CustomerNotFoundException("Could not find any customer with the email: " + email);
        }
    }

    public User getByResetPasswordToken(String token) {
        User user = userRepo.findByResetPasswordToken(token);
        return user;

    }

    public void updatePassword(User customer, String newPassword) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(newPassword);
        customer.setPassword(encodedPassword);

        customer.setResetPasswordToken(null);
        userRepo.save(customer);
    }

    public Boolean deleteUserByEmail(String email) {
        return userRepo.deleteByEmail(email);
    }

    public void activeAccount(User customer) {
        customer.setResetPasswordToken(null);
        customer.setEnabled(true);
        userRepo.save(customer);
    }

}
