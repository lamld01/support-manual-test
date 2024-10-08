package com.lamld.supportmanualtest.app.dto.account;

import com.lamld.supportmanualtest.server.data.type.SellerRoleEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "The class for creating a new user account")
public class AccountCreate {

    @Schema(description = "The username of the account")
    @NotNull(message = "Username is required")
    private String username;

    @Schema(description = "The password for the account")
    @NotNull(message = "Password is required")
    private String password;
}