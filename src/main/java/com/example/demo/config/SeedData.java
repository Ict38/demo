package com.example.demo.config;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.example.demo.model.Account;
import com.example.demo.model.Authority;
import com.example.demo.model.Post;
import com.example.demo.repository.AuthorityRepository;
import com.example.demo.services.AccountService;
import com.example.demo.services.PostService;

@Component
public class SeedData implements CommandLineRunner {

    @Autowired
    private PostService postService;

    @Autowired
    private AccountService accountService;

    @Autowired 
    private AuthorityRepository authorityRepository;

    @Override
    public void run(String... args) throws Exception {
        List<Post> posts = postService.getAll();
    
        if (posts.size() == 0) {

            Authority user = new Authority();
            user.setName("ROLE_USER");
            authorityRepository.save(user);

            Authority admin = new Authority();
            admin.setName("ROLE_ADMIN");
            authorityRepository.save(admin);

            Account account1 = new Account();
            account1.setEmail("user@domain.com");
            account1.setPassword("password");
            account1.setFirstName("John");
            account1.setLastName("Doe");
            Set<Authority> authorities = new HashSet<>();
            authorityRepository.findById("ROLE_USER").ifPresent(authorities::add);
            account1.setAuthorities(authorities);

            // create a new account2
            Account account2 = new Account();
            account2.setEmail("admin@domian.com");
            account2.setPassword("password");
            account2.setFirstName("Admin");
            account2.setLastName("Admin");   
            Set<Authority> authorities1 = new HashSet<>();
            authorityRepository.findById("ROLE_USER").ifPresent(authorities1::add);
            authorityRepository.findById("ROLE_ADMIN").ifPresent(authorities1::add);
            account2.setAuthorities(authorities1);

            accountService.save(account1);
            accountService.save(account2);
            
            Post post1 = new Post();
            post1.setTitle("HC38");
            post1.setBody("DAY LA HC38");
            post1.setAccount(account1);

            Post post2 = new Post();
            post2.setTitle("HC39");
            post2.setBody("DAY LA HC39");
            post2.setAccount(account2);

            postService.save(post1);
            postService.save(post2);
        }
    }

}
