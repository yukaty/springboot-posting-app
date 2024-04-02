package com.example.postingapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.postingapp.form.SignupForm;
import com.example.postingapp.service.UserService;

@Controller
public class AuthController {
    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }    
    
    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }
    
    @GetMapping("/signup")
    public String signup(Model model) {
        model.addAttribute("signupForm", new SignupForm());
        return "auth/signup";
    }    
    
    @PostMapping("/signup")
    public String signup(@ModelAttribute @Validated SignupForm signupForm, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        // If the email address is already registered, add an error message to the BindingResult object
        if (userService.isEmailRegistered(signupForm.getEmail())) {
            FieldError fieldError = new FieldError(bindingResult.getObjectName(), "email", "The email address is already registered.");
            bindingResult.addError(fieldError);
        }

        // If the input values of the password and password (confirmation) do not match, add an error message to the BindingResult object
        if (!userService.isSamePassword(signupForm.getPassword(), signupForm.getPasswordConfirmation())) {
            FieldError fieldError = new FieldError(bindingResult.getObjectName(), "password", "Passwords do not match.");
            bindingResult.addError(fieldError);
        }

        if (bindingResult.hasErrors()) {
            return "auth/signup";
        }

        // Register the user
        userService.create(signupForm);
        // Pass a success message to the redirected page
        redirectAttributes.addFlashAttribute("successMessage", "Signed up successfully!");
        
        return "redirect:/login";
    }   
}

