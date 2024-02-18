package com.example.demo.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import com.example.demo.model.Account;
import com.example.demo.model.Post;
import com.example.demo.services.AccountService;
import com.example.demo.services.PostService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class PostController {
    @Autowired
    private PostService pService;

    @Autowired
    private AccountService aService;

    @GetMapping("/posts/{id}")
    public String getPost(@PathVariable Long id, Model model) {
        Optional<Post> oPost = pService.findById(id);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("currentUsername", auth.getName());
        model.addAttribute("isAdmin", auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")));
        if (oPost.isPresent()) {
            Post post = oPost.get();
            model.addAttribute("post", post);
            return "post";
        } else {
            return "404";
        }

    }

    @GetMapping("/posts/new")
    public String getNewPostPage(Model model) {
        Optional<Account> oAccount = aService.findByEmail("user@domain.com");
        if (oAccount.isPresent()) {
            Account account = oAccount.get();
            Post post = new Post();
            post.setAccount(account);
            model.addAttribute("post", post);
            return "newpost";
        } else {
            return "404";
        }
    }

    @PostMapping("/posts/new")
    public String saveNewPost(@ModelAttribute Post post) {
        pService.save(post);

        return "redirect:/posts/" + post.getId();
    }

    @GetMapping("/posts/{id}/edit")
    @PreAuthorize("isAuthenticated()")
    public String getPostForEdit(@PathVariable Long id, Model model) {

        Optional<Post> optionalPost = pService.findById(id);

        if (optionalPost.isPresent()) {
            Post post = optionalPost.get();
            model.addAttribute("post", post);
            return "edit";
        } else {
            return "404";
        }
    }

    @PostMapping("/posts/{id}")
    @PreAuthorize("isAuthenticated()")
    public String updatePost(@PathVariable Long id, Post post) {

        Optional<Post> optionalPost = pService.findById(id);
        if (optionalPost.isPresent()) {
            Post existingPost = optionalPost.get();

            existingPost.setTitle(post.getTitle());
            existingPost.setBody(post.getBody());

            pService.save(existingPost);
        }

        return "redirect:/posts/" + post.getId();
    }

    @GetMapping("/posts/{id}/delete")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String deletePost(@PathVariable Long id) {

        Optional<Post> optionalPost = pService.findById(id);
        if (optionalPost.isPresent()) {
            Post post = optionalPost.get();

            pService.delete(post);
            return "redirect:/";
        } else {
            return "404";
        }
    }

}
