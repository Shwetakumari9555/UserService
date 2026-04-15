package com.UserService.service;

import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.UserService.dtos.RegisterUserRequest;
import com.UserService.dtos.UpdateUserRequest;
import com.UserService.dtos.UserResponse;
import com.UserService.entities.Provider;
import com.UserService.entities.Role;
import com.UserService.entities.User;
import com.UserService.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
	
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	

	
	public UserResponse mapToResponse(User user) {
	    return UserResponse.builder()
	            .userId(user.getUserId())
	            .name(user.getName())
	            .email(user.getEmail())
	            .profilePic(user.getProfilePic())
	            .phone(user.getPhone())
	            .location(user.getLocation())
	            .enabled(user.isEnabled())
	            .gender(user.getGender())
	            .provider(user.getProvider())
	            .role(user.getRole())
	            .build();
	}
	
	@Override
	public UserResponse registerUser(RegisterUserRequest request) {
		userRepository.findByEmail(request.email()).ifPresent(user-> {
			throw new RuntimeException("User Email Id Already Exists ");
		});
		
		User createUser = User.builder()
					.name(request.name())
					.email(request.email())
					.password(passwordEncoder.encode(request.password()))
					.provider(Provider.LOCAL)
					.role(Role.CANDIDATE)
					.phone(request.phone())
					.location(request.location())
					.enabled(true)
					.gender(request.gender())
					.build();
		
		User savedUser = userRepository.save(createUser);
		return mapToResponse(savedUser);
		
					
		
	}

	@Override
	public UserResponse findByEmail(String emailId) {
	User user = 	userRepository.findByEmail(emailId).orElseThrow(()-> new RuntimeException("User Not Found"));
		return mapToResponse(user);
	}

	@Override
	public UserResponse updateUser(String userId,UpdateUserRequest request) {
		UUID uuid = UUID.fromString(userId);
		User user = 	userRepository.findById(uuid).orElseThrow(()-> new RuntimeException("User Not Found"));
		user.setName(request.name());
		user.setPhone(request.phone());
		user.setLocation(request.location());
		
		User updatedUser = userRepository.save(user);
		return mapToResponse(updatedUser);
	}

	@Override
	public void resetPassword(String emailId, String newPassword) {
		User user = 	userRepository.findByEmail(emailId).orElseThrow(()-> new RuntimeException("User Not Found"));
		
		user.setPassword(passwordEncoder.encode(newPassword));
		userRepository.save(user);
	}

	@Override
	public void deleteUser(String userId) {
		UUID uuid = UUID.fromString(userId);
		User user = userRepository.findById(uuid)
			    .orElseThrow(() -> new RuntimeException("User not found"));
		user.setEnabled(false);
		userRepository.save(user);
		
	}

	@Override
	public UserResponse getUserById(String userId) {
		UUID uuid = UUID.fromString(userId);
		User user = 	userRepository.findById(uuid).orElseThrow(()-> new RuntimeException("User Not Found"));
		return mapToResponse(user);
	}

	@Override
	public Iterable<UserResponse> getAllUser() {
	    return userRepository.findAll()
	            .stream()
	            .map(this::mapToResponse)
	            .toList();
	}
}
