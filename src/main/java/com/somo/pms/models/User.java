package com.somo.pms.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@ToString(exclude = "password")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users")
public class User {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Column( nullable = false)
    private String fullName;
    @Column( nullable = false, unique = true )
    private String email;
    @Column( nullable = false )
    private String password;
    @JsonIgnore
    @OneToMany(mappedBy = "assignee", cascade = CascadeType.ALL)
    private List<Issue> assignedIssues=new ArrayList<>();
    @Column( nullable = false)
    private int projectSize;
}
