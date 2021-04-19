package com.mins.transaction.config;

import com.mins.transaction.entity.Account;
import com.mins.transaction.entity.Book;
import com.mins.transaction.entity.BookStock;
import com.mins.transaction.repository.AccountRepository;
import com.mins.transaction.repository.BookRepository;
import com.mins.transaction.repository.BookStockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
public class DBInit {

    private final AccountRepository accountRepository;
    private final BookRepository bookRepository;
    private final BookStockRepository bokBookStockRepository;

    @PostConstruct
    public void init() {
        Account account = Account.builder()
                .username("user1")
                .balance(40)
                .build();

        accountRepository.save(account);

        Book book1 = Book.builder()
                .isbn("0001")
                .bookName("The First Book")
                .price(30)
                .build();

        Book book2 = Book.builder()
                .isbn("0002")
                .bookName("The Second Book")
                .price(50)
                .build();

        bookRepository.save(book1);
        bookRepository.save(book2);

        BookStock bookStock1 = BookStock.builder()
                .isbn(book1.getIsbn())
                .stock(10)
                .build();

        BookStock bookStock2 = BookStock.builder()
                .isbn(book2.getIsbn())
                .stock(10)
                .build();

        bokBookStockRepository.save(bookStock1);
        bokBookStockRepository.save(bookStock2);
    }

}
