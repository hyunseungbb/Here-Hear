package com.ssafy.herehear.api.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel("BookSearchGetResponse")
public class BookSearchGetRes {
	
	@ApiModelProperty(name="책 ID")
	private Long id;
	
	@ApiModelProperty(name="책 제목")
	private String title;
	
	@ApiModelProperty(name="책 표지")
	private String img_url;
	
}
