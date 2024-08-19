package com.huy2209.library_backend.service;

import com.huy2209.library_backend.dao.MessageRepository;
import com.huy2209.library_backend.dto.request.MessageRequest;
import com.huy2209.library_backend.dto.response.MessageResponse;
import com.huy2209.library_backend.entity.Message;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MessageService implements IMessageService{
    private final MessageRepository messageRepository;
    private final ModelMapper modelMapper;

    @Override
    public void postMessage(MessageRequest messageRequest, String userEmail) {
        Message message = Message.builder()
                    .title(messageRequest.getTitle())
                    .question(messageRequest.getQuestion())
                    .userEmail(userEmail)
                    .build();
        messageRepository.save(message);
    }

    @Override
    public Page<MessageResponse> getAllMessages(String getEmail, PageRequest pageRequest) throws Exception {
        Page<Message> messagePage = messageRepository.findByUserEmail(getEmail,pageRequest);
        return messagePage.map(message -> modelMapper.map(message,MessageResponse.class));
    }
}
