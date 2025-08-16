package com.somo.pms.services;

import com.somo.pms.dto.request.InvitationSendRequest;
import com.somo.pms.dto.response.InvitationResponse;
import com.somo.pms.models.Invitation;
import com.somo.pms.repositories.InvitationRepository;
import jakarta.mail.MessagingException;
import org.apache.coyote.BadRequestException;

public interface InvitationService {
    public void sendInvite(InvitationSendRequest request) throws BadRequestException, MessagingException;
    public InvitationResponse acceptInvitation(String token, String userId) throws BadRequestException;
    public String getTokenByUserMail(String mail) throws BadRequestException;
    public void deleteInvitation(String token) throws BadRequestException;
}
