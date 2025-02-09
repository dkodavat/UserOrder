package com.project.userorder.service;

import com.project.userorder.entity.User;
import com.project.userorder.exception.ResourceNotFoundException;
import com.project.userorder.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    
    public User createUser(User user) {
    	try {
            return userRepository.save(user);
    	}catch(DataIntegrityViolationException e) {
    		if(e.getMessage().contains("users_email_key")){
    			throw new RuntimeException("email aldready exists");
    		}
    		throw e;
    	}
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User does not exist")); 
    }




    public User updateUser(Long id, User userDetails) {
        return userRepository.findById(id).map(user -> {
            user.setName(userDetails.getName());
            user.setEmail(userDetails.getEmail());
            user.setMobile(userDetails.getMobile());
            user.setAddress(userDetails.getAddress());
            return userRepository.save(user);
        }).orElseThrow(() -> new RuntimeException("User not found"));
    }
    
    

    public void deleteUser(Long id) {
    	 User user = userRepository.findById(id)
                 .orElseThrow(() -> new ResourceNotFoundException("User does not exist"));
         userRepository.delete(user);    }
}
