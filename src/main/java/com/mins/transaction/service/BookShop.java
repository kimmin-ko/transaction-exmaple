package com.mins.transaction.service;

import com.mins.transaction.entity.Account;
import com.mins.transaction.entity.Book;
import com.mins.transaction.entity.BookStock;
import com.mins.transaction.repository.AccountRepository;
import com.mins.transaction.repository.BookRepository;
import com.mins.transaction.repository.BookStockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Component
@RequiredArgsConstructor
public class BookShop {

    private final AccountRepository accountRepository;
    private final BookRepository bookRepository;
    private final BookStockRepository bookStockRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
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
}