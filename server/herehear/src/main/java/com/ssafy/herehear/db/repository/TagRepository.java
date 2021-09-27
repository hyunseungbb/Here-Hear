package com.ssafy.herehear.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ssafy.herehear.db.entity.Tag;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {

}
