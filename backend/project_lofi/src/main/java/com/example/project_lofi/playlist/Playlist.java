package com.example.project_lofi.playlist;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import com.example.project_lofi.lofi.Lofi;
import com.example.project_lofi.user.User;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Version;
import lombok.Data;

@Entity @Data
public class Playlist implements Serializable {
    
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long playlistId;

    @Version
    private int playlistVersion;

    @Basic(optional = false)
    @Column
    private String playlistName;

    @Basic(optional = true)
    @Column
    private String playlistGenre;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Basic(optional = false)
    @Column
    private String playlistStatus;

    @Basic (optional = false)
    @Column
    private LocalDateTime playlistCreated;

    @Basic (optional = false)
    @Column
    private LocalDateTime playlistUpdated;

    @ManyToMany
    @JoinTable(
        name = "PlaylistAssignment",
        joinColumns = @JoinColumn(name = "playlistId"),
        inverseJoinColumns = @JoinColumn(name = "lofiId")
    )
    private List<Lofi> playlistLofies;
}
