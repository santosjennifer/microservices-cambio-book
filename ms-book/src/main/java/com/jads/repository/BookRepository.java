package com.jads.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jads.model.Book;

public interface BookRepository extends JpaRepository<Book, Long> {

}
