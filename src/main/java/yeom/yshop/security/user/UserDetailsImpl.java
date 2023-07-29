package yeom.yshop.security.user;


import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import yeom.yshop.entity.Account;
import yeom.yshop.entity.Authority;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class UserDetailsImpl implements UserDetails {
    private Account account;

    public Account getAccount() {
        return this.account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> authorities = new HashSet<>();
        if (account != null) {
            Set<Authority> accountAuthorities = account.getAuthorities();
            if (accountAuthorities != null) {
                for (Authority authority : accountAuthorities) {
                    authorities.add(new SimpleGrantedAuthority(authority.getAuthorityName()));
                }
            }
        }
        return authorities;
//        return null;
    }
    // 권한 확인 메소드 추가
    public boolean hasRoleAdmin() {
        return getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(role -> role.equals("ROLE_ADMIN"));
    }
    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
