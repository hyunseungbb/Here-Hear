package com.ssafy.herehear.api.request;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel("CommentPostReq")
public class CommentPostReq {
	private Long userId;
	private String content;
	private Double reading_time;
	private Boolean isshow;
}
