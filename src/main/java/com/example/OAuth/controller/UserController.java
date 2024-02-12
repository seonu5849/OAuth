package com.example.OAuth.controller;

import com.example.OAuth.dto.SessionUser;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class UserController {

    private final HttpSession httpSession;

//    @GetMapping("/")
//    public String main(Model model){
//
//        SessionUser user = (SessionUser) httpSession.getAttribute("user");
//        if(user != null){
//            model.addAttribute("username", user.getName());
//        }
//
//        return "main";
//    }

    @GetMapping("/success")
    public String success() {
        return "success";
    }

    @GetMapping("/auth/login")
    public String login(){
        return "login";
    }

//    @PostMapping("/logout")
//    public String logout(HttpServletRequest request, HttpServletResponse response){
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if(authentication != null){
//            new SecurityContextLogoutHandler().logout(request, response, authentication);
//        }
//
//        return "redirect:/";
//    }

}
