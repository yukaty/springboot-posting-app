package com.example.postingapp.event;

import org.springframework.context.ApplicationEvent;

import com.example.postingapp.entity.User;

import lombok.Getter;

/*
 * Custom event class that will be triggered when a user signs up.
 */
@Getter
public class SignupEvent extends ApplicationEvent {
    private User user;
    private String requestUrl;

    public SignupEvent(Object source, User user, String requestUrl) {
        super(source);

        this.user = user;
        this.requestUrl = requestUrl;
    }
}

