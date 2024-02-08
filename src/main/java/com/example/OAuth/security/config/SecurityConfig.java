package com.example.OAuth.security.config;

import com.example.OAuth.entity.Role;
import com.example.OAuth.security.oauth.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;

    // 특정 HTTP 요청에 대한 웹 기반 보안 구성
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // stateless한 rest api를 개발할 것이므로 csrf 공격에 대한 옵션은 꺼둔다.
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((authorizeRequests) -> { authorizeRequests

                    // 특정 URL에 대한 권한 설정
//                    .requestMatchers("/", "/logout").permitAll()
//                    .requestMatchers("/css/**", "/images/**", "/js/**", "/h2-console/**").authenticated() // 이 URL들은 permitAll() 옵션으로 전체 열람 권한
//                    .requestMatchers("/api/v1/**").hasRole(Role.USER.name()) // 이 URL은 USER 권한을 가진 사람만 열람 가능하도록 함.
                    .anyRequest().permitAll(); // anyRequest는 나머지 URL들을 의미하며, authenticated()을 추가하여 나머지 URL들은 모두 인증된 사용자들에게만 허용하도록 함. (로그인한 유저만)
                });

        //logout 관련 처리
//        http.logout(logout -> logout
//                .logoutUrl("/logout")
//                .logoutSuccessUrl("/")
//                .invalidateHttpSession(true)
//                .deleteCookies("JSESSIONID")
//                .permitAll()
//        );
//
//        // 소셜로그인 성공 후 후속조치
//        http.oauth2Login(oauth2 -> oauth2
//                .loginPage("/auth/login")
//                .successHandler(successHandler())
//                .userInfoEndpoint(userInfo -> userInfo
//                        .userService(this.customOAuth2UserService)));
//
        // 권한이 필요한 요청은 해당 url로 리다이렉트
        http.formLogin(formLogin -> formLogin
            .loginPage("/auth/login")
            .usernameParameter("email")
            .passwordParameter("password")
            .defaultSuccessUrl("/", true)
            .permitAll()
        );

        return http.build();
    }

    @Bean
    public AuthenticationSuccessHandler successHandler() {
        return (((request, response, authentication) -> {
            DefaultOAuth2User authorities = (DefaultOAuth2User) authentication.getAuthorities();
            String id = authorities.getAttributes().get("id").toString();
            String body = """
                    {"id":"%s"}
                    """.formatted(id);

            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());

            PrintWriter writer = response.getWriter();
            writer.println(body);
            writer.flush();
        }));
    }

}
