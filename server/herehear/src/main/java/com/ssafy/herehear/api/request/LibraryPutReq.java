package com.ssafy.herehear.api.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel("LibraryPutRequest")
public class LibraryPutReq {
	
	@ApiModelProperty(name="서재 ID")
	private Long id;
	
	@ApiModelProperty(name="읽음 상태", example = "전:0, 중:1, 후:2")
	private int read_status;
	
	@ApiModelProperty(name="별점")
	private int stars;
}
