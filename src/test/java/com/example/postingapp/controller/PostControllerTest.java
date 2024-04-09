package com.example.postingapp.controller;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.example.postingapp.entity.Post;
import com.example.postingapp.service.PostService;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class PostControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PostService postService;

    @Test
    @WithUserDetails("johnd@example.com")
    public void showPostListPageIfLoggedIn() throws Exception {
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

    @Test
    @WithUserDetails("johnd@example.com")
    public void showCreatePostPageIfLoggedIn() throws Exception {
        mockMvc.perform(get("/posts/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("posts/create"));
    }

    @Test
    public void redirectFromCreatePostPageToLoginPageIfNotLoggedIn() throws Exception {
        mockMvc.perform(get("/posts/create"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));
    }

    @Test
    @WithUserDetails("johnd@example.com")
    @Transactional
    public void redirectToPostListPageAfterCreatingPostWhenLoggedIn() throws Exception {
        mockMvc.perform(post("/posts/create").with(csrf()).param("title", "Post Test").param("content",
                "Finished the Java course!"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts"));

        Post post = postService.findFirstPostByOrderByIdDesc();
        assertThat(post.getTitle()).isEqualTo("Post Test");
        assertThat(post.getContent()).isEqualTo("Finished the Java course!");
    }

    @Test
    @Transactional
    public void redirectToLoginPageWithoutCreatingPostWhenNotLoggedIn() throws Exception {
        mockMvc.perform(post("/posts/create").with(csrf()).param("title", "Post Test").param("content",
                "Finished the Java course!"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));

        Post post = postService.findFirstPostByOrderByIdDesc();
        assertThat(post.getTitle()).isNotEqualTo("Post Test");
        assertThat(post.getContent()).isNotEqualTo("Finished the Java course!");
    }

    @Test
    @WithUserDetails("johnd@example.com")
    public void loggedInUserCanViewOwnPostEditPage() throws Exception {
        mockMvc.perform(get("/posts/1/edit"))
                .andExpect(status().isOk())
                .andExpect(view().name("posts/edit"));
    }

    @Test
    @WithUserDetails("janed@example.com")
    public void loggedInUserIsRedirectedFromOthersPostEditPageToListPage() throws Exception {
        mockMvc.perform(get("/posts/1/edit"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts"));
    }

    @Test
    public void notLoggedInUserIsRedirectedToLoginPageWhenAccessingPostEditPage() throws Exception {
        mockMvc.perform(get("/posts/1/edit"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));
    }

    @Test
    @WithUserDetails("johnd@example.com")
    @Transactional
    public void loggedInUserIsRedirectedToPostDetailPageAfterPostUpdate() throws Exception {
        mockMvc.perform(post("/posts/1").with(csrf()).param("title", "Test Title").param("content", "Test Content"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts/1"));

        Optional<Post> optionalPost = postService.findPostById(1);
        assertThat(optionalPost).isPresent();
        Post post = optionalPost.get();
        assertThat(post.getTitle()).isEqualTo("Test Title");
        assertThat(post.getContent()).isEqualTo("Test Content");
    }

    @Test
    @WithUserDetails("janed@example.com")
    @Transactional
    public void loggedInUserIsRedirectedToListPageWithoutUpdatingOthersPost() throws Exception {
        mockMvc.perform(post("/posts/1").with(csrf()).param("title", "Test Title").param("content", "Test Content"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts"));

        Optional<Post> optionalPost = postService.findPostById(1);
        assertThat(optionalPost).isPresent();
        Post post = optionalPost.get();
        assertThat(post.getTitle()).isNotEqualTo("Test Title");
        assertThat(post.getContent()).isNotEqualTo("Test Content");
    }

    @Test
    @Transactional
    public void notLoggedInUserIsRedirectedToLoginPageWithoutUpdatingPost() throws Exception {
        mockMvc.perform(post("/posts/1").with(csrf()).param("title", "Test Title").param("content", "Test Content"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));

        Optional<Post> optionalPost = postService.findPostById(1);
        assertThat(optionalPost).isPresent();
        Post post = optionalPost.get();
        assertThat(post.getTitle()).isNotEqualTo("Test Title");
        assertThat(post.getContent()).isNotEqualTo("Test Content");
    }

    @Test
    @WithUserDetails("johnd@example.com")
    @Transactional
    public void loggedInUserIsRedirectedToListPageAfterDeletingOwnPost() throws Exception {
        mockMvc.perform(post("/posts/1/delete").with(csrf()))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts"));

        Optional<Post> optionalPost = postService.findPostById(1);
        assertThat(optionalPost).isEmpty();
    }

    @Test
    @WithUserDetails("janed@example.com")
    @Transactional
    public void loggedInUserIsRedirectedToListPageWithoutDeletingOthersPost() throws Exception {
        mockMvc.perform(post("/posts/1/delete").with(csrf()))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts"));

        Optional<Post> optionalPost = postService.findPostById(1);
        assertThat(optionalPost).isPresent();
    }

    @Test
    @Transactional
    public void notLoggedInUserIsRedirectedToLoginPageWithoutDeletingPost() throws Exception {
        mockMvc.perform(post("/posts/1/delete").with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));

        Optional<Post> optionalPost = postService.findPostById(1);
        assertThat(optionalPost).isPresent();
    }
}
