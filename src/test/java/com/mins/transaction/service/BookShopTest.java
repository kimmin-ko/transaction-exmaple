package com.mins.transaction.service;

import com.mins.transaction.entity.Account;
import com.mins.transaction.entity.BookStock;
import com.mins.transaction.repository.AccountRepository;
import com.mins.transaction.repository.BookStockRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.annotation.Commit;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class BookShopTest {

    @Autowired
    Cashier cashier;

    @Autowired
    BookShop bookShop;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    BookStockRepository bookStockRepository;

    @Test
    @DisplayName("user1이 책 두 권을 체크아웃할 때 잔고 부족으로 두 권 모두 구매 불가")
    void transaction_propagation_required_test() {
        // given
        final List<String> isbns = Arrays.asList("0001", "0002");
        final String username = "user1";

        // when then
        assertThatThrownBy(() -> cashier.checkout(isbns, username))
                .isInstanceOf(DataIntegrityViolationException.class) // 무결성 제약조건 위반 예외
                .hasMessageContaining("BALANCE >= 0");

        // given
        Account findAccount = accountRepository.findById(username).get();
        BookStock bookStock1 = bookStockRepository.findById(isbns.get(0)).get();
        BookStock bookStock2 = bookStockRepository.findById(isbns.get(1)).get();

        // then
        assertThat(findAccount.getBalance()).isEqualTo(40);
        assertThat(bookStock1.getStock()).isEqualTo(10);
        assertThat(bookStock2.getStock()).isEqualTo(10);
    }

    @Test
    @DisplayName("user1이 책 두 권을 체크아웃할 때 잔고 부족으로 0001 책만 구매 가능, 0002 책은 구매 불가")
    void transaction_propagation_required_new_test() {
        // given
        final List<String> isbns = Arrays.asList("0001", "0002");
        final String username = "user1";

        // when then
        assertThatThrownBy(() -> cashier.checkout(isbns, username))
                .isInstanceOf(DataIntegrityViolationException.class) // 무결성 제약조건 위반 예외
                .hasMessageContaining("BALANCE >= 0");

        // given
        Account findAccount = accountRepository.findById(username).get();
        BookStock bookStock1 = bookStockRepository.findById(isbns.get(0)).get();
        BookStock bookStock2 = bookStockRepository.findById(isbns.get(1)).get();

        // then
        assertThat(findAccount.getBalance()).isEqualTo(10);
        assertThat(bookStock1.getStock()).isEqualTo(9);
        assertThat(bookStock2.getStock()).isEqualTo(10);
    }

}