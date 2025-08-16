package com.somo.pms.repositories;

import com.somo.pms.models.Project;
import com.somo.pms.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.awt.print.Pageable;
import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, String> {
    List<Project> findByOwner(User owner);
    @Query("SELECT p FROM Project p JOIN p.team t WHERE t=:user")
    List<Project> findByTeam(@Param("user") User team);
    List<Project> findByNameContainingAndTeamContains(String name, User team);
    List<Project> findByTeamContainingOrOwner(User user, User owner);
}
