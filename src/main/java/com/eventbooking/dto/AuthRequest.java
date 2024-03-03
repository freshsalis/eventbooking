package com.eventbooking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthRequest {


    @NotBlank()
    @Email(message = "Email not a valid email address")
    @Size(min=8, max = 100, message = "password must not less than or equals to 8 characters")
    private String email;

    @NotBlank
    @Size(min=8, message = "password must not less than or equals to 8 characters")
    private String password;
}
