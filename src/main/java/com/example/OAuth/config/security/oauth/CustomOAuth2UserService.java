package com.example.OAuth.config.security.oauth;

import com.example.OAuth.dto.OAuthAttributes;
import com.example.OAuth.dto.SessionUser;
import com.example.OAuth.entity.User;
import com.example.OAuth.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    // 구글 로그인 후 가져온 사용자의 정보(email, name, picture 등)들을 기반으로 가입 및 정보수정, 세션 저장 등의 기능을 지원

    private final UserRepository userRepository;
    private final HttpSession httpSession;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2UserService<OAuth2UserRequest,OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        // OAuth2 서비스 id 구분 코드 (구글, 카카오, 네이버)
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        log.info("registrationId = {}", registrationId);

        // OAuth2 로그인 진행시 키가 되는 필드 값 (PK) (구글의 기본 코드는 "sub")
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails()
                .getUserInfoEndpoint()
                .getUserNameAttributeName();
        log.info("userNameAttributeName = {}", userNameAttributeName);

        // OAuth2UserService를 통해 가져온 OAuth2User의 attribute를 담을 클래스
        OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());
        log.info("attributes = {}", attributes);

        User user = saveOrUpdate(attributes);
        log.info("user = {}", user);

        // 세션 정보를 저장하는 직력화된 DTO 클래스
        httpSession.setAttribute("user", new SessionUser(user));

        // 사용자의 권한 정보와 속성을 사용하여 OAuth2User 객체를 생성하여 반환합니다.
        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(user.getRoleKey())),
                attributes.getAttributes(),
                attributes.getNameAttributeKey()
        );
    }

    /**
     * findByEmail 메소드를 통해 이메일에 해당하는 회원을 찾는데,
     * 회원이 있다면 엔티티를 업데이트하고, (map 부분에서 update를 실행)
     * 만족하는 회원이 없다면 새로운 엔티티를 생성해서 반환 (orElse를 통해 만족하지 않으면 새로운 엔티티를 생성)
     */
    private User saveOrUpdate(OAuthAttributes attributes) {
        User user = userRepository.findByEmail(attributes.getEmail())
                .map(entity -> entity.update(entity.getName(), entity.getPicture())) // 사용자의 이름이나, 프로필 사진이 변경되면 User 엔티티도 반영
                .orElse(attributes.toEntity());

        return userRepository.save(user); // 만들어진 엔티티를 DB에 저장
    }
}
