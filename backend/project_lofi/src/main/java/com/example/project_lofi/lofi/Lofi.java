package com.example.project_lofi.lofi;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;

import java.io.Serializable;
import java.util.List;

import com.example.project_lofi.lofipool.LofiPool;
import com.example.project_lofi.playlistassignment.PlaylistLofiAssignment;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Entity @Data
public class Lofi implements Serializable{
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long lofiId;

    @Basic(optional = false)
    @Column(unique = true)
    private String lofiLocation;

    @Basic(optional = false)
    @Column(unique = true)
    private String lofiName;

    @Basic(optional = false)
    @Column
    private String lofiType;

    @Basic(optional = true)
    @Column
    private String lofiAuthor;

    @Basic(optional = true)
    @Column
    private String lofiPlaytime;

    @JsonIgnore
    @OneToMany(mappedBy = "lofi")
    private List<PlaylistLofiAssignment> playlists;

    @JsonIgnore
    @ManyToMany(mappedBy = "poolLofies")
    private List<LofiPool> lofiPools;
}
