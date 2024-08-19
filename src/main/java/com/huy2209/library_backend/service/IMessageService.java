package com.huy2209.library_backend.service;

import com.huy2209.library_backend.dto.request.MessageRequest;
import com.huy2209.library_backend.dto.response.MessageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface IMessageService {
    void postMessage(MessageRequest message, String userEmail);
    Page<MessageResponse> getAllMessages(String getEmail, PageRequest pageRequest) throws Exception;
}
