package com.dada.auth_practice.application.service;

import com.dada.auth_practice.application.dto.KakaoLogoutResponseDto;
import com.dada.auth_practice.application.dto.KakaoTokenResponseDto;
import com.dada.auth_practice.application.dto.KakaoUnlinkResponseDto;
import com.dada.auth_practice.application.dto.KakaoUserInfoResponseDto;
import io.netty.handler.codec.http.HttpHeaderValues;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class LoginServiceImpl implements LoginService {

    @Value("${kakao.auth.uri}")
    String kakaoAuthUri;

    @Value("${kakao.api.uri}")
    String kakaoApiUri;

    @Value("${kakao.client.id}")
    String kakaoClientId;

    @Value("${kakao.redirect.uri.login}")
    String kakaoRedirectUriLogin;

    @Value("${kakao.redirect.uri.logout}")
    String kakaoRedirectUriLogout;

    @Value("${kakao.response.type}")
    String kakaoResponseType;

    @Value("${kakao.grant.type}")
    String kakaoGrantType;

    @Override
    public String getAccessCode() {
        return kakaoAuthUri + "/oauth/authorize?client_id=" +
                kakaoClientId + "&redirect_uri=" +
                kakaoRedirectUriLogin + "&response_type=code";
    }

    @Override
    public String getAccessToken(String code) {

        WebClient webClient = WebClient.create(kakaoAuthUri);

        KakaoTokenResponseDto response = webClient
                .post()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .path("/oauth/token")
                        .queryParam("grant_type", kakaoGrantType)
                        .queryParam("client_id", kakaoClientId)
                        .queryParam("code", code)
                        .build(true))
                .header(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_X_WWW_FORM_URLENCODED.toString())
                .retrieve()
                .bodyToMono(KakaoTokenResponseDto.class)
                .block();

        return response.getAccessToken();
    }

    @Override
    public KakaoUserInfoResponseDto getUserInfo(String accessToken) {
        log.info("getUserInfo 집입");
        log.info("accessToken : {}", accessToken);

        WebClient webClient = WebClient.create(kakaoApiUri);

        KakaoUserInfoResponseDto userInfo = webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .path("/v2/user/me")
                        .build(true))
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .header(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_X_WWW_FORM_URLENCODED.toString())
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> Mono.error(new RuntimeException("Invalid Parameter")))
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse -> Mono.error(new RuntimeException("Internal Server Error")))
                .bodyToMono(KakaoUserInfoResponseDto.class)
                .block();
        log.info("user info - id: {}", userInfo.getId());
        return userInfo;
    }

    @Override
    public KakaoLogoutResponseDto logout(String accessToken) {
        log.info("logout 집입");
        log.info("accessToken : {}", accessToken);

        WebClient webClient = WebClient.create(kakaoApiUri);

        KakaoLogoutResponseDto logout = webClient
                .post()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .path("/v1/user/logout")
                        .build(true))
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> Mono.error(new RuntimeException("Invalid Parameter")))
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse -> Mono.error(new RuntimeException("Internal Server Error")))
                .bodyToMono(KakaoLogoutResponseDto.class)
                .block();
        log.info("logout - id: {}", logout.getId());
        return logout;
    }

    @Override
    public String logoutWithKakaoAccount() {
        return kakaoAuthUri + "/oauth/logout?client_id=" +
                kakaoClientId + "&logout_redirect_uri=" +
                kakaoRedirectUriLogout;
    }

    @Override
    public KakaoUnlinkResponseDto unlink(String accessToken) {
        log.info("unlink 집입");
        log.info("accessToken : {}", accessToken);

        WebClient webClient = WebClient.create(kakaoApiUri);

        KakaoUnlinkResponseDto unlink = webClient
                .post()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .path("/v1/user/unlink")
                        .build(true))
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> Mono.error(new RuntimeException("Invalid Parameter")))
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse -> Mono.error(new RuntimeException("Internal Server Error")))
                .bodyToMono(KakaoUnlinkResponseDto.class)
                .block();
        log.info("unlink - id: {}", unlink.getId());
        return unlink;
    }
}
