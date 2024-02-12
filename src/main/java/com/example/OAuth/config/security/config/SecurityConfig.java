package com.example.OAuth.config.security.config;

import com.example.OAuth.config.security.oauth.CustomOAuth2UserService;
import com.example.OAuth.entity.Role;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.web.SecurityFilterChain;

@Configuration // 스프링 구성 클래스임을 나타내는 어노테이션
@EnableWebSecurity // 스프링 시큐리티 활성화
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final ClientRegistrationRepository clientRegistrationRepository;

    // 특정 HTTP 요청에 대한 웹 기반 보안 구성
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            // stateless한 rest api를 개발할 것이므로 csrf 공격에 대한 옵션은 꺼둔다.
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests((authorizeRequests) -> { authorizeRequests

                // 특정 URL에 대한 권한 설정
                .requestMatchers("/", "/css/**", "/images/**", "/js/**", "/h2-console/**").permitAll()
//                    .requestMatchers("/css/**", "/images/**", "/js/**", "/h2-console/**").authenticated() // 이 URL들은 permitAll() 옵션으로 전체 열람 권한
                .requestMatchers("/api/v1/**").hasRole(Role.USER.name()) // 이 URL은 USER 권한을 가진 사람만 열람 가능하도록 함.
                .anyRequest().authenticated(); // anyRequest는 나머지 URL들을 의미하며, authenticated()을 추가하여 나머지 URL들은 모두 인증된 사용자들에게만 허용하도록 함. (로그인한 유저만)
            });

        //logout 관련 처리
        http.logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .clearAuthentication(true)
                .permitAll()
        );

        // 소셜로그인 성공 후 후속조치
        http.oauth2Login(oauth2 -> oauth2
                .loginPage("/login")
                .defaultSuccessUrl("/", true)
                .userInfoEndpoint(userInfo -> userInfo
                        .userService(this.customOAuth2UserService))
                
                // 구글 로그인 시 자동로그인 방지
                .authorizationEndpoint(authorizationEndpoint -> authorizationEndpoint
                        .authorizationRequestResolver(new CustomAuthorizationRequestResolver(this.clientRegistrationRepository))));

        // 권한이 필요한 요청은 해당 url로 리다이렉트
        http.formLogin(formLogin -> formLogin
            .loginPage("/login")
            .usernameParameter("username")
            .passwordParameter("password")
            .defaultSuccessUrl("/", true)
            .permitAll()
        );

        return http.build();
    }

}
