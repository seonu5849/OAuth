package com.example.OAuth.dto;

import com.example.OAuth.entity.User;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class SessionUser implements Serializable { // DTO와 같은 역할
    /**
     * SessionUser는 인증된 사용자 정보만 세션에 저장하기 위한 클래스
     * 세션에 저장하기 위해 User 엔티티 클래스를 직접 사용하게 되면 직렬화를 해야 하는데
     * 엔티티 클래스에 직렬화를 넣어주면 추후에 다른 엔티티와 연관관계를 맺을시
     * 직렬화 대상에 다른 엔티티까지 포함될 수 있어 성능 이슈 우려가 있기 때문에
     * 세션 저장용 DTO 클래스를 생성하는 것이다.
     */

    private String name;
    private String email;
    private String picture;

    public SessionUser(User user) {
        this.name = user.getName();
        this.email = user.getEmail();
        this.picture = user.getPicture();
    }
}
