package com.team11.userservice.userservice;

import com.team11.jwtutil.JwtUtil;
import com.team11.userservice.controller.AuthController;
import com.team11.userservice.entity.User;
import com.team11.userservice.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.util.Pair;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Set;

@MockitoSettings(strictness = Strictness.LENIENT)
@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {

    @Mock
    private AuthService authService;

    @MockBean
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthController authController; // Injecting the controller

    private MockMvc mockMvc; 

    @BeforeEach
    public void setUp() {
        // Set up MockMvc after all mocks are injected
        this.mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
        
    }

    @Test
    public void testRegister_Success() throws Exception {
       
        User mockUser = new User("user1", "user@example.com", "password123", Set.of("USER"));

        // Mock the behavior of the authService.registerUser() method
        when(authService.registerUser("user1", "user@example.com", "password123")).thenReturn(mockUser);

        // Perform the POST request to the /register endpoint with parameters
        mockMvc.perform(post("/api/auth/register")
                .param("username", "user1")
                .param("email", "user@example.com")
                .param("password", "password123"))
                .andExpect(status().isOk());  // Expect HTTP 200

        // Verify that the register method was called exactly once with the correct parameters
        verify(authService, times(1)).registerUser("user1", "user@example.com", "password123");

    }


    @Test
    public void testLogin_Success() throws Exception {
        //access token mock
        String mockJwtToken = "mocked-jwt-token";

        //Mock the behavior of the authService.loginUser() method
        when(authService.loginUser("user1", "password123"))
                .thenReturn(Pair.of(mockJwtToken, "mocked-refresh-token"));

        //POST request to the /login endpoint with parameters
        mockMvc.perform(post("/api/auth/login")
                .param("username", "user1")
                .param("password", "password123"))
                .andExpect(status().isOk())  // Expect HTTP 200
                .andExpect(jsonPath("$.accessToken").value(mockJwtToken));  // Verify JWT token in response
                
        // Verify that the loginUser method was called exactly once with the correct parameters
        verify(authService, times(1)).loginUser("user1", "password123");
    }

    @Test
    public void testLogin_Failure_InvalidCredentials() throws Exception {
        // Mock login failure (null return value when credentials are invalid)
        when(authService.loginUser("user1", "wrongpassword")).thenReturn(null);

        // Perform the POST request to the /login endpoint with parameters
        mockMvc.perform(post("/api/auth/login")
                .param("username", "user1")
                .param("password", "wrongpassword"))
                .andExpect(status().isUnauthorized());  // Expect HTTP 401 (Unauthorized)
    }

}
