package com.lrz.sysbackend.web;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import com.lrz.sysbackend.DTOs.UpdateUserDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lrz.sysbackend.DTOs.SaveUserDTO;
import com.lrz.sysbackend.DTOs.UserDetailsResponseDTO;
import com.lrz.sysbackend.service.UserService;
import com.lrz.sysbackend.web.UserController;
import org.springframework.data.domain.PageImpl;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testSaveUser_Success() throws Exception {
        SaveUserDTO saveUserDTO = new SaveUserDTO();
        saveUserDTO.setName("John Doe");
        saveUserDTO.setEmail("john.doe@example.com");
        saveUserDTO.setPassword("password123");

        UserDetailsResponseDTO responseDTO = new UserDetailsResponseDTO();
        responseDTO.setId(UUID.randomUUID());
        responseDTO.setName("John Doe");

        given(userService.signUp(any(SaveUserDTO.class))).willReturn(responseDTO);

        mockMvc.perform(post("/user/signUp")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(saveUserDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Doe"));
    }

    @Test
    public void testSaveUser_Failure() throws Exception {
        SaveUserDTO saveUserDTO = new SaveUserDTO();
        saveUserDTO.setName("John Doe");
        saveUserDTO.setEmail("john.doe@example.com");
        saveUserDTO.setPassword("password123");

        when(userService.signUp(any(SaveUserDTO.class))).thenThrow(new RuntimeException("Error"));

        mockMvc.perform(post("/user/signUp")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(saveUserDTO)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void testAuth_Success() throws Exception {
        SaveUserDTO saveUserDTO = new SaveUserDTO();
        saveUserDTO.setEmail("john.doe@example.com");
        saveUserDTO.setPassword("password123");

        String jwtToken = "dummy-jwt-token";

        when(userService.signIn(any(SaveUserDTO.class))).thenReturn(jwtToken);

        mockMvc.perform(post("/user/auth")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(saveUserDTO)))
                .andExpect(status().isOk())
                .andExpect(content().string(jwtToken));
    }

    @Test
    public void testAuth_Failure() throws Exception {
        SaveUserDTO saveUserDTO = new SaveUserDTO();
        saveUserDTO.setEmail("john.doe@example.com");
        saveUserDTO.setPassword("password123");

        when(userService.signIn(any(SaveUserDTO.class))).thenThrow(new RuntimeException("Error"));

        mockMvc.perform(post("/user/auth")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(saveUserDTO)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void testGetAllUsers_Success() throws Exception {
        UserDetailsResponseDTO userDTO = new UserDetailsResponseDTO();
        userDTO.setId(UUID.randomUUID());
        userDTO.setName("John Doe");

        Page<UserDetailsResponseDTO> userPage = new PageImpl<>(Arrays.asList(userDTO));

        when(userService.getAllUsers(any(Pageable.class))).thenReturn(userPage);

        mockMvc.perform(get("/user")
                .param("page", "0")
                .param("size", "10")
                .param("direction", "asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("John Doe"));
    }

    @Test
    public void testGetUserById_Success() throws Exception {
        UUID userId = UUID.randomUUID();
        UserDetailsResponseDTO userDTO = new UserDetailsResponseDTO();
        userDTO.setId(userId);
        userDTO.setName("John Doe");

        when(userService.getUserById(userId)).thenReturn(userDTO);

        mockMvc.perform(get("/user/{id}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Doe"));
    }

   

    @Test
    public void testUpdateUser_Success() throws Exception {
        UpdateUserDTO updateUserDTO = new UpdateUserDTO();
        updateUserDTO.setId(UUID.randomUUID());
        updateUserDTO.setName("John Doe");

        UserDetailsResponseDTO responseDTO = new UserDetailsResponseDTO();
        responseDTO.setId(updateUserDTO.getId());
        responseDTO.setName("John Doe");

        when(userService.updateUser(any(UpdateUserDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(put("/user/update")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateUserDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Doe"));
    }

}
