package com.example.OAuth.controller;

import com.example.OAuth.dto.posts.PostsListResponseDto;
import com.example.OAuth.dto.posts.PostsResponseDto;
import com.example.OAuth.service.PostsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class IndexController {

    private final PostsService postsService;

    @GetMapping("/")
    public String index(Model model) {
        List<PostsListResponseDto> posts = this.postsService.findAllDesc();
        model.addAttribute("posts", posts);

        return "index";
    }

    @GetMapping("/posts/save")
    public String postsSave(Model model){
        PostsResponseDto responseDto = new PostsResponseDto();
        model.addAttribute("post", responseDto);
        return "posts-save";
    }

    @GetMapping("/posts/update/{id}")
    public String postsUpdate(@PathVariable("id") Long id,  Model model) {
        PostsResponseDto responseDto = this.postsService.findById(id);
        model.addAttribute("post", responseDto);
        return "posts-update";
    }

}
