
**Curl Commands**

1) Register User:

`curl.exe -X POST http://localhost:8083/api/auth/register -d "username=john_doe&email=john.doe@example.com&password=securepass" -H "Content-Type: application/x-www-form-urlencoded"`

  

2) Login User:

`curl.exe -X POST http://localhost:8083/api/auth/login -d "username=john_doe&password=securepass" -H "Content-Type: application/x-www-form-urlencoded"`



**How to add authentication to other microservices (booking service):**

Note: The secret key is stored as an environment variable (look at docker compose file). There is a file called ".env" that stores the key as an environment variable so you need to import this .env file to every microservice that needs access to the secret key. Look at the dockercompose file's userservice section to see how to do that.

  
Steps:

1. Add Dependencies in Other Services

In the build.gradle file of the service that needs to authenticate users, include the following dependencies:

  
    implementation 'org.springframework.boot:spring-boot-starter-security'
    implementation 'io.jsonwebtoken:jjwt-api:0.12.6'
    implementation 'io.jsonwebtoken:jjwt-impl:0.12.6'
    implementation 'io.jsonwebtoken:jjwt-jackson:0.12.6'

  

Just include the jwutil class as a dependency in the build.gradle file as well

    implementation project(':jwtutil')

Autowire the constructor of your service class and pass the Environment env (as a parameter to the constructor). Then pass env in while constructing the JwtUtil object. (Make sure you added the .env file to the microservice in the dockercompose {as mentioned in the note above}).

  
2. Create JWT Filter

In each service, create a filter to intercept requests and check the JWT token for authentication. Here's an example of a filter:

    package com.team11.someothermicroservice.security;
    
    import com.team11.userservice.util.JwtUtil;
    import io.jsonwebtoken.JwtException;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.security.core.context.SecurityContextHolder;
    import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
    import org.springframework.stereotype.Component;
    import org.springframework.web.filter.OncePerRequestFilter;
    
    import javax.servlet.Filter;
    import javax.servlet.FilterChain;
    import javax.servlet.FilterConfig;
    import javax.servlet.ServletException;
    import javax.servlet.annotation.WebFilter;
    import javax.servlet.http.HttpServletRequest;
    import javax.servlet.http.HttpServletResponse;
    import java.io.IOException;
    
    @Component
    @WebFilter(urlPatterns = "/*") // Adjust this to limit the filter to specific URLs if needed
    public class JwtFilter extends OncePerRequestFilter {
    
        @Autowired
        private JwtUtil jwtUtil;
    
        @Override
        protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
            String token = extractToken(request);
            if (token != null && jwtUtil.validateToken(token)) {
                String username = jwtUtil.extractUsername(token);
                // Set the user in the security context for later use in the service layer
                SecurityContextHolder.getContext().setAuthentication(
                        new UsernamePasswordAuthenticationToken(username, null, new ArrayList<>())
                );
            }
            chain.doFilter(request, response);
        }
    
        private String extractToken(HttpServletRequest request) {
            String header = request.getHeader("Authorization");
            if (header != null && header.startsWith("Bearer ")) {
                return header.substring(7); // Remove "Bearer " prefix
            }
            return null;
        }
    
        @Override
        public void init(FilterConfig filterConfig) throws ServletException {
        }
    
        @Override
        public void destroy() {
        }
    }

  
  

3. Enable Filter in Configuration

In each microservice, you need to register this filter as part of the Spring Security configuration:

      package com.team11.someothermicroservice.config;
    
    import com.team11.someothermicroservice.security.JwtFilter;
    import org.springframework.context.annotation.Bean;
    import org.springframework.context.annotation.Configuration;
    import org.springframework.security.config.annotation.web.builders.HttpSecurity;
    import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
    import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
    
    @Configuration
    @EnableWebSecurity
    public class SecurityConfig extends WebSecurityConfigurerAdapter {
    
        @Autowired
        private JwtFilter jwtFilter;
    
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/public/**").permitAll() // Allow open endpoints
                .anyRequest().authenticated() // Require JWT authentication for all other endpoints
                .and()
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class); // Add JWT filter
        }
    }



4. (MAYBE SKIP THIS STEP FOR NOW) Handling the JWT Token in API Requests (don't think we need this since as we discussed, our services only talk to each via kafka + they don't need to pass the jwt around)

In each service, when making API calls (e.g., from one microservice to another), you will need to add the JWT token to the Authorization header in the request. You can retrieve the token from the user’s session or from an already authenticated context.

  

Example using RestTemplate:

  
  

    HttpHeaders headers = new HttpHeaders();    
    headers.set("Authorization", "Bearer " + jwtToken); // Add the token
    HttpEntity<String> entity = new HttpEntity<>(headers);
    
    ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

  

5. Using Curl/Postman to make requests and passing the Jwt token in

  

When you're testing your API endpoints (e.g., for UserService or other services) using tools like cURL or Postman, you need to add the JWT token to the Authorization header of your request. You can send the JWT token as a Bearer token in the header:

  

    curl.exe -X GET http://localhost:port/your-api-endpoint -H "Authorization: Bearer <your-jwt-token>"

  

6. Testing JWT Authentication

Now, when a user logs in to the UserService and receives the JWT token, they can use that token for subsequent requests to other services. The JWT filter in each service will validate the token and provide the user's information (e.g., username) in the security context. You can use the methods in JwtUtil to extract the username etc. etc.

