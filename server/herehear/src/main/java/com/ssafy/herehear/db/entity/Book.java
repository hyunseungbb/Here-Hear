package com.ssafy.herehear.db.entity;

import javax.persistence.Entity;

import org.hibernate.annotations.ColumnDefault;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Book extends BaseEntity {
	private String isbn13;	
	private String title;	
	private String author;	
	private String description;	
	private String publisher;	
	private String img_url;	
	
	@ColumnDefault("0")
	private int stars_sum;
	
	@ColumnDefault("0")
	private int stars_count;

}