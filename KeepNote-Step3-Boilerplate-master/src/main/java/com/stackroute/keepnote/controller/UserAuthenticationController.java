package com.stackroute.keepnote.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.stackroute.keepnote.exception.UserNotFoundException;
import com.stackroute.keepnote.model.User;
import com.stackroute.keepnote.service.UserService;


@RestController
@SessionAttributes
public class UserAuthenticationController {

	UserService userService;

	@Autowired
	public UserAuthenticationController(UserService userService) {
		this.userService= userService;
	}

	
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody User user, HttpSession session){
		try {
			if(userService.validateUser(user.getUserId(), user.getUserPassword())) {
				session.setAttribute("loggedInUserId", user.getUserId());
				return new ResponseEntity<String>("login successfull", HttpStatus.OK);
			}else {
				return new ResponseEntity<String>("login unsuccessfull", HttpStatus.UNAUTHORIZED);
			}
		} catch (UserNotFoundException e) {
			return new ResponseEntity<String>("login unsuccessfull", HttpStatus.UNAUTHORIZED);
		}
	}

	
	@GetMapping("/logout")
	public ResponseEntity<?> logout(HttpSession session){
		if(session!=null && session.getAttribute("loggedInUserId")!=null) {
			session.invalidate();
			return new ResponseEntity<String>("logged out successfully", HttpStatus.OK);
		}else {
			return new ResponseEntity<String>("log out failed", HttpStatus.BAD_REQUEST);
		}	
	}

	
	
}
