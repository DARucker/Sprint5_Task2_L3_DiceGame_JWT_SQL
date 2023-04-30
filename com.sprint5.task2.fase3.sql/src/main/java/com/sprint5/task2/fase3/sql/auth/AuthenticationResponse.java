package com.sprint5.task2.fase3.sql.auth;

import lombok.*;

@Data @Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {
    private String token;
}
