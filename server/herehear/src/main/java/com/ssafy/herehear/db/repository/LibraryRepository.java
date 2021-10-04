package com.ssafy.herehear.db.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ssafy.herehear.db.entity.Library;

@Repository
public interface LibraryRepository extends JpaRepository<Library, Long> {
	public List<Library> findAll();
	public List<Library> findByBook_id(Long book_id);
	public Optional<Library> findById(Long id);
}
