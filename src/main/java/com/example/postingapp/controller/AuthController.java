package com.example.postingapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.postingapp.entity.User;
import com.example.postingapp.entity.VerificationToken;
import com.example.postingapp.event.SignupEventPublisher;
import com.example.postingapp.form.SignupForm;
import com.example.postingapp.service.UserService;
import com.example.postingapp.service.VerificationTokenService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class AuthController {
    private final UserService userService;
    private final SignupEventPublisher signupEventPublisher;
    private final VerificationTokenService verificationTokenService;


    public AuthController(UserService userService, SignupEventPublisher signupEventPublisher, VerificationTokenService verificationTokenService) {
        this.userService = userService;
        this.signupEventPublisher = signupEventPublisher;
        this.verificationTokenService = verificationTokenService;
    }

    /*
     * Display the login form
     */
    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }

    /*
     * Display the signup form
     */
    @GetMapping("/signup")
    public String signup(Model model) {
        model.addAttribute("signupForm", new SignupForm());
        return "auth/signup";
    }

    /*
     * Create a new user
     */
    @PostMapping("/signup")
    public String signup(@ModelAttribute @Validated SignupForm signupForm, BindingResult bindingResult, RedirectAttributes redirectAttributes, HttpServletRequest httpServletRequest) {
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

        // Create a new user
        User createdUser = userService.create(signupForm);
        // Publish a signup event
        String requestUrl = new String(httpServletRequest.getRequestURL());
        signupEventPublisher.publishSignupEvent(createdUser, requestUrl);
        // Add a success message to the redirected page
        redirectAttributes.addFlashAttribute("successMessage", "We have sent a confirmation email to you. Please click on the link in the email to complete your registration.");

        return "redirect:/login";
    }

    /*
     * Verify the token and enable the user
     */
    @GetMapping("/signup/verify")
    public String verify(@RequestParam(name = "token") String token, Model model) {
        VerificationToken verificationToken = verificationTokenService.getVerificationToken(token);

        if (verificationToken != null) {
            User user = verificationToken.getUser();
            userService.enableUser(user);
            String successMessage = "Signed up successfully! You can now login.";
            model.addAttribute("successMessage", successMessage);
        } else {
            String errorMessage = "The token is invalid.";
            model.addAttribute("errorMessage", errorMessage);
        }

        return "auth/verify";
    }
}

