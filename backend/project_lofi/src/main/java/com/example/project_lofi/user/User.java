package com.example.project_lofi.user;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import com.example.project_lofi.playlist.Playlist;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Version;
import lombok.Data;

@Entity @Data
public class User implements Serializable{

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private Long userId;

    @Version
    private int userVersion;

    @Basic(optional = false)
    @Column
    private String userPassword;
    
    @Basic(optional = false)
    @Column
    private String userName;

    @Basic(optional = false)
    @Column
    private String userType;

    @Column
    private LocalDateTime userCreated;
    
    @Column
    private LocalDateTime userUpdated;
    
    @JsonIgnore
    @OneToMany(cascade = CascadeType.MERGE, mappedBy = "user")
    private List<Playlist> playlists;
    
}
