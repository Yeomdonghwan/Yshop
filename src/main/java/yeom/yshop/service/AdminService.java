package yeom.yshop.account.service;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import yeom.yshop.dto.AccountReqDto;
import yeom.yshop.entity.Account;
import yeom.yshop.repository.AccountRepository;

import java.util.ArrayList;
import java.util.List;

@Transactional
@RequiredArgsConstructor
@Service
public class AdminService {
    private final AccountRepository accountRepository;

    public List<AccountReqDto> getAllUsers() {
        List<Account> accounts = accountRepository.findAll();
        List<AccountReqDto> accountDtos = new ArrayList<>(accounts.size());
        for(Account account: accounts){
            accountDtos.add(new AccountReqDto(account));
        }
        return accountDtos;
    }

    public AccountReqDto getAccountDtoByAccountId(Long accountId) {
        Account account = accountRepository.findById(accountId).orElseThrow(
                ()->new RuntimeException("Invalid accountId: "+accountId)
        );
        return new AccountReqDto(account);
    }

    public void assignAdminRole(Long accountId) {
        Account account = accountRepository.findById(accountId).orElseThrow(
                ()->new RuntimeException("Invalid accountId: "+accountId)
        );
    }
}
