package com.example.project_lofi.auth;

import java.io.Serializable;

import com.example.project_lofi.user.User;


public class JwtResponse implements Serializable {

    private static final long serialVersionUID = 1L;
    private final String jwt;
    private final User user;

    public JwtResponse(String jwt, User user) {
      this.jwt = jwt;
      this.user = user;
    }

    public String getToken() {
      return this.jwt;
    }

    public User getUser(){
      return this.user;
    }

}
