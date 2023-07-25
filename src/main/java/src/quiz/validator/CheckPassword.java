package src.quiz.validator;

import org.springframework.stereotype.Service;

@Service
public class CheckPassword {

    public String isValidPassword(String password) {
        int chu_thuong = 0;
        int chu_hoa = 0;
        int so = 0;
        int ky_tu_db = 0;
        if (password.length() < 8 || password.length() > 20) {
            return "password from 8 -> 20 characters";
        }
        for (int i = 0; i < password.length(); i++) {
            char ch = password.charAt(i);
            if (ch >= 48 && ch <= 57) {
                so++;
            } else {
                if (ch >= 65 && ch <= 90) {
                    chu_hoa++;
                } else {
                    if (ch >= 97 && ch <= 122) {
                        chu_thuong++;
                    } else {
                        ky_tu_db++;
                    }
                }

            }
        }
        if (chu_hoa < 1) {
            return "Password must have at least 1 uppercase letter";
        }
        if (chu_thuong < 1) {
            return "Password must be at least 1 lowercase letter";
        }
        if (so < 1) {
            return "Minimum 1 digit password";
        }
        if (ky_tu_db > 1) {
            return "Password does not contain special characters";
        }

        return "validPassword";
    }


}
