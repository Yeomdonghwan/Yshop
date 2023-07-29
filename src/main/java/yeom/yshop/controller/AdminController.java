package yeom.yshop.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import yeom.yshop.dto.AccountReqDto;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final yeom.yshop.account.service.AdminService adminService;
    @GetMapping("/") //테스트용
    @Secured("ROLE_ADMIN")
    ResponseEntity<String> adminOnly(){
        return ResponseEntity.ok("success");
    }

    @GetMapping("/getAllUsers")
    @Secured("ROLE_ADMIN")
    ResponseEntity<List<AccountReqDto>> getAllUsers(){
        List<AccountReqDto> accountDtos = adminService.getAllUsers();
        return ResponseEntity.ok(accountDtos);
    }

    @GetMapping("/account/{accountId}")
    @Secured("ROLE_ADMIN")
    ResponseEntity<AccountReqDto> getAccount(@PathVariable Long accountId){
        AccountReqDto accountDto = adminService.getAccountDtoByAccountId(accountId);
        return ResponseEntity.ok(accountDto);
    }

    @PatchMapping("/account/{accountId}")
    @Secured("ROLE_ADMIN")
    ResponseEntity<Void> assignAdminRole(@PathVariable Long accountId){
        adminService.assignAdminRole(accountId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
