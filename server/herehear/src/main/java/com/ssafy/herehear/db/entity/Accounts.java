package com.ssafy.herehear.db.entity;

// import javax.persistence.CascadeType;
import javax.persistence.Entity;
// import javax.persistence.FetchType;
// import javax.persistence.JoinColumn;
// import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

/**
 * 유저 모델 정의.
 */
//Entity가 붙은 클래스는 JPA가 관리하는 클래스이고, 테이블과 매핑할 테이블에 해당 어노테이션을 붙인다.
@Entity
@Getter
@Setter
public class Accounts extends BaseEntity {
	private String username;

	@JsonIgnore
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private String password;
	
}