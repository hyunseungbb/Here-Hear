package com.ssafy.herehear.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ssafy.herehear.db.entity.Library;

@Repository
public interface LibraryRepository extends JpaRepository<Library, Long> {

}
