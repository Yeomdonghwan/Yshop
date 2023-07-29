package yeom.yshop.service;


import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import yeom.yshop.dto.AccountReqDto;
import yeom.yshop.dto.LoginReqDto;
import yeom.yshop.entity.Account;
import yeom.yshop.entity.Authority;
import yeom.yshop.entity.RefreshToken;
import yeom.yshop.repository.AccountRepository;
import yeom.yshop.repository.RefreshTokenRepository;
import yeom.yshop.redis.RedisUtil;
import yeom.yshop.security.jwt.dto.TokenDto;
import yeom.yshop.security.jwt.util.JwtUtil;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
@RequiredArgsConstructor
public class AccountService {
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final AccountRepository accountRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final RedisUtil redisUtil;
    @Transactional
    public Long signup(AccountReqDto accountReqDto) {
        //nickname 중복검사
        if(accountRepository.findOneWithAuthoritiesByEmail(accountReqDto.getEmail()).isPresent()){
            throw new RuntimeException("Overlap Check");
        }

        // 패스워드 암호화
        accountReqDto.setEncodePwd(passwordEncoder.encode(accountReqDto.getPassword()));
        Account account = new Account(accountReqDto);

        //기본권한 설정
        Authority authority = Authority.builder()
                .authorityName("ROLE_USER")
                .build();
        account.setAuthorities(Collections.singleton(authority));
        // 회원가입 성공
        accountRepository.save(account);
        return account.getId();
    }

    @Transactional
    public void login(LoginReqDto loginReqDto, HttpServletResponse response) {

        // 아이디 검사
        Account account = accountRepository.findOneWithAuthoritiesByEmail(loginReqDto.getEmail()).orElseThrow(
                () -> new RuntimeException("Not found Account")
        );

        // 비밀번호 검사
        if(!passwordEncoder.matches(loginReqDto.getPassword(), account.getPassword())) {
            throw new RuntimeException("Not matches Password");
        }

        // 아이디 정보로 Token생성
        TokenDto tokenDto = jwtUtil.createAllToken(loginReqDto.getEmail(),account.getAuthorities());

        // Refresh토큰 있는지 확인
        Optional<RefreshToken> refreshToken = refreshTokenRepository.findByAccountEmail(loginReqDto.getEmail());

        // 있다면 새토큰 발급후 업데이트
        // 없다면 새로 만들고 디비 저장
        if(refreshToken.isPresent()) {
            refreshTokenRepository.save(refreshToken.get().updateToken(tokenDto.getRefreshToken()));
        }else {
            RefreshToken newToken = new RefreshToken(tokenDto.getRefreshToken(), loginReqDto.getEmail());
            refreshTokenRepository.save(newToken);
        }

        // response 헤더에 Access Token / Refresh Token 넣음
        setHeader(response, tokenDto);

//        return new GlobalResDto("Success Login", HttpStatus.OK.value());

    }

    private void setHeader(HttpServletResponse response, TokenDto tokenDto) {
        response.addHeader(JwtUtil.ACCESS_TOKEN, tokenDto.getAccessToken());
        response.addHeader(JwtUtil.REFRESH_TOKEN, tokenDto.getRefreshToken());
    }

    public Set<Authority> getAuthorities(String loginId) {
        Account account = accountRepository.findOneWithAuthoritiesByEmail(loginId).orElseThrow(
        );
        return account.getAuthorities();
    }

    public String logout(String accessToken, Account account) {
        refreshTokenRepository.deleteRefreshTokenByAccountEmail(account.getEmail());
        redisUtil.setBlackList(accessToken,"accessToken",5);

        return "로그아웃 완료";
    }
}
