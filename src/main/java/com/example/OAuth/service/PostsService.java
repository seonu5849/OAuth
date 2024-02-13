package com.example.OAuth.service;

import com.example.OAuth.dto.posts.PostsUpdateRequestDto;
import com.example.OAuth.dto.posts.PostsListResponseDto;
import com.example.OAuth.dto.posts.PostsResponseDto;
import com.example.OAuth.dto.posts.PostsSaveRequestDto;
import com.example.OAuth.entity.posts.Posts;
import com.example.OAuth.repository.PostsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class PostsService {

    private final PostsRepository postsRepository;

    @Transactional
    public Long save(PostsSaveRequestDto requestDto) {
        return this.postsRepository.save(requestDto.toEntity()).getId();
    }

    // Setter를 만들지 않았기 때문에 엔티티의 값을 변경해줄 메소드를 엔티티 클래스에 생성
    @Transactional
    public Long update(Long id, PostsUpdateRequestDto requestDto) {
        Posts posts = this.postsRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));
        posts.update(requestDto.getTitle(), requestDto.getContent());

        return id;
    }

    @Transactional
    public PostsResponseDto findById(Long id){
        Posts entity = this.postsRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));

        return new PostsResponseDto(entity);
    }

    @Transactional(readOnly = true)
    public List<PostsListResponseDto> findAllDesc() {
        return this.postsRepository.findAllDesc().stream()
                .map(PostsListResponseDto::new)
                .toList();

    }

    public void delete(Long id) {
        Posts posts = this.postsRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));
        this.postsRepository.delete(posts);
    }
}
