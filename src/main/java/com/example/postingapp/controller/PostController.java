package com.example.postingapp.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.postingapp.entity.Post;
import com.example.postingapp.entity.User;
import com.example.postingapp.form.CreatePostForm;
import com.example.postingapp.form.EditPostForm;
import com.example.postingapp.security.UserDetailsImpl;
import com.example.postingapp.service.PostService;

@Controller
@RequestMapping("/posts")
public class PostController {
    private final PostService postService;

    public PostController(PostService postService) {
        
        this.postService = postService;
    }

    @GetMapping
    public String index(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl, Model model) {
        
        User user = userDetailsImpl.getUser();
        List<Post> posts = postService.findPostsByUserOrderedByCreatedAtDesc(user);

        // Pass the posts to the view
        model.addAttribute("posts", posts);

        return "posts/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable(name = "id") Integer id, RedirectAttributes redirectAttributes, Model model) {
        
        Optional<Post> optionalPost = postService.findPostById(id);

        if (optionalPost.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "The post does not exist.");

            return "redirect:/posts";
        }
        // Convert Optional<Post> to Post to get the field values
        Post post = optionalPost.get();
        // Pass the post to the view
        model.addAttribute("post", post);

        return "posts/details";
    }

    @GetMapping("/create")
    public String showCreatePostForm(Model model) {
        
        model.addAttribute("postCreateForm", new CreatePostForm());

        return "posts/create";
    }

    @PostMapping("/create")
    public String createPost(@ModelAttribute @Validated CreatePostForm postCreateForm,
            BindingResult bindingResult,
            @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
            RedirectAttributes redirectAttributes,
            Model model)
    {
        
        if (bindingResult.hasErrors()) {
            model.addAttribute("postCreateForm", postCreateForm);

            return "posts/create";
        }

        User user = userDetailsImpl.getUser();

        postService.createPost(postCreateForm, user);
        redirectAttributes.addFlashAttribute("successMessage", "The post has been created successfully.");
        return "redirect:/posts";
    }

    @GetMapping("/{id}/edit")
    public String showEditPostForm(@PathVariable(name = "id") Integer id,
                               @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
                               RedirectAttributes redirectAttributes,
                               Model model) {
        Optional<Post> optionalPost = postService.findPostById(id);
        User user = userDetailsImpl.getUser();

        if (optionalPost.isEmpty() || !optionalPost.get().getUser().equals(user)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Unauthorized access.");

            return "redirect:/posts";
        }

        Post post = optionalPost.get();
        model.addAttribute("post", post);
        model.addAttribute("editPostForm", new EditPostForm(post.getTitle(), post.getContent()));

        return "posts/edit";
    }
    
    @PostMapping("/{id}")
    public String updatePost(@ModelAttribute @Validated EditPostForm editPostForm,
                             BindingResult bindingResult,
                             @PathVariable(name = "id") Integer id,
                             @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
                             RedirectAttributes redirectAttributes,
                             Model model) {
        Optional<Post> optionalPost = postService.findPostById(id);
        User user = userDetailsImpl.getUser();

        if (optionalPost.isEmpty() || !optionalPost.get().getUser().equals(user)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Unauthorized access.");

            return "redirect:/posts";
        }

        if (bindingResult.hasErrors()) {
            Post post = optionalPost.get();
            model.addAttribute("post", post);
            model.addAttribute("editPostForm", editPostForm);

            return "posts/edit";
        }

        postService.updatePost(editPostForm, optionalPost.get());
        redirectAttributes.addFlashAttribute("successMessage", "The post has been updated.");

        return "redirect:/posts/" + id;
    }        

    @PostMapping("/{id}/delete")
    public String deletePost(@PathVariable(name = "id") Integer id,
                             @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
                             RedirectAttributes redirectAttributes,
                             Model model) {
        Optional<Post> optionalPost = postService.findPostById(id);
        User user = userDetailsImpl.getUser();

        if (optionalPost.isEmpty() || !optionalPost.get().getUser().equals(user)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Unauthorized access.");

            return "redirect:/posts";
        }

        postService.deletePost(optionalPost.get());
        redirectAttributes.addFlashAttribute("successMessage", "The post has been deleted.");

        return "redirect:/posts";
    }

}
