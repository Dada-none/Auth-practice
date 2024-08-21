package com.dada.auth_practice.application.service;

import com.dada.auth_practice.application.dto.KakaoLogoutResponseDto;
import com.dada.auth_practice.application.dto.KakaoUnlinkResponseDto;
import com.dada.auth_practice.application.dto.KakaoUserInfoResponseDto;

public interface LoginService {
    String getAccessToken(String code);
    KakaoUserInfoResponseDto getUserInfo(String accessToken);
    KakaoLogoutResponseDto logout(String accessToken);
    KakaoUnlinkResponseDto unlink(String accessToken);
}
