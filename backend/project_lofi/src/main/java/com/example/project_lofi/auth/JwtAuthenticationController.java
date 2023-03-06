package com.example.project_lofi.auth;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import com.example.project_lofi.user.User;
import com.example.project_lofi.user.UserRepository;

import lombok.extern.slf4j.Slf4j;

@RestController
@CrossOrigin @Slf4j
public class JwtAuthenticationController {

   @Autowired
   private JwtUtil jwtTokenUtil;

   @Autowired
   private JwtUserDetailsService userDetailsService;

   @Autowired
   private UserRepository userRepository;

   @PostMapping("/api/authenticate")
   public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest)
         throws AuthenticationException {
              
         final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
         Optional<User> user = userRepository.findUserByName(userDetails.getUsername());
         final String token = jwtTokenUtil.generateToken(userDetails);
         log.info("A new JWT token is created for " + authenticationRequest.getUsername());
         return ResponseEntity.ok(new JwtResponse(token, user.get()));
   }
}
