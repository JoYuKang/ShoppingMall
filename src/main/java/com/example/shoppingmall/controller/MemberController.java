package com.example.shoppingmall.controller;

import com.example.shoppingmall.constant.Role;
import com.example.shoppingmall.dto.MemberFormDto;
import com.example.shoppingmall.entity.KakaoProfile;
import com.example.shoppingmall.entity.Member;
import com.example.shoppingmall.entity.OAuthToken;
import com.example.shoppingmall.service.MemberService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.view.RedirectView;

import javax.validation.Valid;

@RequestMapping("/member")
@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    String grant_type = "authorization_code";
    String client_id = "250930d8d97ff9d3b375782381f68f33";
    String redirect_uri = "http://localhost/member/kakao/callback";

    @GetMapping(value = "/new")
    public String memberForm(Model model) {
        model.addAttribute("memberFormDto", new MemberFormDto());
        return "member/memberForm";
    }

    @PostMapping(value = "/new")
    public String newMember(@Valid MemberFormDto memberFormDto, BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            return "member/memberForm";
        }

        try {
            Member member = Member.createMember(memberFormDto, passwordEncoder);
            memberService.saveMember(member);
        } catch (IllegalStateException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "member/memberForm";
        }

        return "redirect:/";
    }

    @GetMapping(value = "/login")
    public String loginMember() {
        return "member/memberLoginForm";
    }

    @GetMapping(value = "/login/error")
    public String loginError(Model model) {
        model.addAttribute("loginErrorMsg", "아이디 또는 비밀번호를 확인해주세요");
        return "member/memberLoginForm";
    }


    // 카카오 연동정보 조회
    @GetMapping(value = "/kakao/callback")
    public @ResponseBody
    RedirectView kakaoCallback(String code) {

        // a태그에는 GET방식으로 받지만 POST방식으로 바꿔 줘야하기 때문에 RESTTemplate 사용
        RestTemplate rt = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.set("grant_type", grant_type);
        params.set("client_id", client_id);
        params.set("redirect_uri", redirect_uri);
        params.set("code", code);

        // HttpHeader와 HttpBody를 하나의 오브젝트로 담기
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(params, httpHeaders);

        // Http 요청하기 -POST방식으로  response 변수 응답
        ResponseEntity<String> response = rt.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );

        ObjectMapper objectMapper = new ObjectMapper();
        OAuthToken oAuthToken = null;
        try {
            oAuthToken = objectMapper.readValue(response.getBody(), OAuthToken.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        assert oAuthToken != null;
        System.out.println("카카오 토큰 : " + oAuthToken.getAccess_token());

        RestTemplate rt2 = new RestTemplate();
        HttpHeaders httpHeaders2 = new HttpHeaders();
        httpHeaders2.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        httpHeaders2.add("Authorization", "Bearer " + oAuthToken.getAccess_token());
        // HttpHeader와 HttpBody를 하나의 오브젝트로 담기
        HttpEntity<MultiValueMap<String, String>> kakaoProfileRequest2 = new HttpEntity<>(httpHeaders2);

        // Http 요청하기 -POST방식으로  response 변수 응답
        ResponseEntity<String> response2 = rt2.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                kakaoProfileRequest2,
                String.class
        );

        ObjectMapper objectMapper2 = new ObjectMapper();
        KakaoProfile kakaoProfile = null;
        try {
            kakaoProfile = objectMapper2.readValue(response2.getBody(), KakaoProfile.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        assert kakaoProfile != null;
        System.out.println(kakaoProfile.getId());

        System.out.println(kakaoProfile.getConnected_at());

        //카카오 계정으로 member 만들기 member email name password

        System.out.println("email: " + kakaoProfile.kakao_account.getEmail() + "_" + kakaoProfile.getId());
        System.out.println("name: " + kakaoProfile.getProperties().getNickname());
        System.out.println("password: " + kakaoProfile.getId());

        Member member = new Member();
        member.setEmail(kakaoProfile.getKakao_account().getEmail());
        member.setPassword(passwordEncoder.encode(kakaoProfile.getId().toString()));
        member.setName(kakaoProfile.getProperties().getNickname());
        member.setRole(Role.User);
        memberService.saveKakaoMember(member);

        SecurityContextHolder
                .getContext()
                .setAuthentication(new UsernamePasswordAuthenticationToken(
                        kakaoProfile.getKakao_account().getEmail(),
                        kakaoProfile.getId().toString(),
                        AuthorityUtils.createAuthorityList(Role.User.toString())
                ));


        return new RedirectView("/");
    }


}
