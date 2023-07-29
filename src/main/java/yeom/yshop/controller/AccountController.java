package yeom.yshop.controller;


import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import yeom.yshop.dto.AccountReqDto;
import yeom.yshop.dto.LoginReqDto;
import yeom.yshop.entity.Account;
import yeom.yshop.service.AccountService;
import yeom.yshop.security.jwt.dto.TokenDto;
import yeom.yshop.security.user.CurrentUser;
import yeom.yshop.security.user.UserDetailsImpl;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/account")
public class AccountController {
    private final AccountService accountService;

    @PostMapping("/register")
    public ResponseEntity<Long> register(@RequestBody @Valid AccountReqDto accountReqDto) {
        Long accountId= accountService.signup(accountReqDto);
        return ResponseEntity.ok(accountId);
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody @Valid LoginReqDto loginReqDto, HttpServletResponse response) {
        accountService.login(loginReqDto, response);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/logout")
    public ResponseEntity<String> logout(@CurrentUser UserDetailsImpl userDetails, @RequestBody TokenDto tokenDto){
       return ResponseEntity.ok(accountService.logout(tokenDto.getAccessToken(),userDetails.getAccount()));
    }
    @Secured("ROLE_ADMIN")
    @GetMapping("/admin")
    public ResponseEntity<String> adminOnlyMethod() {
        // ROLE_ADMIN 권한이 있는 사용자만 실행 가능한 로직
        return new ResponseEntity<>("success!!",HttpStatus.OK);
    }

    @GetMapping("/current-user")
    public ResponseEntity<Account> getCurrentUser(@AuthenticationPrincipal UserDetailsImpl userDetails) {

        return ResponseEntity.ok(userDetails.getAccount()); // 로그인되지 않은 경우에 대한 처리
    }

    @GetMapping("/getAuth")
    public List<String> getAuth(@CurrentUser UserDetailsImpl userDetails) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null) {
            return authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());
        }

        return List.of("No authorities found");
    }
}
