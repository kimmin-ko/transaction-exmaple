package com.mins.transaction.service;

import com.fasterxml.jackson.databind.deser.std.StdDelegatingDeserializer;
import com.mins.transaction.entity.Account;
import com.mins.transaction.entity.BookStock;
import com.mins.transaction.repository.AccountRepository;
import com.mins.transaction.repository.BookStockRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

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








    @Test
    @DisplayName("Thread1에서 0001 책의 재고를 5개 올리는 도중 롤백되었지만 Thread2는 이미 올라간 재고인 15의 값을 읽음")
    void transaction_isolation_read_uncommited_test() throws InterruptedException {
        final String isbn = "0001";
        final int stock = 5;

        Thread thread1 = new Thread(() -> bookShop.increaseStock(isbn, stock), "Thread1");
        Thread thread2 = new Thread(() -> {
            // 오염된 값을 읽음 (Dirty Read)
            int readStock = bookShop.checkStock(isbn);

            assertThat(readStock).isEqualTo(15);
        }, "Thread2");

        thread1.start();

        Thread.sleep(2000);

        thread2.start();

        Thread.sleep(6000);
    }

    @Test
    @DisplayName("Thread1에서 0001 책의 재고를 변경 중 롤백된 후에 Thread2가 값을 읽음")
    void transaction_isolation_read_commited_test() throws InterruptedException {
        final String isbn = "0001";
        final int stock = 5;

        Thread thread1 = new Thread(() -> bookShop.increaseStock(isbn, stock), "Thread1");
        Thread thread2 = new Thread(() -> {
            // 커밋 또는 롤백이 완료된 값을 읽음
            int readStock = bookShop.checkStock(isbn);

            assertThat(readStock).isEqualTo(10);
        }, "Thread2");

        thread1.start();

        Thread.sleep(2000);

        thread2.start();

        Thread.sleep(6000);
    }

    @Test
    @DisplayName("")
    void test() throws InterruptedException {
        /* READ_UNCOMMITTED & READ_COMMITED
         * Thread1이 0001 레코드를 읽었지만 Thread2는 해당 레코드를 수정할 수 있음.
         * Thread1이 다시 001 레코드를 읽으면 변경된 값을 읽음
         * Non-Repeatable Read 문제 발생
         *
         * ------- 이론적으론 두 격리 수준 모두 Non-Repeatable Read 문제가 발생해야 하지만
         * ------- READ_COMMITED는 발생하지 않음. 이유는 찾지못함.
         */

        /*
         * REPEATABLE_READ
         * 위와 같이 Thread2는 해당 레코드를 변경할 수 있지만
         * Thread1의 트랜잭션은 snapshot을 이용하기 때문에 두번 째 쿼리에서 일관된 값을 읽어올 수 있음
         */
        final String isbn = "0001";
        final int stock = 5;

        Thread thread1 = new Thread(() -> bookShop.checkStock(isbn), "Thread1");
        Thread thread2 = new Thread(() -> bookShop.increaseStock(isbn, stock), "Thread2");

        thread1.start();

        Thread.sleep(2000);

        thread2.start();

        Thread.sleep(6000);
    }

}
















