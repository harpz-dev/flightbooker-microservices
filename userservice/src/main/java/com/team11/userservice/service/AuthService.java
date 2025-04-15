package com.team11.userservice.service;

import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.util.Pair;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.team11.userservice.entity.User;
import com.team11.userservice.repository.UserRepository;
import com.team11.userservice.exceptions.*;
import com.team11.jwtutil.AccessToken;
import com.team11.jwtutil.JwtUtil;
import com.team11.jwtutil.RefreshToken;
import com.team11.jwtutil.TokenType;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    private Environment env;

    @Autowired
    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, Environment env) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.env = env;
    }

    public JwtUtil getJwtUtil(TokenType tokenType) {
        return new JwtUtil(tokenType);
    }


    public User registerUser(String username, String email, String password) {
        // Check for duplicate username
        if (userRepository.existsByUsername(username)) {
            throw new DuplicateUsernameException("Username already exists.");
        }

        // Check for duplicate email
        if (userRepository.existsByEmail(email)) {
            throw new DuplicateEmailException("Email already exists.");
        }

        String hashedPassword = passwordEncoder.encode(password);
        User user = new User(username, email, hashedPassword, Set.of("USER"));
        return userRepository.save(user);
    }
    
    //returns a pair containing the access and refresh JWT tokens for a user
    public Pair<String, String> loginUser(String username, String password) {

        JwtUtil accessJwtUtil = getJwtUtil(new AccessToken(env));
        JwtUtil refreshJwtUtil = getJwtUtil(new RefreshToken(env));

        // Find the user by username
        Optional<User> optionalUser = userRepository.findByUsername(username);
        
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            // Compare the provided password with the stored hashed password
            if (passwordEncoder.matches(password, user.getPassword())) {
                return Pair.of(accessJwtUtil.generateToken(String.valueOf(user.getId())), refreshJwtUtil.generateToken(String.valueOf(user.getId())));  // If passwords match, return the JWT token
            }
        }
        return null; // Return null if no user found or password doesn't match
    }

    public String refreshToken(String refreshToken) {
        JwtUtil accessJwtUtil = getJwtUtil(new AccessToken(env));
        JwtUtil refreshJwtUtil = getJwtUtil(new RefreshToken(env));

        // Check if the refresh token is valid
        if (refreshJwtUtil.validateToken(refreshToken)) {
            String username = refreshJwtUtil.extractUserIdString(refreshToken);
            return accessJwtUtil.generateToken(username);
        }
        return null;
    }

    public void validateLogout(String refreshToken){
        JwtUtil refreshJwtUtil = getJwtUtil(new RefreshToken(env));
        if (!refreshJwtUtil.validateToken(refreshToken)) {
            throw new AlreadyLoggedOutException("User is already logged out.");
        }

    }
    //needed to construct a cookie in authcontroller
    public int getRefreshTokenExpiry(){
        return (int)new RefreshToken(env).getExpirationTime()/1000;
    }

    public String getEmailByUserId(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        return userOptional.get().getEmail();
    }

}