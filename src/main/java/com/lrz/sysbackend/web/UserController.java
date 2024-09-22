/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lrz.sysbackend.web;

import com.lrz.sysbackend.DTOs.SaveUserDTO;
import com.lrz.sysbackend.DTOs.UpdateUserDTO;
import com.lrz.sysbackend.DTOs.UserDetailsResponseDTO;
import com.lrz.sysbackend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

/**
 *
 * @author lara
 */

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Tag(name = "Users", description = "Endpoints for Managing Users")
public class UserController {

    private final UserService userService;

    @PostMapping("/signUp")
    @Operation(
            summary = "Create a new User", 
            description = "Receives a DTO with name, email, and password and saves it to the Database", 
            tags = {"Users"},
            responses = {
                @ApiResponse(
                    responseCode = "201", 
                    description = "User Created", 
                    content = @Content(schema = @Schema(implementation = UserDetailsResponseDTO.class))
                ),
                @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
                @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
            }
    )
    public ResponseEntity<UserDetailsResponseDTO> saveUser(@RequestBody SaveUserDTO user) {
        return ResponseEntity.ok(this.userService.signUp(user));
    }

    @PostMapping("/auth")
    @Operation(
            summary = "Authenticate a User",
            description = "Authenticates a user by verifying email and password and returns a JWT token",
            tags = {"Users"},
            responses = {
                @ApiResponse(responseCode = "200", description = "User Authenticated", content = @Content),
                @ApiResponse(responseCode = "400", description = "Invalid Credentials", content = @Content),
                @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
            }
    )
    public ResponseEntity<String> auth(@RequestBody SaveUserDTO user) {
        return ResponseEntity.ok(this.userService.signIn(user));
    }

    @GetMapping
    @Operation(
            summary = "Get all Users",
            description = "Returns a paginated list of all users",
            tags = {"Users"},
            responses = {
                @ApiResponse(responseCode = "200", description = "Users Retrieved", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Page.class))
                }),
                @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
            }
    )
    public ResponseEntity<Page<UserDetailsResponseDTO>> getAllUsers(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size,
            @RequestParam(value = "direction", defaultValue = "asc") String direction) {
        
        var sortDirection = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, "name"));
        return ResponseEntity.ok(this.userService.getAllUsers(pageable));
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get a User by ID",
            description = "Fetches a user's details by their UUID",
            tags = {"Users"},
            responses = {
                @ApiResponse(responseCode = "200", description = "User Found", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = UserDetailsResponseDTO.class))
                }),
                @ApiResponse(responseCode = "404", description = "User Not Found", content = @Content),
                @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
            }
    )
    public ResponseEntity<UserDetailsResponseDTO> getUserById(@PathVariable UUID id) {
        return ResponseEntity.ok(this.userService.getUserById(id));
    }

    @PutMapping("/update")
    @Operation(
            summary = "Update a User",
            description = "Updates the details of an existing user",
            tags = {"Users"},
            responses = {
                @ApiResponse(responseCode = "200", description = "User Updated", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = UserDetailsResponseDTO.class))
                }),
                @ApiResponse(responseCode = "404", description = "User Not Found", content = @Content),
                @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
                @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
            }
    )
    public ResponseEntity<UserDetailsResponseDTO> updateUser(@RequestBody UpdateUserDTO info) {
        return ResponseEntity.ok(this.userService.updateUser(info));
    }

}

