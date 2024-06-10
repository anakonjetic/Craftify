package com.tvz.hr.craftify.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PasswordServiceTest {

    @Test
    void hashPassword_Success() {
        String plainPassword = "password123";

        String hashedPassword = PasswordService.hashPassword(plainPassword);

        assertNotNull(hashedPassword);
        assertNotEquals(plainPassword, hashedPassword);
    }

    @Test
    void isPasswordMatching_Success() {
        String plainPassword = "password123";
        String hashedPassword = PasswordService.hashPassword(plainPassword);

        assertTrue(PasswordService.isPasswordMatching(plainPassword, hashedPassword));
    }

    @Test
    void isPasswordMatching_Failure() {
        String plainPassword = "password123";
        String wrongPassword = "wrongpassword";
        String hashedPassword = PasswordService.hashPassword(plainPassword);

        assertFalse(PasswordService.isPasswordMatching(wrongPassword, hashedPassword));
    }

    @Test
    void isPasswordStrong_Success() {
        String strongPassword = "StrongPassword123";

        assertTrue(PasswordService.isPasswordStrong(strongPassword));
    }

    @Test
    void isPasswordStrong_Failure_Length() {
        String weakPassword = "weak";

        assertFalse(PasswordService.isPasswordStrong(weakPassword));
    }

    @Test
    void isPasswordStrong_Failure_UpperCase() {
        String weakPassword = "weakpassword123";

        assertFalse(PasswordService.isPasswordStrong(weakPassword));
    }

    @Test
    void isPasswordStrong_Failure_LowerCase() {
        String weakPassword = "WEAKPASSWORD123";

        assertFalse(PasswordService.isPasswordStrong(weakPassword));
    }

    @Test
    void isPasswordStrong_Failure_Digit() {
        String weakPassword = "WeakPassword";

        assertFalse(PasswordService.isPasswordStrong(weakPassword));
    }
}