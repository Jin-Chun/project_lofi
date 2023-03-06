package com.example.project_lofi.auth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.project_lofi.user.UserRepository;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class JwtUserDetailsService implements UserDetailsService {

   @Autowired
   private UserRepository userRepository;

   @Override
   public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
      final Optional<com.example.project_lofi.user.User> user = userRepository.findUserByName(username);
      if (user.isPresent()) {
         return new User(user.get().getUserName(), user.get().getUserPassword(),
               new ArrayList<>());
      } else {
         throw new UsernameNotFoundException("User not found with username: " + username);
      }
   }
}