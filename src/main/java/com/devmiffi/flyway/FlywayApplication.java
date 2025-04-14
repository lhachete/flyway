package com.devmiffi.flyway;

import com.devmiffi.flyway.repositories.BookRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FlywayApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(FlywayApplication.class, args);
	}

	private final BookRepository bookRepository;

	public FlywayApplication(BookRepository bookRepository, BookRepository bookRepository1) {
		this.bookRepository = bookRepository;
	}

	@Override
	public void run(String... args) throws Exception {
		this.bookRepository.findAll().forEach( book -> {
			System.out.println(book.toString());
		});
	}
}
