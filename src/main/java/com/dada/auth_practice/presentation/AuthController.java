package com.dada.auth_practice.presentation;

import com.dada.auth_practice.application.dto.KakaoLogoutResponseDto;
import com.dada.auth_practice.application.dto.KakaoUnlinkResponseDto;
import com.dada.auth_practice.application.dto.KakaoUserInfoResponseDto;
import com.dada.auth_practice.application.service.LoginService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/oauth")
public class AuthController {

    private final LoginService loginService;
    private String accessToken;

    @GetMapping("/login")
    public RedirectView getAccessCode() {
        RedirectView response = new RedirectView();
        response.setUrl(loginService.getAccessCode());
        log.info(response.getUrl());
        return response;
    }

    @GetMapping("/redirect-login")
    public ResponseEntity<?> getAccessToken(@RequestParam("code") String code) {
        log.info("Get access token: {}", code);
        accessToken = loginService.getAccessToken(code);
        log.info("카카오 토큰 : {}", accessToken);

        return new ResponseEntity<>(accessToken, HttpStatus.OK);
    }

    @GetMapping("/userInfo")
    public ResponseEntity<?> getUserInfo() {
//        log.info("유저 인포 진입 인가 코드 : {}", code);
//        String accessToken = loginService.getAccessToken(code);
        KakaoUserInfoResponseDto userInfo = loginService.getUserInfo(accessToken);
        log.info("유저 인포 성공 userInfo : {}", userInfo);

        return new ResponseEntity<>(userInfo, HttpStatus.OK);
    }

    @GetMapping("/logout")
    public ResponseEntity<?> logout() {
        log.info("로그아웃 진입");
//        String accessToken = loginService.getAccessToken(code);
        KakaoLogoutResponseDto logout = loginService.logout(accessToken);
        log.info("로그아웃 성공 logout : {}", logout);

        return new ResponseEntity<>(logout, HttpStatus.OK);
        //얘도 redirectUrl 알아봐바
    }

    @GetMapping("/redirect-logout")
    public RedirectView logoutWithKakaoAccount() {
        RedirectView response = new RedirectView();
        response.setUrl(loginService.logoutWithKakaoAccount());
        return response;
    }

    @GetMapping("/unlink")
    public ResponseEntity<?> unlink() {
        log.info("탈퇴 진입");
//        String accessToken = loginService.getAccessToken(code);
        KakaoUnlinkResponseDto unlink = loginService.unlink(accessToken);
        log.info("탈퇴 성공 unlink : {}", unlink);

        return new ResponseEntity<>(unlink, HttpStatus.OK);
    }
}
