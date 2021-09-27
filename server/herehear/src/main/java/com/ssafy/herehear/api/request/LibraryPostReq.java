package com.ssafy.herehear.api.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel("LibraryPostRequest")
public class LibraryPostReq {
	
	@ApiModelProperty(name="À¯Àú ID")
	private Long user_id;
	
	@ApiModelProperty(name="Ã¥ ID")
	private Long book_id;
}
