package com.devmiffi.flyway.repositories;

import com.devmiffi.flyway.domain.Book;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends CrudRepository<Book, String> {

}
