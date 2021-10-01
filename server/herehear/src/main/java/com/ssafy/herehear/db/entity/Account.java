package com.ssafy.herehear.db.entity;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 유저 모델 정의.
 */
//Entity가 붙은 클래스는 JPA가 관리하는 클래스이고, 테이블과 매핑할 테이블에 해당 어노테이션을 붙인다.
@Entity
@Getter
@Setter
@NoArgsConstructor
public class Account extends BaseEntity {
	
	private String username;

	@JsonIgnore
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private String password;
	
	@Enumerated(EnumType.STRING)
    private Authority authority;
	
	@Builder
    public Account (String username, String password, Authority authority) {
        this.username = username;
        this.password = password;
        this.authority = authority;
    }
	
	public enum Authority {
	    ROLE_USER, ROLE_ADMIN
	}
	
}
