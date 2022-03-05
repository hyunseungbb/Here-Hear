package com.ssafy.herehear.api.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel("BookGetResponse")
public class BookGetRes {
	
	@ApiModelProperty(name="책 ID")
	private Long id;
	
	@ApiModelProperty(name="책 제목")
	private String title;
	
	@ApiModelProperty(name="책 표지")
	private String img_url;
	
	@ApiModelProperty(name="책 소개")
	private String description;	
	
	@ApiModelProperty(name="별점 총 합")
	private int stars_sum;
	
	@ApiModelProperty(name="별점 갯수")
	private int stars_count;

	@ApiModelProperty(name="저자")
	private String author;
}
