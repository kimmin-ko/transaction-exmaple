package com.mins.transaction.repository;

import com.mins.transaction.entity.Account;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
@DataJpaTest
class AccountRepositoryTest {

    @Autowired
    AccountRepository accountRepository;

    @Test
    void save() {
        // given
        Account account = Account.builder()
                .username("01")
                .balance(10)
                .build();

        // when
        accountRepository.save(account);

        Account findAccount = accountRepository.findById(account.getId()).get();

        System.out.println("account = " + account);
        System.out.println("findAccount = " + findAccount);

        // then
        assertThat(findAccount).isEqualTo(account);
    }


}