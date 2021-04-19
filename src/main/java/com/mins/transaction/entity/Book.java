package com.mins.transaction.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Check;
import org.springframework.data.domain.Persistable;

import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Book implements Persistable<String> {

    @Id
    private String isbn;

    private String bookName;

    private Integer price;

    @Builder
    public Book(String isbn, String bookName, Integer price) {
        this.isbn = isbn;
        this.bookName = bookName;
        this.price = price;
    }

    @Override
    public String getId() {
        return this.isbn;
    }

    @Override
    public boolean isNew() {
        return true;
    }
}
