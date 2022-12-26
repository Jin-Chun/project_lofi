package com.example.project_lofi.playlistassignment;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.example.project_lofi.lofi.Lofi;
import com.example.project_lofi.playlist.Playlist;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import lombok.Data;

@Entity @Data
public class PlaylistLofiAssignment implements Serializable {
    
    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private PlaylistLofiAssignmentKey playlistLofiAssignmentId;

    @ManyToOne
    @MapsId("playlistId")
    @JoinColumn(name = "playlistId")
    private Playlist playlist;

    @ManyToOne
    @MapsId("lofiId")
    @JoinColumn(name = "lofiId")
    private Lofi lofi;

    @Basic(optional = false)
    @Column
    private LocalDateTime playlistLofiAssignmentTime;
}
