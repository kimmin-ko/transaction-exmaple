package com.mins.transaction.repository;

import com.mins.transaction.entity.BookStock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookStockRepository extends JpaRepository<BookStock, String> {
}
