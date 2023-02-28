package com.example.project_lofi.lofipool;

import java.io.Serializable;
import java.util.List;

import com.example.project_lofi.lofi.Lofi;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

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
import lombok.Data;

/**
 * Defines Lofi Pool DTO
 * 
 * @author Gwanjin
 */
@Entity @Data
@JsonIgnoreProperties(value = {"poolLofies"})
public class LofiPool implements Serializable {
    
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long lofiPoolId;

    @Basic(optional = false) @Column(unique = true)
    private String lofiPoolName;

    @Basic(optional = false) @Column
    private String lofiPoolGenre;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "LofiPoolAssignment",
        joinColumns = @JoinColumn(name = "lofiPoolId"),
        inverseJoinColumns = @JoinColumn(name = "lofiId")
    )
    private List<Lofi> poolLofies;
}
