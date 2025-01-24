package com.project.userorder.controller;

import com.project.userorder.entity.User;
import com.project.userorder.exception.ResourceNotFoundException;
import com.project.userorder.service.UserService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;
	//private String error;
    
    @PostMapping
    public ResponseEntity<?> createUser (@Valid @RequestBody User user) {
        if (user.getName() == null || user.getName().isEmpty()) {
            return ResponseEntity.badRequest().body(new ErrorResponse("Name is required."));
        }
        if (!isValidEmail(user.getEmail())) {
            return ResponseEntity.badRequest().body(new ErrorResponse("Invalid Email format."));
        }
        
        try {
            User createdUser  = userService.createUser (user);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdUser );
        } catch (DataIntegrityViolationException e) {
            if (e.getMessage().contains("users_email_key")) {
                return ResponseEntity.badRequest().body(new ErrorResponse("Email already exists: " + user.getEmail()));
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("error occurred while creating the user."));
        }
    }

    private boolean isValidEmail(String email) {
		// TODO Auto-generated method stub
    	
		return email != null && Pattern.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z]{2,}$",email);
	}

	@GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }
    
    
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        try {
            User user = userService.getUserById(id);
            return ResponseEntity.ok(user);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("User not Found")); 
        }
    }

   

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody User userDetails) {
        try {
            return ResponseEntity.ok(userService.updateUser(id, userDetails));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("User not found")); 
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
    	try {
            userService.deleteUser (id);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("User  not found"));
        }
    }
    
    public static class ErrorResponse{
		private String error;
	
		public ErrorResponse(String error) {
			this.error= error;

		}
		public String getError() {
			return error;
		}
    }

}