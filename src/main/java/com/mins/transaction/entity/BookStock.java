package com.mins.transaction.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Check;
import org.springframework.data.domain.Persistable;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Objects;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Check(constraints = "STOCK >= 0")
public class BookStock implements Persistable<String> {

    @Id
    private String isbn;

    private Integer stock;

    @Builder
    public BookStock(String isbn, Integer stock) {
        this.isbn = isbn;
        this.stock = stock;
    }

    @Override
    public String getId() {
        return this.isbn;
    }

    @Override
    public boolean isNew() {
        return true;
    }

    public void increaseStock(Integer stock) {
        Objects.requireNonNull(stock, "stock is required.");

        this.stock += stock;
    }

    public void decreaseStock(Integer stock) {
        Objects.requireNonNull(stock, "stock is required.");

        this.stock -= stock;
    }
}
