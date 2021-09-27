package com.ssafy.herehear.db.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

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
//    private String vector;    
    private int stars_sum;    
    private int stars_count;
    
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "tag_id")
//    private Tag tag;
//    
//    // 순환참조
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "par_book_id")
//    private Book parent;
//    
//    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parent")
//    private List<Book> similar_book;
}