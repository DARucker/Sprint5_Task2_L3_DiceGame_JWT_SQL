package com.sprint5.task2.fase3.sql.auth;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationRequest {

    @Schema(description = "This field is the EMAIL of the Player and will be used as the key for login and play.", example = "Dario@mail.com", required = true)
    private String email;
    @Schema(description = "This field is the Password and will be required to login.", example = "123", required = true)
    private String password;

}
