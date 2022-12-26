package com.example.project_lofi.playlistassignment;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable @Data
public class PlaylistLofiAssignmentKey implements Serializable{
    
    @Column
    private long playlistId;

    @Column
    private long lofiId;
}
