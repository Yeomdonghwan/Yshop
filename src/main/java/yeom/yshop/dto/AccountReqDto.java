package yeom.yshop.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import yeom.yshop.entity.Account;

@Getter
@NoArgsConstructor
public class AccountReqDto {

    @NotBlank
    private String nickname;
    @NotBlank
    private String email;
    @NotBlank
    private String password;

    private String passwordCheck;

    public AccountReqDto(String nickname,String email, String pw, String pwck) {
        this.email = email;
        this.password = pw;
        this.passwordCheck = pwck;
        this.nickname=nickname;
    }

    public AccountReqDto(Account account) {
        this.email=account.getEmail();
        this.nickname=account.getNickname();
//        this.password=account.getPassword(); 패스워드는 외부로 보내지 않음
    }

    public void setEncodePwd(String encodePwd) {
        this.password = encodePwd;
    }
}
