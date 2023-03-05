package com.example.project_lofi.playlist;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import com.example.project_lofi.playlistassignment.PlaylistLofiAssignment;
import com.example.project_lofi.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Version;
import lombok.Data;

/**
 * Defines a playlist DTO
 * 
 * @author Gwanjin
 * 
 */
@Entity @Data
public class Playlist implements Serializable {
    
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long playlistId;

    @Version
    private int playlistVersion;

    @Basic(optional = false)
    @Column
    private String playlistName;

    @Basic(optional = true)
    @Column
    private String playlistGenre;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Basic(optional = false)
    @Column
    private String playlistStatus;

    @Basic(optional = false)
    @Column
    private LocalDateTime playlistCreated;

    @Basic(optional = false)
    @Column
    private LocalDateTime playlistUpdated;

    @JsonIgnore
    @OneToMany(mappedBy = "playlist")
    private List<PlaylistLofiAssignment> playlistLofies;
}
