package com.study.simpleboard.service;

import com.study.simpleboard.common.exception.CustomException;
import com.study.simpleboard.common.exception.ErrorCode;
import com.study.simpleboard.dto.*;
import com.study.simpleboard.dto.response.UserResponse;
import com.study.simpleboard.mapper.UserMapper;
import com.study.simpleboard.mapper.UserSocialMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class KakaoUserService {
    private final UserService userService;
    private final UserMapper userMapper;
    private final UserSocialMapper userSocialMapper;

    // 카카오 API 관련 설정값들을 application.yml에서 가져옴
    @Value("${kakao.client.id}")
    private String clientId;

    @Value("${kakao.client.secret}")
    private String clientSecret;

    @Value("${kakao.redirect.uri}")
    private String redirectUri;

    // 카카오 로그인
    @Transactional
    public UserResponse loginKakaoUser(String code) {
        // 인가 코드로 액세스토큰 요청
        KakaoToken kakaoToken = getKakaoToken(code);

        // 액세스 토큰으로 카카오 사용자 정보 요청
        KakaoUser kakaoUser = getKakaoUser(kakaoToken.getAccessToken());
        String providerId = String.valueOf(kakaoUser.getId());

        // 카카오 ID로 가입된 기존 회원인지 확인
        // 회원 찾기 또는 생성
        User user = findOrCreateKakaoUser(kakaoToken, kakaoUser, providerId);

        // 인증 처리
        authenticateUser(user);

        return UserResponse.from(user);
    }

    // 카카오 액세스 토큰 요청
    private KakaoToken getKakaoToken(String code) {
        String reqURL = "https://kauth.kakao.com/oauth/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", clientId);
        params.add("client_secret", clientSecret);
        params.add("redirect_uri", redirectUri);
        params.add("code", code);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        // RestTemplate을 사용하여 카카오 토큰 API 호출
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<KakaoToken> response = restTemplate.postForEntity(
                reqURL,
                request,
                KakaoToken.class
        );

        return response.getBody();
    }

    // 카카오 사용자 정보 요청
    private KakaoUser getKakaoUser(String accessToken) {
        String reqURL = "https://kapi.kakao.com/v2/user/me";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<String> request = new HttpEntity<>(headers);

        // RestTemplate을 사용하여 카카오 사용자 정보 API 호출
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<KakaoUser> response = restTemplate.exchange(
                reqURL,
                HttpMethod.GET,
                request,
                KakaoUser.class
        );

        return response.getBody();
    }

    // 메서드
    // 회원 찾기/생성
    private User findOrCreateKakaoUser(KakaoToken kakaoToken, KakaoUser kakaoUser, String providerId) {
        Optional<UserSocial> userSocial = userSocialMapper.findByProviderAndProviderId(String.valueOf(SocialType.KAKAO), providerId);

        if (userSocial.isPresent()) {
            User user = userMapper.findById(userSocial.get().getUserId())
                    .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
            updateKakaoToken(userSocial.get(), kakaoToken);
            return user;
        }

        User newUser = createKakaoUser(kakaoUser);
        createUserSocial(newUser.getUserId(), providerId, kakaoToken);
        return newUser;
    }

    // 인증 처리
    private void authenticateUser(User user) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                new CustomUserDetails(user),
                null,
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"))
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Transactional
    private void updateKakaoToken(UserSocial userSocial, KakaoToken kakaoToken) {
        userSocial.updateToken(kakaoToken);
        userSocialMapper.updateToken(userSocial);
    }

    @Transactional
    private User createKakaoUser(KakaoUser kakaoUser) {
        User user = User.createKakaoUser(kakaoUser);
        userMapper.insertUser(user);
        return user;
    }

    @Transactional
    private void createUserSocial(Long userId, String providerId, KakaoToken kakaoToken) {
        UserSocial userSocial = UserSocial.createKakaoUserSocial(userId, providerId, kakaoToken);
        userSocialMapper.insertUserSocial(userSocial);
    }
}
