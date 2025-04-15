package com.team11.userservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.team11.jwtutil.RefreshToken;
import com.team11.userservice.entity.User;
import com.team11.userservice.exceptions.DuplicateEmailException;
import com.team11.userservice.exceptions.DuplicateUsernameException;
import com.team11.userservice.json.LoginResponse;
import com.team11.userservice.service.AuthService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @Autowired
    private Environment env;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestParam String username, @RequestParam String email, @RequestParam String password) {
      try {
            User user = authService.registerUser(username, email, password);
            return ResponseEntity.ok("User registered successfully: " + user.getUsername());
        } catch (DuplicateUsernameException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: Username already exists.");
        } catch (DuplicateEmailException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: Email already exists.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: Unable to register user.");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestParam String username, @RequestParam String password, HttpServletResponse response) {
        // Find user by username
        Pair<String, String> tokenPair = authService.loginUser(username, password);
        
        if (tokenPair != null) {
            //If login is successful, add the refresh token to response cookie and the access token to the response body (as JSON)
            
        String refreshToken = tokenPair.getSecond();

        String refreshCookieString= ResponseCookie.from("refreshCookie", refreshToken)
            .maxAge(authService.getRefreshTokenExpiry())
            .path("/")
            .httpOnly(true)
            .secure(false)
            .domain("localhost")
            .build()
            .toString();

        String accessToken = tokenPair.getFirst();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, refreshCookieString).body(new LoginResponse(accessToken, "Login successful"));
        } else {
            // If login fails, return an unauthorized response
            return ResponseEntity.status(401).body(new LoginResponse(null, "Invalid username or password"));
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponse> refresh(HttpServletRequest request) {
        // Refresh the access token using the refresh token
        System.out.println("Attempting to refresh token");
        String refreshToken= getRefreshTokenFromRequest(request);
        String newAccessToken = authService.refreshToken(refreshToken); //validate and do stuff
        if (newAccessToken != null) {
            //If refresh is successful, add the new access token to the response body (as JSON)
            System.out.println("Refresh token successfully validated");

            return ResponseEntity.ok(new LoginResponse(newAccessToken, "Refresh successful"));
        } else {
            // If refresh fails, return an unauthorized response
            return ResponseEntity.status(401).body(new LoginResponse(null, "Invalid session, please sign in again"));
        }
    }

    private String getRefreshTokenFromRequest(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if ("refreshCookie".equals(cookie.getName())) {
                System.out.println("Refresh token found");
                return cookie.getValue();
            }
        }
        return null;
    }

    @PostMapping("/logout")
    public ResponseEntity<LoginResponse> logout(HttpServletRequest request, HttpServletResponse response) {

        // validate the refresh token to see if user is even logged in
        String refreshToken= getRefreshTokenFromRequest(request);
        
        try{
            //check if user is already logged out (checking refresh token)
            authService.validateLogout(refreshToken);
            // Clear the refresh token cookie
            ResponseCookie responseCookie = ResponseCookie.from("refreshCookie", "")
                .maxAge(0)
                .path("/")
                .httpOnly(true)
                .secure(false)
                .domain("localhost")
                .build();
            
            response.addHeader(HttpHeaders.SET_COOKIE, responseCookie.toString());
            return ResponseEntity.ok(new LoginResponse(null, "Logout successful"));
        } 
        catch (Exception e) {
            return ResponseEntity.status(401).body(new LoginResponse(null, "Invalid session, please sign in again"));
        }
    }

    @GetMapping("/get-email")
    public ResponseEntity<String> getEmailByUserId(@RequestParam Long userId) {
        try {
            String email = authService.getEmailByUserId(userId);
            return ResponseEntity.ok(email);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
    }
}
    