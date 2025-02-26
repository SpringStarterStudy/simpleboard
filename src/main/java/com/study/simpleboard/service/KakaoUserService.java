package com.study.simpleboard.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.simpleboard.common.exception.CustomException;
import com.study.simpleboard.common.exception.ErrorCode;
import com.study.simpleboard.dto.*;
import com.study.simpleboard.dto.response.UserResponse;
import com.study.simpleboard.mapper.UserMapper;
import com.study.simpleboard.mapper.UserSocialMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
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

@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoUserService {
    public static final String TOKEN_REQUEST_URL = "https://kauth.kakao.com/oauth/token";
    public static final String USER_INFO_REQUEST_URL = "https://kapi.kakao.com/v2/user/me";
    public static final String GRANT_TYPE_PARAM = "grant_type";
    public static final String GRANT_TYPE_VALUE = "authorization_code";
    public static final String CLIENT_ID_PARAM = "client_id";
    public static final String CLIENT_SECRET_PARAM = "client_secret";
    public static final String REDIRECT_URI_PARAM = "redirect_uri";
    public static final String CODE_PARAM = "code";

    private final UserService userService;
    private final UserMapper userMapper;
    private final UserSocialMapper userSocialMapper;

    // 카카오 API 관련 설정값들을 application.yml에서 가져옴
    @Value("${kakao.client.id}")
    private String clientId;

    @Value("${kakao.client.secret}")
    private String clientSecret;

    @Value("${kakao.client.redirect-uri}")
    private String redirectUri;

    // 카카오 로그인
    @Transactional
    public UserResponse loginKakaoUser(String code) {
        log.info("=== Login Kakao User Started ===");
        log.info("Received code in service: {}", code);

        // 인가 코드로 액세스토큰 요청
        KakaoToken kakaoToken = getKakaoToken(code);
        log.info("Token received: {}", kakaoToken != null);

        // 액세스 토큰으로 카카오 사용자 정보 요청
        KakaoUser kakaoUser = getKakaoUser(kakaoToken.getAccessToken());
        String providerId = String.valueOf(kakaoUser.getId());
        log.info("Kakao user info received. Provider ID: {}", providerId);

        // 카카오 ID로 가입된 기존 회원인지 확인
        // 회원 찾기 또는 생성
        User user = findOrCreateKakaoUser(kakaoToken, kakaoUser, providerId);

        // 인증 처리
        authenticateUser(user);

        return UserResponse.from(user);
    }

    // 카카오 액세스 토큰 요청
    private KakaoToken getKakaoToken(String code) {
        log.info("Requesting Kakao token with code: {}", code); // 로깅

        String reqURL = TOKEN_REQUEST_URL;
        log.info("Token URL: {}", reqURL); // 로깅

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        log.info("Headers: {}", headers); // 로깅

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add(GRANT_TYPE_PARAM, GRANT_TYPE_VALUE);
        params.add(CLIENT_ID_PARAM, clientId);
        params.add(CLIENT_SECRET_PARAM, clientSecret);
        params.add(REDIRECT_URI_PARAM, redirectUri);
        params.add(CODE_PARAM, code);
        log.info("Token request params: {}", params);  // 파라미터 로깅

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
        // RestTemplate을 사용하여 카카오 토큰 API 호출
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<KakaoToken> response = restTemplate.exchange(
                reqURL,
                HttpMethod.POST,
                request,
                KakaoToken.class
        );
        log.info("Response status: {}", response.getStatusCode()); // 로깅
        log.info("Response body: {}", response.getBody()); // 로깅

        return response.getBody();
    }

    // 카카오 사용자 정보 요청
    private KakaoUser getKakaoUser(String accessToken) {
        String reqURL = USER_INFO_REQUEST_URL;

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

    // 카카오 유저 로그아웃
    @Transactional
    public void kakaoLogout(CustomUserDetails userDetails) {
        try {
            // 사용자의 소셜 정보 조회
            UserSocial userSocial = userSocialMapper.findByUserId(userDetails.getUserId())
                    .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
            log.info("User social info found: {}", userSocial);

            // 사용자 토큰 정보 삭제 또는 무효화
            userSocialMapper.deleteToken(userDetails.getUserId());

            SecurityContextHolder.clearContext();
        } catch (Exception e) {
            throw new CustomException(ErrorCode.LOGOUT_FAILED);
        }
    }

    // 카카오 로그인 유저 탈퇴
    @Transactional
    public void deleteKakaoUser(Long userId) {
        log.info("deleteKakaoUser method started for userId: {}", userId);

        User user = userMapper.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        log.info("user found: {}", user);

        // 카카오 연동 해제 API 호출 (선택)

        // 소셜 정보 먼저 삭제
        userSocialMapper.deleteUserSocial(userId);

        // 사용자 삭제
        userMapper.deleteUser(userId);

        // 삭제 확인
        User deletedUser = userMapper.findById(userId).orElse(null);
        log.info("After deletion check: {}", deletedUser);  // null이어야 정상
    }


    // 메서드
    // 회원 찾기/생성
    private User findOrCreateKakaoUser(KakaoToken kakaoToken, KakaoUser kakaoUser, String providerId) {
        Optional<UserSocial> userSocial = userSocialMapper.findByProviderAndProviderId(String.valueOf(SocialType.KAKAO), providerId);

        if (userSocial.isPresent()) {
            User user = userMapper.findById(userSocial.get().getUserId())
                    .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

            // 리프레시 토큰이 변경된 경우에만 업데이트
            if (!userSocial.get().getRefreshToken().equals(kakaoToken.getRefreshToken())) {
                updateKakaoToken(userSocial.get(), kakaoToken);
            }

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
        userSocial.updateToken(kakaoToken); // 리프레시 토큰 업데이트
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
