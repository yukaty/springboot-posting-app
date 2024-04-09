package com.example.postingapp.form;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor // Lombok annotation to create a constructor with all the fields in the class as arguments.
public class EditPostForm {
    
    @NotBlank(message = "Please enter a title.")
    private String title;

    @NotBlank(message = "Please enter the content.")
    private String content;
}
