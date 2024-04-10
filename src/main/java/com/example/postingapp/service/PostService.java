package com.example.postingapp.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.postingapp.entity.Post;
import com.example.postingapp.entity.User;
import com.example.postingapp.form.CreatePostForm;
import com.example.postingapp.form.EditPostForm;
import com.example.postingapp.repository.PostRepository;

@Service
public class PostService {
    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    // Get a list of posts with a specific user in order of newest to oldest
    public List<Post> findPostsByUserOrderedByCreatedAtDesc(User user) {
        return postRepository.findByUserOrderByCreatedAtDesc(user);
    }
    
    // Get a list of posts with a specific user in order of oldest to newest
    public List<Post> findPostsByUserOrderedByCreatedAtAsc(User user) {
        return postRepository.findByUserOrderByCreatedAtAsc(user);
    }
    
    // Get a post by id
    public Optional<Post> findPostById(Integer id) {
        return postRepository.findById(id);
    }
    
    // Get the latest post
    public Post findFirstPostByOrderByIdDesc() {
        return postRepository.findFirstByOrderByIdDesc();
    }    

    // Create a post
    @Transactional
    public void createPost(CreatePostForm createPostForm, User user) {
        
        Post post = new Post();

        post.setTitle(createPostForm.getTitle());
        post.setContent(createPostForm.getContent());
        post.setUser(user);

        postRepository.save(post);
    }
    
    // Update a post
    @Transactional
    public void updatePost(EditPostForm editPostForm, Post post) {
        
        post.setTitle(editPostForm.getTitle());
        post.setContent(editPostForm.getContent());

        postRepository.save(post);
    }
    
    // Delete a post
    @Transactional
    public void deletePost(Post post) {
        postRepository.delete(post);
    }

}
