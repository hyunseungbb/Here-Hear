package com.ssafy.herehear.test;

import com.ssafy.herehear.db.entity.Accounts;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorld {
	@GetMapping("/test")
    public String test() {
		
        return "Hello World!";
    }
	
}
