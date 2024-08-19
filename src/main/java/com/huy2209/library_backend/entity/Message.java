package com.huy2209.library_backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "messages")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_email")
    private String userEmail;

    private String title;

    private String question;

    @Column(name = "admin_email")
    private String adminEmail;

    private String response;

    private boolean closed;
}
