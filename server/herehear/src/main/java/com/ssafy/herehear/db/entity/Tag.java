package com.ssafy.herehear.db.entity;

import javax.persistence.Entity;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Tag extends BaseEntity {
	private String category;
}
