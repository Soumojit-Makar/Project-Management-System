package com.somo.pms.repositories;

import com.somo.pms.models.Invitation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface InvitationRepository extends JpaRepository<Invitation, String> {
    Optional<Invitation> findByToken(String token);
    Optional<Invitation> findByEmail(String email);
    List<Invitation> findByExpiresAtBefore(LocalDateTime dateTime);
}
