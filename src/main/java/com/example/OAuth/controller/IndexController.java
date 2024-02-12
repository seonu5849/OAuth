package com.example.OAuth.controller;

import com.example.OAuth.annotation.LoginUser;
import com.example.OAuth.dto.SessionUser;
import com.example.OAuth.dto.posts.PostsListResponseDto;
import com.example.OAuth.dto.posts.PostsResponseDto;
import com.example.OAuth.service.PostsService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Controller
public class IndexController {

    private final PostsService postsService;

    @GetMapping("/")
    public String index(Model model, @LoginUser SessionUser user) {
        List<PostsListResponseDto> posts = this.postsService.findAllDesc();

        if(user != null){
            model.addAttribute("username", user.getName());
        }

        model.addAttribute("posts", posts);
        return "index";
    }

    @GetMapping("/posts/save")
    public String postsSave(Model model, @LoginUser SessionUser user){
        PostsResponseDto responseDto = new PostsResponseDto();

        if(user != null){
            responseDto.setAuthor(user.getName());
        }

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
