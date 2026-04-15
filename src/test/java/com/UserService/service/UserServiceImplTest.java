package com.UserService.service;

import com.UserService.UserServiceImpl;
import com.UserService.dtos.RegisterUserRequest;
import com.UserService.dtos.UpdateUserRequest;
import com.UserService.dtos.UserResponse;
import com.UserService.entities.Gender;
import com.UserService.entities.User;
import com.UserService.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
public class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    // ✅ SUCCESS: Register user
    @Test
    void registerUser_success() {

        RegisterUserRequest request = new RegisterUserRequest(
                "Test User",
                "testing@gmail.com",
                "1234",
                "1234567890",
                "Test Location",
                 Gender.MALE
        );


        User savedUser = new User();
        savedUser.setEmail("test@gmail.com");
        savedUser.setPassword("encoded");

        when(passwordEncoder.encode("1234")).thenReturn("encoded");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        UserResponse response = userService.registerUser(request);

        assertNotNull(response);
        assertEquals("test@gmail.com", response.email());

        verify(userRepository, times(1)).save(any(User.class));
    }

    // ✅ FAILURE: User not found
    @Test
    void findByEmail_notFound() {

        when(userRepository.findByEmail("abc@gmail.com"))
                .thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> userService.findByEmail("abc@gmail.com"));
        System.out.println(ex.getMessage());
        assertTrue(ex.getMessage().contains("User Not Found"));
    }

    // ✅ SUCCESS: Find user
    @Test
    void findByEmail_success() {

        User user = new User();
        user.setEmail("test@gmail.com");

        when(userRepository.findByEmail("test@gmail.com"))
                .thenReturn(Optional.of(user));

        UserResponse response = userService.findByEmail("test@gmail.com");

        assertEquals("test@gmail.com", response.email());
    }

    // ✅ EDGE CASE: Null password
    @Test
    void registerUser_nullPassword() {

        RegisterUserRequest request = new RegisterUserRequest(
                "Test User",
                "testing@gmail.com",
                null,
                "1234567890",
                "Test Location",
                Gender.MALE
        );

        when(passwordEncoder.encode(null)).thenReturn(null);
        when(userRepository.save(any(User.class))).thenReturn(new User());

        UserResponse response = userService.registerUser(request);

        assertNotNull(response);
    }

    @Test
    void registerUser_duplicateEmail() {

        RegisterUserRequest request = new RegisterUserRequest(
                "Test User",
                "testing@gmail.com",
                "1234",
                "1234567890",
                "Test Location",
                Gender.MALE
        );

        when(userRepository.findByEmail("test@gmail.com"))
                .thenThrow(RuntimeException.class);

        assertThrows(RuntimeException.class,
                () -> userService.registerUser(request));
    }

    @Test
    void updateUser_success() {
        UUID uuid = UUID.randomUUID();

        User user = new User();
        user.setUserId(uuid);

        UpdateUserRequest request = new UpdateUserRequest(UUID.randomUUID(), "Test1", "some_path", "8809177555", "Bangalore");

        when(userRepository.findById(uuid)).thenReturn(Optional.of(user));
        when(userRepository.save(any())).thenReturn(user);

        UserResponse response = userService.updateUser(uuid.toString(), request);

        assertNotNull(response);
        assertEquals("Test1", user.getName());
        assertEquals("8809177555", user.getPhone());
        assertEquals("Bangalore", user.getLocation());
    }

    @Test
    void updateUser_notFound() {
        UUID uuid = UUID.randomUUID();

        when(userRepository.findById(uuid)).thenReturn(Optional.empty());

        UpdateUserRequest request = new UpdateUserRequest(UUID.randomUUID(), "User1", "xyz", "1234567890","New york");

        assertThrows(RuntimeException.class,
                () -> userService.updateUser(uuid.toString(), request));
    }

    @Test
    void updateUser_invalidUUID() {
        UpdateUserRequest request = new UpdateUserRequest(UUID.randomUUID(), "User1", "some_path", "9912455677","Hyderabad");

        assertThrows(IllegalArgumentException.class,
                () -> userService.updateUser("invalid-uuid", request));
    }

    @Test
    void resetPassword_success() {
        User user = new User();
        user.setEmail("test@gmail.com");

        when(userRepository.findByEmail("test@gmail.com"))
                .thenReturn(Optional.of(user));
        when(passwordEncoder.encode("newPass")).thenReturn("encoded");

        userService.resetPassword("test@gmail.com", "newPass");

        assertEquals("encoded", user.getPassword());
        verify(userRepository).save(user);
    }

    @Test
    void resetPassword_notFound() {
        when(userRepository.findByEmail("abc@gmail.com"))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> userService.resetPassword("abc@gmail.com", "1234"));
    }


    @Test
    void deleteUser_success() {
        UUID uuid = UUID.randomUUID();

        User user = new User();
        user.setUserId(uuid);
        user.setEnabled(true);

        when(userRepository.findById(uuid)).thenReturn(Optional.of(user));

        userService.deleteUser(uuid.toString());

        assertFalse(user.isEnabled());
        verify(userRepository).save(user);
    }

    @Test
    void deleteUser_notFound() {
        UUID uuid = UUID.randomUUID();

        when(userRepository.findById(uuid)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> userService.deleteUser(uuid.toString()));
    }

    @Test
    void getUserById_success() {
        UUID uuid = UUID.randomUUID();

        User user = new User();
        user.setUserId(uuid);
        user.setEmail("test@gmail.com");

        when(userRepository.findById(uuid)).thenReturn(Optional.of(user));

        UserResponse response = userService.getUserById(uuid.toString());

        assertEquals("test@gmail.com", response.email());
    }

    @Test
    void getUserById_notFound() {
        UUID uuid = UUID.randomUUID();

        when(userRepository.findById(uuid)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> userService.getUserById(uuid.toString()));
    }


    @Test
    void getAllUser_success() {
        User u1 = new User();
        u1.setEmail("a@gmail.com");

        User u2 = new User();
        u2.setEmail("b@gmail.com");

        when(userRepository.findAll()).thenReturn(List.of(u1, u2));

        Iterable<UserResponse> responses = userService.getAllUser();

        List<UserResponse> list = new ArrayList<>();
        responses.forEach(list::add);

        assertEquals(2, list.size());
    }

    @Test
    void getAllUser_empty() {
        when(userRepository.findAll()).thenReturn(List.of());

        Iterable<UserResponse> responses = userService.getAllUser();

        List<UserResponse> list = new ArrayList<>();
        responses.forEach(list::add);

        assertTrue(list.isEmpty());
    }

    @Test
    void registerUser_shouldThrowException_whenEmailAlreadyExists() {

        RegisterUserRequest request = new RegisterUserRequest(
                "Test User",
                "testing@gmail.com",
                "1234",
                "1234567890",
                "Test Location",
                Gender.MALE
        );

        User existingUser = new User();
        existingUser.setEmail("testing@gmail.com");

        when(userRepository.findByEmail(anyString()))
                .thenReturn(Optional.of(existingUser)); // 🔥 triggers ifPresent

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> userService.registerUser(request));
        System.out.println(ex.getMessage());
        assertEquals("User Email Id Already Exists ", ex.getMessage());
        verify(userRepository, never()).save(any());
    }
}
