package com.example.postingapp.form;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreatePostForm {
    
    @NotBlank(message = "Please enter the title.")
    private String title;

    @NotBlank(message = "Please enter the content.")
    private String content;
}

