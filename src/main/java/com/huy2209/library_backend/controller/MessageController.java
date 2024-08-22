package com.huy2209.library_backend.controller;

import com.huy2209.library_backend.component.JwtUtil;
import com.huy2209.library_backend.dto.request.AdminMessageRequest;
import com.huy2209.library_backend.dto.request.MessageRequest;
import com.huy2209.library_backend.dto.response.MessageResponse;
import com.huy2209.library_backend.dto.response.PageMessageResponse;
import com.huy2209.library_backend.service.IMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/messages")
@RequiredArgsConstructor
@CrossOrigin("http://localhost:3000")
public class MessageController {
    private final IMessageService iMessageService;
    private final JwtUtil jwtUtil;

    @GetMapping()
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<PageMessageResponse> getAllMessage(@RequestHeader("Authorization") String token,
                                                             @RequestParam("page") int page,
                                                             @RequestParam("size") int size) throws Exception {
        try {
            String getToken = token.substring(7);
            String getEmail = jwtUtil.extractEmail(getToken);
            PageRequest pageRequest = PageRequest.of(page, size, Sort.by("id").ascending());
            Page<MessageResponse> messageResponses = iMessageService.getAllMessages(getEmail, pageRequest);
            int totalPages = messageResponses.getTotalPages();
            List<MessageResponse> messageResponseList = messageResponses.getContent();
            return ResponseEntity.ok().body(PageMessageResponse.builder()
                    .totalPages(totalPages).messageResponses(messageResponseList).build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<PageMessageResponse> getAdminMessage(@RequestParam("page") int page,
                                                             @RequestParam("size") int size,
                                                             @RequestParam("closed") boolean closed) throws Exception {
        try {
            PageRequest pageRequest = PageRequest.of(page, size, Sort.by("id").ascending());
            Page<MessageResponse> messageResponses = iMessageService.adminGetMessages(closed, pageRequest);
            int totalPages = messageResponses.getTotalPages();
            List<MessageResponse> messageResponseList = messageResponses.getContent();
            return ResponseEntity.ok().body(PageMessageResponse.builder()
                    .totalPages(totalPages).messageResponses(messageResponseList).build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/admin")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> putAdminMessage(@RequestHeader("Authorization") String token,
                                                  @RequestBody AdminMessageRequest adminMessageRequest) throws Exception{
        try {
            String getToken = token.substring(7);
            String getEmail = jwtUtil.extractEmail(getToken);
            iMessageService.putMessage(adminMessageRequest,getEmail);
            return ResponseEntity.ok().body("response success !!!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }



    @PostMapping()
    @PreAuthorize("hasRole('ROLE_USER')")
    public void postMessage(@RequestHeader("Authorization") String token,
                            @RequestBody MessageRequest messageRequest){
        String getToken = token.substring(7);
        String getEmail = jwtUtil.extractEmail(getToken);
        iMessageService.postMessage(messageRequest,getEmail);
    }
}
