package com.example.postingapp.form;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor // Lombok annotation to create a constructor with all the fields in the class as arguments.
public class EditPostForm {
    
    @NotBlank(message = "Please enter a title.")
    @Length(max = 40, message = "The title must be within 40 characters.")
    private String title;

    @NotBlank(message = "Please enter the content.")
    @Length(max = 200, message = "The content must be within 200 characters.")
    private String content;
}