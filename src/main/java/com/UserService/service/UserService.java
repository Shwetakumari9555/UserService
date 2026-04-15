package com.UserService.service;

import com.UserService.dtos.RegisterUserRequest;
import com.UserService.dtos.UpdateUserRequest;
import com.UserService.dtos.UserResponse;


public interface UserService{

	UserResponse registerUser(RegisterUserRequest request);
	UserResponse findByEmail(String emailId);
	UserResponse updateUser(String userId, UpdateUserRequest request);
	void resetPassword(String email, String newPassword);
	void deleteUser(String userId);
	UserResponse getUserById(String userId);
	Iterable<UserResponse> getAllUser();
		
	
	
}
