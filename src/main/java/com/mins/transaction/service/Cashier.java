package com.mins.transaction.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class Cashier {

    private final BookShop bokBookShop;

    @Transactional
    public void checkout(List<String> isbns, String username) {
        isbns.forEach(isbn -> bokBookShop.purchase(isbn, username));
    }
}