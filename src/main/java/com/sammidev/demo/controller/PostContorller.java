package com.sammidev.demo.controller;

import com.sammidev.demo.Entity.Post;
import com.sammidev.demo.repository.PostRepository;
import netscape.javascript.JSObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
public class PostContorller {

    @Autowired
    private PostRepository postRepository;

    @GetMapping("/posts")
    public Flux<Post> getAllTweets() {
        return postRepository.findAll();
    }

    @PostMapping("/post")
    public Mono<Post> createPost(@Valid @RequestBody Post post) {
        return postRepository.save(post);
    }

    @GetMapping("/posts/{id}")
    public Mono<ResponseEntity<Post>> getPostById(@PathVariable(value = "id") String postId) {
        return postRepository.findById(postId)
                .map(savedPost -> ResponseEntity.ok(savedPost))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PutMapping("/posts/{id}")
    public Mono<ResponseEntity<Post>> updatePost(@PathVariable(value = "id") String id, @Valid @RequestBody Post post) {
        return postRepository.findById(id)
                .flatMap(existingPost -> {
                    existingPost.setText(post.getText());
                    return postRepository.save(existingPost);
                })
                .map(updatedPost -> new ResponseEntity<>(updatedPost, HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/posts/{id}")
    public Mono<ResponseEntity<Void>> deletePost(@PathVariable(value = "id") String id) {
        return postRepository.findById(id)
                .flatMap(existingPost ->
                        postRepository.delete(existingPost)
                            .then(Mono.just(new ResponseEntity<Void>(HttpStatus.OK)))
                )
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping(value = "/stream/posts", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Post> streamlAllPost() {
        return postRepository.findAll();
    }

}