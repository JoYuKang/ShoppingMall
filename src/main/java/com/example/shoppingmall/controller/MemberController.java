package com.example.shoppingmall.controller;

import com.example.shoppingmall.constant.Role;
import com.example.shoppingmall.dto.ItemSearchDto;
import com.example.shoppingmall.dto.MemberFormDto;
import com.example.shoppingmall.dto.MemberSearchDto;
import com.example.shoppingmall.entity.Item;
import com.example.shoppingmall.entity.KakaoProfile;
import com.example.shoppingmall.entity.Member;
import com.example.shoppingmall.entity.OAuthToken;
import com.example.shoppingmall.repository.MemberRepository;
import com.example.shoppingmall.service.MemberService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
import java.security.Principal;
import java.util.Optional;

@RequestMapping("/member")
@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;
    private MemberRepository memberRepository;

    String grant_type = "authorization_code";
    String client_id = "250930d8d97ff9d3b375782381f68f33";
    String redirect_uri = "http://localhost/member/kakao/callback";

    @GetMapping(value = "/new")
    public String memberForm(Model model) {
        model.addAttribute("memberFormDto", new MemberFormDto());
        return "member/memberForm";
    }

//    @GetMapping(value = "/memberCheck")
//    public String memberCheck(Model model) {
//        model.addAttribute("memberFormDto", new MemberFormDto());
//
//        return "member/memberCheck";
//    }

    @GetMapping(value = "/memberCheck")
    public String memberCheck(Model model, Principal principal) {

        Member member = memberService.checkMember(principal.getName());

        model.addAttribute("memberFormDto", member);

        return "member/memberCheck";
    }


    @PostMapping(value = "/update")
    public String updateMember(@Valid MemberFormDto memberFormDto, BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            return "member/memberCheck";
        }
        try {

            Member member = Member.updateMember(memberFormDto, passwordEncoder);

            memberService.updateMember(member);

        } catch (IllegalStateException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "member/memberCheck";
        }

        return "member/memberCheck";
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
        model.addAttribute("loginErrorMsg", "????????? ?????? ??????????????? ??????????????????");
        return "member/memberLoginForm";
    }


    // ????????? ???????????? ??????
    @GetMapping(value = "/kakao/callback")
    public @ResponseBody
    RedirectView kakaoCallback(String code) {

        // a???????????? GET???????????? ????????? POST???????????? ?????? ???????????? ????????? RESTTemplate ??????
        RestTemplate rt = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.set("grant_type", grant_type);
        params.set("client_id", client_id);
        params.set("redirect_uri", redirect_uri);
        params.set("code", code);

        // HttpHeader??? HttpBody??? ????????? ??????????????? ??????
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(params, httpHeaders);

        // Http ???????????? -POST????????????  response ?????? ??????
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
        System.out.println("????????? ?????? : " + oAuthToken.getAccess_token());

        RestTemplate rt2 = new RestTemplate();
        HttpHeaders httpHeaders2 = new HttpHeaders();
        httpHeaders2.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        httpHeaders2.add("Authorization", "Bearer " + oAuthToken.getAccess_token());
        // HttpHeader??? HttpBody??? ????????? ??????????????? ??????
        HttpEntity<MultiValueMap<String, String>> kakaoProfileRequest2 = new HttpEntity<>(httpHeaders2);

        // Http ???????????? -POST????????????  response ?????? ??????
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
//        System.out.println(kakaoProfile.getId());
//
//        System.out.println(kakaoProfile.getConnected_at());

        //????????? ???????????? member ????????? member email name password

//        System.out.println("email: " + kakaoProfile.kakao_account.getEmail() + "_" + kakaoProfile.getId());
//        System.out.println("name: " + kakaoProfile.getProperties().getNickname());
//        System.out.println("password: " + kakaoProfile.getId());

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

    //member ?????? ??????
//    @GetMapping(value = {"/members", "/members/{page}"})
//    public String memberManage(MemberFormDto memberFormDto, @PathVariable("page") Optional<Integer> page, Model model) {
//        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 3);
//        Page<Member> members = memberService.getMemberAll(memberFormDto, pageable);
//        model.addAttribute("members", members);
//        model.addAttribute("memberFormDto", memberFormDto);
//        model.addAttribute("MaxPage", 5);
//        return "member/memberAll";
//    }

}
