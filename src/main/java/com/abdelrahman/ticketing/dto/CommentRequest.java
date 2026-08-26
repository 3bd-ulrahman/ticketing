package com.abdelrahman.ticketing.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentRequest {

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotBlank(message = "Comment content is required")
    @Size(max = 2000, message = "Comment must not exceed 2000 characters")
    private String content;
}
