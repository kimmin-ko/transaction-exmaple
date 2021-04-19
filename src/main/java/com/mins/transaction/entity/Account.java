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
@Check(constraints = "BALANCE >= 0")
public class Account implements Persistable<String> {

    @Id
    private String username;

    private Integer balance;

    @Builder
    public Account(String username, Integer balance) {
        this.username = username;
        this.balance = balance;
    }

    @Override
    public String getId() {
        return this.username;
    }

    @Override
    public boolean isNew() {
        return true;
    }

    public void minusBalance(Integer price) {
        Objects.requireNonNull(price, "price is required.");

        this.balance -= price;
    }
}
