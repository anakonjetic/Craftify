package com.tvz.hr.craftify.service;

//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class PasswordService {
    /*static BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public static String hashPassword(String plainPassword) {
        return passwordEncoder.encode(plainPassword);
    }
    public static boolean isPasswordMatching(String loginPassword, String dbPassword){
        return passwordEncoder.matches(loginPassword, dbPassword);
    }*/
    public static String hashPassword(String text) {
        String hashedPassword = text;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(text.getBytes(StandardCharsets.UTF_8));
            hashedPassword = Base64.getEncoder().encodeToString(hash);
        }catch(NoSuchAlgorithmException e) {
            System.out.println("Something is wrong");
        }
        return hashedPassword;
    }

    public static boolean isPasswordMatching(String loginPassword, String dbPassword){
        return hashPassword(loginPassword).equals(dbPassword);
    }
    public static boolean isPasswordStrong(String password) {
        if (password.length() < 8) {
            return false;
        }
        if (!password.matches(".*[A-Z].*")) {
            return false;
        }
        if (!password.matches(".*[a-z].*")) {
            return false;
        }
        if (!password.matches(".*\\d.*")) {
            return false;
        }
        return true;
    }
}
