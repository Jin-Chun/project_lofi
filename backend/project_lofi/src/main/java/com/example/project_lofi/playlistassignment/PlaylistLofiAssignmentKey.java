package com.example.project_lofi.playlistassignment;

import java.io.Serializable;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable @Data
public class PlaylistLofiAssignmentKey implements Serializable{
    
    @Column
    private long playlistId;

    @Column
    private long lofiId;

    @Column
    private LocalDateTime playlistLofiAssignmentTime;
}
