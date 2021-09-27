package com.ssafy.herehear.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ssafy.herehear.db.entity.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

}