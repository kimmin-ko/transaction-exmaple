package com.mins.transaction;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.jta.JtaTransactionManager;

@SpringBootApplication
public class TransactionExampleApplication {

	public static void main(String[] args) {
		SpringApplication.run(TransactionExampleApplication.class, args);
	}

}