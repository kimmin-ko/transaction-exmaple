package com.mins.transaction.service;

import com.mins.transaction.entity.Account;
import com.mins.transaction.entity.Book;
import com.mins.transaction.entity.BookStock;
import com.mins.transaction.repository.AccountRepository;
import com.mins.transaction.repository.BookRepository;
import com.mins.transaction.repository.BookStockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class BookShop {

    private final EntityManager em;

    private final AccountRepository accountRepository;
    private final BookRepository bookRepository;
    private final BookStockRepository bookStockRepository;

    @Transactional(propagation = Propagation.REQUIRED)
    public void purchase(final String isbn, final String username) {
        Book book = bookRepository.findById(isbn).orElseThrow(EntityNotFoundException::new);

        // book의 재고 1개 차감
        BookStock bookStock = bookStockRepository.findById(isbn).orElseThrow(EntityNotFoundException::new);
        bookStock.decreaseStock(1);

        // account의 잔액 차감
        Account account = accountRepository.findById(username).orElseThrow(EntityNotFoundException::new);
        account.minusBalance(book.getPrice());

        bookStockRepository.flush();
        accountRepository.flush();
    }







    @Transactional
    public void increaseStock(String isbn, int stock) {
        String threadName = Thread.currentThread().getName();
        System.out.println(threadName + " - Prepare to increase book stock");

        BookStock bookStock = bookStockRepository.findById(isbn).orElseThrow(EntityNotFoundException::new);
        bookStock.increaseStock(stock);
        bookStockRepository.flush();

        System.out.println(threadName + " - Book stock increased by " + stock);
        sleep(threadName);

        System.out.println(threadName + " - Book stock rolled back");
        throw new RuntimeException("Increased by mistake");
    }

    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    public int checkStock(String isbn) {
        String threadName = Thread.currentThread().getName();
        System.out.println(threadName + " - Prepare to check book stock");

        BookStock bookStock = bookStockRepository.findById(isbn).orElseThrow(EntityNotFoundException::new);
        System.out.println(threadName + " - Book stock is " + bookStock.getStock());

        sleep(threadName);

//        em.flush();
//        em.clear();

//        bookStock = bookStockRepository.findById(isbn).orElseThrow(EntityNotFoundException::new);
//        System.out.println(threadName + " - Book stock is " + bookStock.getStock());

        return bookStock.getStock();
    }

    private void sleep(String threadName) {
        System.out.println(threadName + " - Sleeping");

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(threadName + " - Wake up");
    }

}





















