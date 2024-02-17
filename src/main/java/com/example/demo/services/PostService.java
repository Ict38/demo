package com.example.demo.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Post;
import com.example.demo.repository.PostRepository;

@Service
public class PostService {
    
    @Autowired
    private PostRepository postRepository;

    public Optional<Post> findById(Long id) {
        return postRepository.findById(id);
    }

    public List<Post> getAll(){
        return postRepository.findAll();
    }

    public Post save(Post post){
        post.setUpdatedAt(LocalDateTime.now());
        if(post.getId() == null){
            post.setCreateAt(LocalDateTime.now());
    }
    return postRepository.save(post);
    }

    public void delete(Post p){
        postRepository.deleteById(p.getId());
    }

}
