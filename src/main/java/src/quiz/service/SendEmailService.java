package src.quiz.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;

@Service
public class SendEmailService {
    @Autowired
    private JavaMailSender mailSender;

    public void sendEmailForgotPassword(String recipientEmail, String link) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setFrom("abcd@gmail.com", "Quiz Pilot");
        helper.setTo(recipientEmail);

        String subject = "Here's the  to reset your password";

        String content = "<p>Hello,</p>" + "<p>You have requested to reset your password.</p>"
                + "<p> below to change your password:</p>" + "<p><a href=" + "\"" + link + "\""
                + ">Change my password</a></p>" + "<br>" + "<p>Ignore this email if you do remember your password, "
                + "or you have not made the request.</p>" + "<b>(If this is a spam message, please click  it is not spam)<b>";
        helper.setSubject(subject);

        helper.setText(content, true);

        mailSender.send(message);
    }

    public void AccountVerification(String recipientEmail, String link) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setFrom("phihuukien08072002@gmail.com", "Quiz Pilot");
        helper.setTo(recipientEmail);
        String subject = "Here is the link to activate your account";

        String content = "<p>Hello,</p>" + "<p>You have successfully registered.</p>"
                + "<p>please click :</p>" + "<p><a href=" + "\"" + link + "\""
                + ">activate</a></p>" + "<br>" + "<p> to be able to use the account, "
                + "<b>(If this is a spam message, please click  it is not spam)<b>";
        helper.setSubject(subject);

        helper.setText(content, true);

        mailSender.send(message);
    }
}
