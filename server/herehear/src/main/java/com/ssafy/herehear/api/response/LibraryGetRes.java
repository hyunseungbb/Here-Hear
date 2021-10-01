package com.ssafy.herehear.api.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel("LibraryGetResponse")
public class LibraryGetRes {
	@ApiModelProperty(name="내 서재 ID")
	private Long id;
	
	@ApiModelProperty(name="책 ID")
	private Long book_id;
	
	@ApiModelProperty(name="유저 ID")
	private Long user_id;
	
	@ApiModelProperty(name="읽음 상태", example = "전:0, 중:1, 후:2")
	private int read_status;
	
	@ApiModelProperty(name="별점")
	private int stars;
}
