package com.example.OAuth.dto.posts;

import com.example.OAuth.entity.posts.Posts;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
public class PostsResponseDto {

    private Long id;
    private String title;
    private String content;
    @Setter
    private String author;

    public PostsResponseDto(Posts entity) {
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.content = entity.getContent();
        this.author = entity.getAuthor();
    }
}
