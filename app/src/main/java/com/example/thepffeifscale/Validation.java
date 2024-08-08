package com.example.thepffeifscale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validation {
    static boolean isEmailValid(String email) {
        Pattern patternStudent = Pattern.compile("[a-z]+\\.[a-z]+@my\\.simpson\\.edu");
        Matcher matcherStudent = patternStudent.matcher(email);

        Pattern patternTeacher = Pattern.compile("[a-z]+\\.[a-z]+@simpson\\.edu");
        Matcher matcherTeacher = patternTeacher.matcher(email);

        return matcherStudent.find() || matcherTeacher.find();
    }

    static boolean isPasswordValid(String password) {
        Pattern pattern = Pattern.compile("........+");
        Matcher matcher = pattern.matcher(password);
        return matcher.find();
    }

    static boolean isUserNameValid(String username) {
        Pattern pattern = Pattern.compile("[Aa-zZ ]{5}[Aa-zZ ]+");
        Matcher matcher = pattern.matcher(username);
        return matcher.find();
    }
}
