package com.abdelrahman.ticketing.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentResponse {

    private Long id;
    private Long ticketId;
    private UserResponse user;
    private String content;
    private LocalDateTime createdAt;
}
