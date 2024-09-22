/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lrz.sysbackend.service;

import com.lrz.sysbackend.DTOs.SaveUserDTO;
import com.lrz.sysbackend.DTOs.UpdateUserDTO;
import com.lrz.sysbackend.DTOs.UserDetailsResponseDTO;
import com.lrz.sysbackend.exceptions.InvalidCredentialsException;
import com.lrz.sysbackend.exceptions.UserNotFoundException;
import com.lrz.sysbackend.models.User;
import com.lrz.sysbackend.repository.UserRepository;
import java.util.Optional;
import java.util.UUID;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 *
 * @author lara
 */
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Test
    public void testSignUp_EmailAlreadyInUse() {
        SaveUserDTO user = new SaveUserDTO("Test User", "test@example.com", "password");
        when(userRepository.existsByEmail(user.getEmail())).thenReturn(true);

        assertThrows(InvalidCredentialsException.class, () -> {
            userService.signUp(user);
        });
    }

    @Test
    public void testSignUp_Success() {
        SaveUserDTO user = new SaveUserDTO("Test User", "test@example.com", "password");
        when(userRepository.existsByEmail(user.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(user.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UserDetailsResponseDTO response = userService.signUp(user);

        assertNotNull(response);
        assertEquals("test@example.com", response.getEmail());
        assertEquals("Test User", response.getName());
    }

    @Test
    public void testSignIn_Success() {
        SaveUserDTO userDto = new SaveUserDTO("Test User", "test@example.com", "password");
        User user = new User();

        user.setEmail(userDto.getEmail());
        user.setPassword("encodedPassword");

        when(userRepository.findByEmail(userDto.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(userDto.getPassword(), user.getPassword())).thenReturn(true);

        when(jwtService.generateToken(user)).thenReturn("jwtToken");

        String token = userService.signIn(userDto);

        assertEquals("jwtToken", token);
    }

    @Test
    public void testSignIn_EmailNotFound() {
        SaveUserDTO userDto = new SaveUserDTO("Test User", "test@example.com", "password");

        when(userRepository.findByEmail(userDto.getEmail())).thenReturn(Optional.empty());

        assertThrows(InvalidCredentialsException.class, () -> {
            userService.signIn(userDto);
        });
    }

    @Test
    public void testSignIn_IncorrectPassword() {
        SaveUserDTO userDto = new SaveUserDTO("Test User", "test@example.com", "password");
        User user = new User();

        user.setEmail(userDto.getEmail());
        user.setPassword("encodedPassword");

        when(userRepository.findByEmail(userDto.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(userDto.getPassword(), user.getPassword())).thenReturn(false);

        assertThrows(InvalidCredentialsException.class, () -> {
            userService.signIn(userDto);
        });        
    }
    
    @Test
     public void testGetAllUsers_Success() {
        Pageable pageable = PageRequest.of(0, 10);


        User user1 = new User();
        user1.setId(UUID.randomUUID());
        user1.setEmail("user1@example.com");
        user1.setName("User One");

        User user2 = new User();
        user2.setId(UUID.randomUUID());
        user2.setEmail("user2@example.com");
        user2.setName("User Two");


        Page<User> userPage = new PageImpl<>(Arrays.asList(user1, user2), pageable, 2);
        when(userRepository.findAll(pageable)).thenReturn(userPage);

        Page<UserDetailsResponseDTO> result = userService.getAllUsers(pageable);

        assertEquals(2, result.getTotalElements());
        List<UserDetailsResponseDTO> users = result.getContent();
        assertEquals("user1@example.com", users.get(0).getEmail());
        assertEquals("User One", users.get(0).getName());
        assertEquals("user2@example.com", users.get(1).getEmail());
        assertEquals("User Two", users.get(1).getName());
    }
     
    @Test
    public void testGetUserById_Success() {
        UUID userId = UUID.randomUUID();
        User user = new User();
        user.setId(userId);
        user.setEmail("user@example.com");
        user.setName("Test User");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        UserDetailsResponseDTO result = userService.getUserById(userId);

        assertEquals(userId, result.getId());
        assertEquals("user@example.com", result.getEmail());
        assertEquals("Test User", result.getName());
    }
    
    @Test
    public void testGetUserById_UserNotFound() {
        UUID userId = UUID.randomUUID();

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            userService.getUserById(userId);
        });
    }
    
    @Test
    public void testUpdateUser_Success() {
        UUID userId = UUID.randomUUID();

        User existingUser = new User();
        existingUser.setId(userId);
        existingUser.setEmail("olduser@example.com");
        existingUser.setName("Old User");
        existingUser.setPassword("oldpassword");

        UpdateUserDTO updateInfo = new UpdateUserDTO(userId,"New User",  "newuser@example.com", "newpassword");

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(passwordEncoder.encode(updateInfo.getPassword())).thenReturn("encodedpassword");
        UserDetailsResponseDTO result = userService.updateUser(updateInfo);

        assertEquals(userId, result.getId());
        assertEquals("newuser@example.com", result.getEmail());
        assertEquals("New User", result.getName());

        assertEquals("newuser@example.com", existingUser.getEmail());
        assertEquals("New User", existingUser.getName());
        assertEquals("encodedpassword", existingUser.getPassword());
        verify(userRepository).findById(userId);
    }
    
    @Test
    public void testUpdateUser_UserNotFound() {
        UUID userId = UUID.randomUUID();

        UpdateUserDTO updateInfo = new UpdateUserDTO(userId, "New User","newuser@example.com",  "newpassword");

        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        
        assertThrows(UserNotFoundException.class, () -> {
            userService.updateUser(updateInfo);
        });
    }
}
