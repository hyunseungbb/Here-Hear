package com.ssafy.herehear.api.response;

import com.ssafy.herehear.db.entity.Account;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 유저 로그인 API ([POST] /api/v1/auth) 요청에 대한 응답값 정의.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("AccountLoginPostResponse")
public class AccountRes extends BaseResponseBody {
	
	@ApiModelProperty(name="Username", example="ssafy")
	String username;
	
	public static AccountRes of(Account account) {
        return new AccountRes(account.getUsername());
    }
}