package com.somo.pms.services.imp;

import com.somo.pms.dto.request.InvitationSendRequest;
import com.somo.pms.dto.response.InvitationResponse;
import com.somo.pms.dto.response.ProjectResponse;
import com.somo.pms.models.Invitation;
import com.somo.pms.repositories.InvitationRepository;
import com.somo.pms.services.EmailService;
import com.somo.pms.services.InvitationService;
import com.somo.pms.services.ProjectService;
import jakarta.annotation.PostConstruct;
import jakarta.mail.MessagingException;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Data
@Log4j2
public class InvitationServiceImpl implements InvitationService {
    private final InvitationRepository invitationRepository;
    private final EmailService emailService;
    private final ProjectService projectService;
    @Value("${frontend.url}")
    String frontendLink;
    @Override
    public void sendInvite(InvitationSendRequest request) throws BadRequestException, MessagingException {
        String token = UUID.randomUUID().toString();
        Invitation invitation = new Invitation();
        invitation.setToken(token);
        invitation.setEmail(request.getEmail());
        invitation.setProjectId(request.getProjectId());
        invitation.setExpiresAt(LocalDateTime.now().plusDays(1));
        Invitation save = invitationRepository.save(invitation);
        String link=frontendLink+"/accept_invitation?token="+save.getToken();
        ProjectResponse project = projectService.getProjectById(save.getProjectId());
        emailService.sendEmailWithToken(save.getEmail(), link,project.getName(), save.getExpiresAt());
    }

    @Override
    public InvitationResponse acceptInvitation(String token, String userId) throws BadRequestException {
        Invitation invitation=invitationRepository.findByToken(token).orElseThrow(()->new BadRequestException("Invitation not found ! Invalid token"));
        return InvitationResponse
                .builder()
                .token(invitation.getToken())
                .email(invitation.getEmail())
                .projectId(invitation.getProjectId())
                .id(invitation.getId())
                .build();
    }
    @Override
    public String getTokenByUserMail(String mail) throws BadRequestException {
       return invitationRepository
               .findByEmail(mail)
               .orElseThrow(
                       ()-> new BadRequestException("Invitation not found ! Invalid email")
               )
               .getToken();
    }

    @Override
    public void deleteInvitation(String token) throws BadRequestException {
        invitationRepository
                .delete(
                        invitationRepository
                                .findByToken(token)
                                .orElseThrow(()-> new BadRequestException("Invitation Not found")
                                )
                );
    }
    @Scheduled(fixedRate = 3600000)
    public void cleanExpiredInvitations() {
        log.info("Starting expired invitation cleanup at {}", LocalDateTime.now());
        try {
            deleteExpiredInvitationsWithRetry();
        } catch (Exception e) {
            log.error("Failed to delete expired invitations after retries: {}", e.getMessage());
        }
    }
    @Retryable(
            retryFor = { RuntimeException.class },
            backoff = @Backoff(delay = 5000)
    )
    public void deleteExpiredInvitationsWithRetry() {
        List<Invitation> expiredInvitations = invitationRepository.findByExpiresAtBefore(LocalDateTime.now());

        if (expiredInvitations.isEmpty()) {
            log.info("No expired invitations found");
            return;
        }

        log.info("Found {} expired invitations to delete", expiredInvitations.size());
        for (Invitation invitation : expiredInvitations) {
            try {
                invitationRepository.delete(invitation);
                log.info("Deleted expired invitation with ID {}", invitation.getId());
            } catch (Exception e) {
                log.error("Error deleting invitation {}: {}", invitation.getId(), e.getMessage());
                throw new RuntimeException("Retryable deletion error");
            }
        }
    }
    @PostConstruct
    public void initScheduler() {
        log.info("Running scheduled cleanup immediately on startup...");
        cleanExpiredInvitations();
    }
}
