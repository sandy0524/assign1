package com.stackroute.keepnote.controller;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.stackroute.keepnote.exception.UserAlreadyExistException;
import com.stackroute.keepnote.exception.UserNotFoundException;
import com.stackroute.keepnote.model.User;
import com.stackroute.keepnote.service.UserService;
@RestController
public class UserController {
     UserService userService;
@Autowired
	public UserController(UserService userService) {
		this.userService = userService;
	}
@PostMapping("/user/register")
	public ResponseEntity<?> create(@RequestBody User user, HttpSession session) {
		try {
			userService.registerUser(user);
			return new ResponseEntity<String>("Created", HttpStatus.CREATED);
		} catch (UserAlreadyExistException e) {
			return new ResponseEntity<String>("User already exist: " + user.getUserName(), HttpStatus.CONFLICT);
		}
	}
@PutMapping("/user/{id}")
	public ResponseEntity<?> update(@RequestBody User user, @PathVariable String id, HttpSession session) {
		if (session != null && session.getAttribute("loggedInUserId") != null) {
			try {
				User u = userService.updateUser(user, id);
				if (u == null) {
					return new ResponseEntity<String>(user.getUserName() + ": not found", HttpStatus.NOT_FOUND);
				}
				return new ResponseEntity<User>(u, HttpStatus.OK);
			} catch (Exception e) {
				return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
			}
		} else {
			return new ResponseEntity<String>("user id didn't match", HttpStatus.UNAUTHORIZED);
		}
	}
@DeleteMapping("/user/{id}")
	public ResponseEntity<?> delete(@PathVariable String id, HttpSession session) {
		if (session != null && session.getAttribute("loggedInUserId") != null) {
			if (userService.deleteUser(id)) {
				return new ResponseEntity<String>("User deleted", HttpStatus.OK);
			} else {
				return new ResponseEntity<String>("User Not Found", HttpStatus.NOT_FOUND);
			}
		} else {
			return new ResponseEntity<String>("user id didn't match", HttpStatus.UNAUTHORIZED);
		}
	}
@GetMapping("/user/{id}")
	public ResponseEntity<?> getReminderById(@PathVariable String id, HttpSession session) {
		if (session != null && session.getAttribute("loggedInUserId") != null) {
			try {
				User u = userService.getUserById(id);
				if (u == null) {
					return new ResponseEntity<String>("User not found with id " + id, HttpStatus.NOT_FOUND);
				}
				return new ResponseEntity<User>(u, HttpStatus.OK);
			} catch (UserNotFoundException e) {
				return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
			}
		} else {
			return new ResponseEntity<String>("user id didn't match", HttpStatus.UNAUTHORIZED);
		}
	}

}