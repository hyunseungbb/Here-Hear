package com.ssafy.herehear.api.response;

import java.util.Date;

import com.ssafy.herehear.db.entity.Book;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@ApiModel("CommentGetMyResponse")
public class CommentGetMyRes {
	
	@ApiModelProperty(name = "감상평 ID")
	private Long id;
	
	@ApiModelProperty(name = "유저이름")
	private String username;
	
	@ApiModelProperty(name = "책")
	private Book book;
	
	@ApiModelProperty(name = "감상평 내용")
	private String content;
	
	@ApiModelProperty(name = "독서 시간")
	private Double reading_time;
	
	@ApiModelProperty(name = "독서 날짜")
	private Date date;
	
	@ApiModelProperty(name = "댓글 공개 여부", example = "true: 공개, false: 비공개")
	private Boolean isshow;
	
}
