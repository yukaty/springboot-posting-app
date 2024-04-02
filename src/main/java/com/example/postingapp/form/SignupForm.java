package com.example.postingapp.form;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SignupForm {
    
    @NotBlank(message = "Enter your name.")
    private String name;

    @NotBlank(message = "Enter email address.")
    @Email(message = "Enter valid email address.")
    private String email;

    @NotBlank(message = "Enter password.")
    @Length(min = 8, message = "Enter password with 8 characters or more.")
    private String password;

    @NotBlank(message = "Enter password again.")
    private String passwordConfirmation;
}

