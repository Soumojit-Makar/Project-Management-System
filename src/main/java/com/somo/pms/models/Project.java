package com.somo.pms.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String name;
    @Column(columnDefinition = "TEXT")
    private String description;
    private String category;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> tags= new ArrayList<>();
    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL,mappedBy = "project",orphanRemoval = true)
    private Chat chat;
    @ManyToOne
    private User owner;
    @OneToMany(mappedBy = "project",cascade = CascadeType.ALL,orphanRemoval = true)
    private  List<Issue> issues=new ArrayList<>();
    @ManyToMany
    private List<User> team=new ArrayList<>();
}
