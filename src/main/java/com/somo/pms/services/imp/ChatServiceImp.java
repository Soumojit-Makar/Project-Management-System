package com.somo.pms.services.imp;

import com.somo.pms.models.Chat;
import com.somo.pms.repositories.ChatRepository;
import com.somo.pms.services.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatServiceImp implements ChatService {
    private final ChatRepository chatRepository;
    @Override
    public Chat createChat(Chat chat) {
        return chatRepository.save(chat);
    }
}
