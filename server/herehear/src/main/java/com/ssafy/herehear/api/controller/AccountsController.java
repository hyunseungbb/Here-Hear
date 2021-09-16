package com.ssafy.herehear.api.controller;

import com.ssafy.herehear.api.service.AccountsService;
import com.ssafy.herehear.db.entity.Accounts;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus; 
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity; 
import org.springframework.web.bind.annotation.*;

import java.util.List; 
import java.util.Optional;


@RestController 
@RequestMapping("Test")
public class AccountsController {
	@Autowired 
	AccountsService accountsService;

	@GetMapping(value = "/{id}", produces = { MediaType.APPLICATION_JSON_VALUE }) 
	public ResponseEntity<Accounts> getMember(@PathVariable("id") Long id) { 
		Optional<Accounts> user = accountsService.findById(id); 
		return new ResponseEntity<Accounts>(user.get(), HttpStatus.OK); 
	}

}
