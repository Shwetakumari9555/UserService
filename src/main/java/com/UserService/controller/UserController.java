package com.UserService.controller;
import com.UserService.dtos.RegisterUserRequest;
import com.UserService.dtos.UpdateUserRequest;
import com.UserService.dtos.UserResponse;
import com.UserService.service.UserService;
import jakarta.validation.Valid;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
     public ResponseEntity<UserResponse> registerUser(@Valid @RequestBody RegisterUserRequest request){
         UserResponse user = userService.registerUser(request);
         return new ResponseEntity<>(user, HttpStatus.CREATED);
     }

     @GetMapping("/email")
     public ResponseEntity<UserResponse>findByEmail(@RequestParam String email){
         UserResponse user = userService.findByEmail(email);
         return new ResponseEntity<>(user,HttpStatus.OK);
     }

    @PutMapping("/{userId}")
    public ResponseEntity<UserResponse>updateUser( @PathVariable String userId, @Valid @RequestBody UpdateUserRequest request){
        UserResponse user = userService.updateUser(userId,request);
        return new ResponseEntity<>(user,HttpStatus.OK);
    }

    @PatchMapping("/reset")
    public ResponseEntity<String> resetPassword(@RequestParam String email,@RequestParam String newPassword){
        userService.resetPassword(email,newPassword);
        return new ResponseEntity<>("Password updated successfully", HttpStatus.OK);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteUser(  @PathVariable String userId){
        userService.deleteUser(userId);
        return new ResponseEntity<>("User Deleted successfully", HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse>getUserById(  @PathVariable String userId){
        UserResponse user = userService.getUserById(userId);
        return new ResponseEntity<>(user,HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Iterable<UserResponse>>getAllUser(){
        Iterable<UserResponse> users = userService.getAllUser();
        return new ResponseEntity<>(users,HttpStatus.OK);
    }
}
