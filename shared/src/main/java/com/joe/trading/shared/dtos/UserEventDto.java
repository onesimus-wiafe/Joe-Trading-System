package com.joe.trading.shared.dtos;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserEventDto {
    private Long id;
    private String name;
    private String email;
    private String accountType;
    private Boolean pendingDelete;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
