package com.example.postingapp.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class PostControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithUserDetails("johnd@example.com")
    public void showPostingListPageIfLoggedIn() throws Exception {
        mockMvc.perform(get("/posts"))
               .andExpect(status().isOk())
               .andExpect(view().name("posts/index"));
    }

    @Test
    public void redirectToLoginPageIfNotLoggedIn() throws Exception {
        mockMvc.perform(get("/posts"))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrl("http://localhost/login"));
    }
    
    @Test
    @WithUserDetails("johnd@example.com")
    public void showPostDetailsPageIfLoggedIn() throws Exception {
        mockMvc.perform(get("/posts/1"))
        .andExpect(status().isOk())
        .andExpect(view().name("posts/details"));
    }
    
    @Test
    public void redirectFromPostDetailsPageToLoginPageIfNotLoggedIn() throws Exception {
        mockMvc.perform(get("/posts/1"))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrl("http://localhost/login"));
    }
}
